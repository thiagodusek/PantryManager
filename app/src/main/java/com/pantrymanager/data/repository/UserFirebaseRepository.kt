package com.pantrymanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pantrymanager.data.dto.UserFirebaseDto
import com.pantrymanager.data.dto.toDomainEntity
import com.pantrymanager.data.dto.toFirebaseDto
import com.pantrymanager.domain.entity.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementação do repositório Firebase para dados do usuário
 */
@Singleton
class UserFirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    companion object {
        private const val USERS_COLLECTION = "users"
    }

    /**
     * Salva ou atualiza dados do usuário no Firestore
     */
    suspend fun saveUser(user: User): Result<User> {
        return try {
            val userDto = user.toFirebaseDto()
            val userId = user.id.ifEmpty { auth.currentUser?.uid ?: generateUserId() }
            val updatedUser = user.copy(
                id = userId,
                updatedAt = System.currentTimeMillis()
            )
            val updatedUserDto = updatedUser.toFirebaseDto()
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(updatedUserDto)
                .await()
                
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca dados do usuário por ID
     */
    suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
                
            if (document.exists()) {
                val userDto = document.toObject(UserFirebaseDto::class.java)
                val user = userDto?.toDomainEntity()
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca dados do usuário atual logado
     */
    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                getUserById(currentUserId)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca usuário por email
     */
    suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
                
            if (!querySnapshot.isEmpty) {
                val userDto = querySnapshot.documents[0].toObject(UserFirebaseDto::class.java)
                val user = userDto?.toDomainEntity()
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca usuário por CPF
     */
    suspend fun getUserByCpf(cpf: String): Result<User?> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("cpf", cpf)
                .limit(1)
                .get()
                .await()
                
            if (!querySnapshot.isEmpty) {
                val userDto = querySnapshot.documents[0].toObject(UserFirebaseDto::class.java)
                val user = userDto?.toDomainEntity()
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Busca usuário por login
     */
    suspend fun getUserByLogin(login: String): Result<User?> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("login", login)
                .limit(1)
                .get()
                .await()
                
            if (!querySnapshot.isEmpty) {
                val userDto = querySnapshot.documents[0].toObject(UserFirebaseDto::class.java)
                val user = userDto?.toDomainEntity()
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Atualiza dados do usuário
     */
    suspend fun updateUser(user: User): Result<User> {
        return try {
            val userId = user.id.ifEmpty { auth.currentUser?.uid ?: throw IllegalStateException("User ID is required") }
            val updatedUser = user.copy(
                id = userId,
                updatedAt = System.currentTimeMillis()
            )
            val userDto = updatedUser.toFirebaseDto()
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(userDto)
                .await()
                
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deleta dados do usuário
     */
    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .delete()
                .await()
                
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifica se email já está em uso
     */
    suspend fun isEmailInUse(email: String): Result<Boolean> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
                
            Result.success(!querySnapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifica se CPF já está em uso
     */
    suspend fun isCpfInUse(cpf: String): Result<Boolean> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("cpf", cpf)
                .limit(1)
                .get()
                .await()
                
            Result.success(!querySnapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifica se login já está em uso
     */
    suspend fun isLoginInUse(login: String): Result<Boolean> {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("login", login)
                .limit(1)
                .get()
                .await()
                
            Result.success(!querySnapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Gera um ID único para o usuário
     */
    private fun generateUserId(): String {
        return firestore.collection(USERS_COLLECTION).document().id
    }
}
