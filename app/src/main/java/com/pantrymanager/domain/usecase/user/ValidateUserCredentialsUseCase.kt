package com.pantrymanager.domain.usecase.user

import com.pantrymanager.data.repository.UserFirebaseRepository
import javax.inject.Inject

/**
 * Use case para validar se email, CPF ou login já estão em uso
 */
class ValidateUserCredentialsUseCase @Inject constructor(
    private val userRepository: UserFirebaseRepository
) {
    suspend fun isEmailInUse(email: String): Result<Boolean> {
        return userRepository.isEmailInUse(email)
    }
    
    suspend fun isCpfInUse(cpf: String): Result<Boolean> {
        return userRepository.isCpfInUse(cpf)
    }
    
    suspend fun isLoginInUse(login: String): Result<Boolean> {
        return userRepository.isLoginInUse(login)
    }
    
    /**
     * Valida todos os campos únicos de uma só vez
     */
    suspend fun validateUniqueFields(email: String, cpf: String, login: String): ValidationResult {
        val emailResult = isEmailInUse(email)
        val cpfResult = isCpfInUse(cpf)
        val loginResult = isLoginInUse(login)
        
        return when {
            emailResult.isFailure -> ValidationResult.Error("Erro ao validar email: ${emailResult.exceptionOrNull()?.message}")
            cpfResult.isFailure -> ValidationResult.Error("Erro ao validar CPF: ${cpfResult.exceptionOrNull()?.message}")
            loginResult.isFailure -> ValidationResult.Error("Erro ao validar login: ${loginResult.exceptionOrNull()?.message}")
            emailResult.getOrDefault(false) -> ValidationResult.EmailInUse
            cpfResult.getOrDefault(false) -> ValidationResult.CpfInUse
            loginResult.getOrDefault(false) -> ValidationResult.LoginInUse
            else -> ValidationResult.AllUnique
        }
    }
}

sealed class ValidationResult {
    object AllUnique : ValidationResult()
    object EmailInUse : ValidationResult()
    object CpfInUse : ValidationResult()
    object LoginInUse : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
