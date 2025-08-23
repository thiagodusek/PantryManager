package com.prantymanager.domain.repository

import com.prantymanager.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(user: User, password: String): Result<User>
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun isEmailAvailable(email: String): Result<Boolean>
    suspend fun isCpfAvailable(cpf: String): Result<Boolean>
}
