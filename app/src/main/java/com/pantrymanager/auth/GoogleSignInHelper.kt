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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSignInHelper @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {
    
    private val googleSignInClient: GoogleSignInClient
    
    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
            
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    private suspend fun refreshTokenIfNeeded(): Boolean {
        return try {
            val lastAccount = GoogleSignIn.getLastSignedInAccount(context)
            if (lastAccount != null && !lastAccount.isExpired) {
                // Tentar refresh silencioso
                val task = googleSignInClient.silentSignIn()
                task.await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.w("GoogleSignInHelper", "Failed to refresh token silently", e)
            false
        }
    }
    
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): Result<String> {
        return try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            
            if (idToken != null) {
                // Verificar se o token não está expirado
                if (account.isExpired) {
                    Log.w("GoogleSignInHelper", "Google account token is expired, attempting refresh")
                    if (!refreshTokenIfNeeded()) {
                        return Result.failure(Exception("Token expirado. Por favor, faça login novamente."))
                    }
                }
                
                // Sign in to Firebase with Google credentials
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                
                try {
                    firebaseAuth.signInWithCredential(credential).await()
                    Log.d("GoogleSignInHelper", "Google Sign-In successful")
                    Result.success(idToken)
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
                            // Tentar limpar cache e retry
                            signOut()
                            Result.failure(Exception("Credencial inválida. Por favor, tente fazer login novamente."))
                        }
                        else -> {
                            Result.failure(Exception("Erro de autenticação: ${authException.message}"))
                        }
                    }
                }
            } else {
                Result.failure(Exception("ID Token é nulo"))
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignInHelper", "Google Sign-In API Exception: ${e.statusCode}", e)
            when (e.statusCode) {
                12501 -> Result.failure(Exception("Login cancelado pelo usuário"))
                12502 -> Result.failure(Exception("Erro de rede. Verifique sua conexão"))
                else -> Result.failure(Exception("Google Sign-In falhou: ${e.message}"))
            }
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Unexpected error during sign-in", e)
            Result.failure(Exception("Erro na autenticação: ${e.message}"))
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
            Log.d("GoogleSignInHelper", "Clearing all credentials")
            
            // Revoke Google access
            googleSignInClient.revokeAccess().await()
            
            // Sign out from both services
            signOut()
            
        } catch (e: Exception) {
            Log.e("GoogleSignInHelper", "Error clearing credentials", e)
        }
    }
    
    fun getCurrentUser() = firebaseAuth.currentUser
}
