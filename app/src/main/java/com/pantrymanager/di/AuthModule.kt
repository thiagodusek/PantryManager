package com.pantrymanager.di

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.pantrymanager.auth.GoogleSignInHelper
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
}
