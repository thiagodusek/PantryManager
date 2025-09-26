package com.pantrymanager.di

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth {
        // Verificar se o Firebase foi inicializado corretamente
        try {
            FirebaseApp.initializeApp(context)
            val auth = FirebaseAuth.getInstance()
            Log.d("FirebaseModule", "Firebase Auth inicializado com sucesso")
            Log.d("FirebaseModule", "Firebase App ID: ${FirebaseApp.getInstance().options.applicationId}")
            return auth
        } catch (e: Exception) {
            Log.e("FirebaseModule", "Erro ao inicializar Firebase Auth", e)
            throw e
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(@ApplicationContext context: Context): FirebaseFirestore {
        try {
            val firestore = FirebaseFirestore.getInstance()
            
            // Configurações avançadas para resolver problemas de conectividade
            val settings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Cache local para offline
                .setCacheSizeBytes(com.google.firebase.firestore.FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .setHost("firestore.googleapis.com") // Host explícito
                .setSslEnabled(true) // Forçar SSL
                .build()
            
            firestore.firestoreSettings = settings
            
            // Forçar limpeza e reconexão
            try {
                // Primeiro desabilita e depois habilita para forçar reconexão
                firestore.disableNetwork().addOnCompleteListener {
                    firestore.enableNetwork().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FirebaseModule", "Firestore network forcefully reconnected")
                        } else {
                            Log.w("FirebaseModule", "Failed to reconnect Firestore", task.exception)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("FirebaseModule", "Error during network reset, continuing...", e)
            }
            
            Log.d("FirebaseModule", "Firebase Firestore inicializado com configurações avançadas")
            return firestore
        } catch (e: Exception) {
            Log.e("FirebaseModule", "Erro ao inicializar Firebase Firestore", e)
            throw e
        }
    }
}
