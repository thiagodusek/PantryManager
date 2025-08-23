package com.prantymanager.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.prantymanager.R
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
            .build()
            
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): Result<String> {
        return try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            
            if (idToken != null) {
                // Sign in to Firebase with Google credentials
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
                
                Result.success(idToken)
            } else {
                Result.failure(Exception("ID Token é nulo"))
            }
        } catch (e: ApiException) {
            Result.failure(Exception("Google Sign-In falhou: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Erro na autenticação: ${e.message}"))
        }
    }
    
    suspend fun signOut() {
        try {
            firebaseAuth.signOut()
            googleSignInClient.signOut().await()
        } catch (e: Exception) {
            // Log error but don't throw
        }
    }
    
    fun getCurrentUser() = firebaseAuth.currentUser
}
