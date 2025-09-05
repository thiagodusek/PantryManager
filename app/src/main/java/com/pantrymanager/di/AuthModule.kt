package com.pantrymanager.di

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.pantrymanager.auth.GoogleSignInHelper
import com.pantrymanager.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth {
        // Verificar se o Firebase foi inicializado corretamente
        try {
            FirebaseApp.initializeApp(context)
            val auth = FirebaseAuth.getInstance()
            Log.d("AuthModule", "Firebase Auth inicializado com sucesso")
            Log.d("AuthModule", "Firebase App ID: ${FirebaseApp.getInstance().options.applicationId}")
            return auth
        } catch (e: Exception) {
            Log.e("AuthModule", "Erro ao inicializar Firebase Auth", e)
            throw e
        }
    }
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(@ApplicationContext context: Context): FirebaseFirestore {
        try {
            val firestore = FirebaseFirestore.getInstance()
            
            // Configurações para melhor conectividade
            val settings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Cache local
                .setCacheSizeBytes(com.google.firebase.firestore.FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build()
            
            firestore.firestoreSettings = settings
            
            // Force network connectivity
            firestore.enableNetwork().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthModule", "Firestore network enabled successfully")
                } else {
                    Log.e("AuthModule", "Failed to enable Firestore network", task.exception)
                }
            }
            
            Log.d("AuthModule", "Firebase Firestore inicializado com sucesso")
            return firestore
        } catch (e: Exception) {
            Log.e("AuthModule", "Erro ao inicializar Firebase Firestore", e)
            throw e
        }
    }
    
    @Provides
    @Singleton
    fun provideGoogleSignInHelper(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth
    ): GoogleSignInHelper = GoogleSignInHelper(context, firebaseAuth)
    
    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils = NetworkUtils()
}
