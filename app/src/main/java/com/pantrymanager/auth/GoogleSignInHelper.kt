package com.pantrymanager.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.pantrymanager.R
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInHelper @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {
    
    private val googleSignInClient: GoogleSignInClient
    
    init {
        try {
            val webClientId = context.getString(R.string.default_web_client_id)
            Log.d("GoogleSignInHelper", "Initializing with Web Client ID: $webClientId")
            Log.d("GoogleSignInHelper", "Package Name: ${context.packageName}")
            
            // Verificar se o web client id está configurado
            if (webClientId.isEmpty() || webClientId.contains("your_web_client_id")) {
                Log.e("GoogleSignInHelper", "Web Client ID not properly configured!")
                throw IllegalStateException("Web Client ID not configured in strings.xml")
            }
            
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .requestProfile()
                .build()
                
            googleSignInClient = GoogleSignIn.getClient(context, gso)
            Log.d("GoogleSignInHelper", "GoogleSignInHelper initialized successfully")
            
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Failed to initialize GoogleSignInHelper", e)
            throw e
        }
    }
    
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    private suspend fun refreshTokenIfNeeded(): Boolean {
        return try {
            val lastAccount = GoogleSignIn.getLastSignedInAccount(context)
            if (lastAccount != null) {
                // Sempre tentar refresh silencioso para garantir token válido
                val task = googleSignInClient.silentSignIn()
                val refreshedAccount = task.await()
                Log.d("GoogleSignInHelper", "Token refreshed successfully for: ${refreshedAccount.email}")
                true
            } else {
                Log.d("GoogleSignInHelper", "No last signed in account found")
                false
            }
        } catch (e: Exception) {
            Log.w("GoogleSignInHelper", "Failed to refresh token silently", e)
            // Limpar credenciais inválidas
            try {
                googleSignInClient.signOut().await()
            } catch (signOutException: Exception) {
                Log.w("GoogleSignInHelper", "Failed to sign out after refresh failure", signOutException)
            }
            false
        }
    }
    
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): Result<String> {
        return try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            
            Log.d("GoogleSignInHelper", "Handling sign-in result for: ${account.email}")
            Log.d("GoogleSignInHelper", "Account expired: ${account.isExpired}")
            Log.d("GoogleSignInHelper", "ID Token null: ${idToken == null}")
            
            if (idToken != null) {
                // Sempre tentar refresh se a conta existir (não confiar apenas no isExpired)
                val refreshSuccess = refreshTokenIfNeeded()
                Log.d("GoogleSignInHelper", "Token refresh attempt result: $refreshSuccess")
                
                // Usar a conta atual (pode ter sido atualizada pelo refresh)
                val currentAccount = GoogleSignIn.getLastSignedInAccount(context) ?: account
                val currentIdToken = currentAccount.idToken ?: idToken
                
                // Sign in to Firebase with Google credentials
                val credential = GoogleAuthProvider.getCredential(currentIdToken, null)
                
                try {
                    val authResult = firebaseAuth.signInWithCredential(credential).await()
                    Log.d("GoogleSignInHelper", "Firebase sign-in successful for: ${authResult.user?.email}")
                    Result.success(currentIdToken)
                } catch (authException: FirebaseAuthException) {
                    Log.e("GoogleSignInHelper", "Firebase auth failed: ${authException.errorCode}", authException)
                    when (authException.errorCode) {
                        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                            Result.failure(Exception("Esta conta já existe com um método de login diferente."))
                        }
                        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> {
                            Result.failure(Exception("Esta credencial já está em uso por outra conta."))
                        }
                        "ERROR_INVALID_CREDENTIAL" -> {
                            // Tentar limpar completamente o cache e forçar novo login
                            Log.w("GoogleSignInHelper", "Invalid credential, clearing all caches")
                            clearCredentials()
                            Result.failure(Exception("Credencial inválida. Cache limpo. Tente fazer login novamente."))
                        }
                        "ERROR_USER_DISABLED" -> {
                            Result.failure(Exception("Esta conta foi desabilitada pelo administrador."))
                        }
                        "ERROR_USER_TOKEN_EXPIRED" -> {
                            Log.w("GoogleSignInHelper", "User token expired, clearing credentials")
                            clearCredentials()
                            Result.failure(Exception("Token expirado. Cache limpo. Faça login novamente."))
                        }
                        else -> {
                            Result.failure(Exception("Erro de autenticação Firebase: ${authException.message}"))
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GoogleSignInHelper", "Unexpected Firebase auth error", e)
                    Result.failure(Exception("Erro inesperado na autenticação: ${e.message}"))
                }
            } else {
                Log.e("GoogleSignInHelper", "ID Token is null")
                Result.failure(Exception("Token de autenticação inválido. Tente novamente."))
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignInHelper", "Google Sign-In API Exception: ${e.statusCode} - ${e.message}", e)
            when (e.statusCode) {
                12501 -> Result.failure(Exception("Login cancelado pelo usuário"))
                12502 -> Result.failure(Exception("Erro de rede. Verifique sua conexão"))
                12500 -> {
                    Log.e("GoogleSignInHelper", "Sign-in failed - clearing credentials")
                    clearCredentials()
                    Result.failure(Exception("Falha no login Google. Cache limpo. Tente novamente."))
                }
                10 -> {
                    // Erro comum quando SHA-1 não está configurado ou credential está incorreta
                    Log.e("GoogleSignInHelper", "Developer error - check SHA-1 and client configuration")
                    Log.e("GoogleSignInHelper", "Package: ${context.packageName}")
                    Log.e("GoogleSignInHelper", "Web Client ID: ${context.getString(R.string.default_web_client_id)}")
                    
                    // Limpar credenciais e tentar reconfigurar
                    clearCredentials()
                    
                    Result.failure(Exception("🔧 Erro de configuração do Google Sign-In\n\n" +
                            "Para corrigir, siga estes passos:\n\n" +
                            "1️⃣ Acesse o Firebase Console\n" +
                            "2️⃣ Vá em 'Authentication' > 'Sign-in method'\n" +
                            "3️⃣ Configure o Google Sign-In\n" +
                            "4️⃣ Adicione o SHA-1 fingerprint:\n" +
                            "   36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5\n" +
                            "5️⃣ Baixe o google-services.json atualizado\n" +
                            "6️⃣ Substitua o arquivo no projeto\n\n" +
                            "Package: ${context.packageName}"))
                }
                else -> Result.failure(Exception("Google Sign-In falhou (código: ${e.statusCode}): ${e.message}"))
            }
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Unexpected error during sign-in", e)
            Result.failure(Exception("Erro inesperado na autenticação: ${e.message}"))
        }
    }
    
    suspend fun signOut() {
        try {
            Log.d("GoogleSignInHelper", "Signing out from Firebase and Google")
            
            // Sign out from Firebase
            firebaseAuth.signOut()
            
            // Sign out from Google
            googleSignInClient.signOut().await()
            
            // Revoke access to completely clear credentials
            googleSignInClient.revokeAccess().await()
            
            Log.d("GoogleSignInHelper", "Sign out completed successfully")
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Error during sign out", e)
            // Don't throw exception, just log it
        }
    }
    
    suspend fun clearCredentials() {
        try {
            Log.d("GoogleSignInHelper", "Clearing all credentials and caches")
            
            // 1. Sign out from Firebase first
            firebaseAuth.signOut()
            Log.d("GoogleSignInHelper", "Firebase sign out completed")
            
            // 2. Revoke Google access (this clears all tokens)
            try {
                googleSignInClient.revokeAccess().await()
                Log.d("GoogleSignInHelper", "Google access revoked")
            } catch (revokeException: Exception) {
                Log.w("GoogleSignInHelper", "Failed to revoke Google access", revokeException)
                // Continue with sign out even if revoke fails
            }
            
            // 3. Sign out from Google (clears local cache)
            try {
                googleSignInClient.signOut().await()
                Log.d("GoogleSignInHelper", "Google sign out completed")
            } catch (signOutException: Exception) {
                Log.w("GoogleSignInHelper", "Failed to sign out from Google", signOutException)
            }
            
            Log.d("GoogleSignInHelper", "All credentials cleared successfully")
            
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Error clearing credentials", e)
        }
    }
    
    fun getCurrentUser() = firebaseAuth.currentUser
    
    /**
     * Força um novo login limpo, removendo todas as credenciais e caches
     * Use esta função quando houver problemas persistentes de autenticação
     */
    suspend fun forceCleanLogin(): Intent {
        Log.d("GoogleSignInHelper", "Forcing clean login - clearing all credentials")
        
        // Limpar todas as credenciais
        clearCredentials()
        
        // Aguardar um pouco para garantir que a limpeza foi processada
        delay(1000)
        
        // Retornar intent para novo login
        return getSignInIntent()
    }
    
    /**
     * Verifica se há um usuário logado e se as credenciais são válidas
     */
    fun isUserSignedInAndValid(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
        
        return firebaseUser != null && 
               googleAccount != null && 
               !googleAccount.isExpired &&
               googleAccount.idToken != null
    }
    
    /**
     * Verifica se a configuração do Google Sign-In está correta
     */
    fun checkConfiguration(): String {
        return try {
            val packageName = context.packageName
            val webClientId = context.getString(R.string.default_web_client_id)
            
            Log.d("GoogleSignInHelper", "=== GOOGLE SIGN-IN CONFIGURATION CHECK ===")
            Log.d("GoogleSignInHelper", "Package Name: $packageName")
            Log.d("GoogleSignInHelper", "Web Client ID: $webClientId")
            
            val configInfo = StringBuilder()
            configInfo.append("📋 Configuração do Google Sign-In:\n\n")
            configInfo.append("📦 Package: $packageName\n")
            configInfo.append("🔑 Web Client ID: $webClientId\n\n")
            
            // Verificar se o web client id não é o padrão do template
            if (webClientId.contains("your_web_client_id") || webClientId.isEmpty()) {
                configInfo.append("❌ ERRO: Web Client ID não configurado!\n\n")
            } else {
                configInfo.append("✅ Web Client ID configurado\n\n")
            }
            
            // Informações sobre configuração necessária
            configInfo.append("🔧 Configuração necessária no Firebase Console:\n")
            configInfo.append("• SHA-1 Debug: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5\n")
            configInfo.append("• Package: $packageName\n")
            configInfo.append("• google-services.json atualizado\n\n")
            
            // Verificar se há conta já logada
            val lastAccount = GoogleSignIn.getLastSignedInAccount(context)
            if (lastAccount != null) {
                configInfo.append("👤 Última conta: ${lastAccount.email}\n")
                configInfo.append("⏰ Token expirado: ${if (lastAccount.isExpired) "Sim" else "Não"}\n")
            } else {
                configInfo.append("👤 Nenhuma conta logada anteriormente\n")
            }
            
            configInfo.toString()
            
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Error checking configuration", e)
            "❌ Erro ao verificar configuração: ${e.message}"
        }
    }
}
