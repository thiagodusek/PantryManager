package com.prantymanager.domain.usecase.auth

import com.prantymanager.domain.entity.User
import com.prantymanager.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, password: String, confirmPassword: String): Result<User> {
        return try {
            // Validate input
            if (password != confirmPassword) {
                return Result.failure(Exception("Passwords do not match"))
            }
            
            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }
            
            // Check if email is available
            val emailAvailable = userRepository.isEmailAvailable(user.email)
            if (emailAvailable.isFailure || emailAvailable.getOrNull() == false) {
                return Result.failure(Exception("Email already in use"))
            }
            
            // Check if CPF is available
            val cpfAvailable = userRepository.isCpfAvailable(user.cpf)
            if (cpfAvailable.isFailure || cpfAvailable.getOrNull() == false) {
                return Result.failure(Exception("CPF already in use"))
            }
            
            userRepository.registerUser(user, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
