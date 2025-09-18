package com.pantrymanager.domain.usecase.auth

import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(login: String, password: String): Result<User> {
        return try {
            if (login.isBlank() || password.isBlank()) {
                return Result.failure(Exception("Login and password are required"))
            }
            
            userRepository.loginWithEmailAndPassword(login, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
