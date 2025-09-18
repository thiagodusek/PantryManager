package com.pantrymanager.domain.usecase.auth

import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.repository.UserRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return try {
            userRepository.loginWithGoogle(idToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
