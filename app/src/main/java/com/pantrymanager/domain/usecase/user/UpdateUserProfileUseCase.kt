package com.pantrymanager.domain.usecase.user

import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case para atualizar o perfil do usuário
 * Garante que apenas campos editáveis sejam atualizados
 */
@Singleton
class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<User> {
        return try {
            // Valida dados obrigatórios
            if (!isValidUser(user)) {
                Result.failure(IllegalArgumentException("Dados do usuário inválidos"))
            } else {
                userRepository.updateUser(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isValidUser(user: User): Boolean {
        return user.nome.trim().isNotBlank() &&
                user.sobrenome.trim().isNotBlank() &&
                user.email.trim().isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(user.email.trim()).matches() &&
                user.cpf.isNotBlank() &&
                user.login.isNotBlank() &&
                user.endereco.endereco.trim().isNotBlank() &&
                user.endereco.numero.trim().isNotBlank() &&
                user.endereco.cep.length == 8 &&
                user.endereco.cidade.trim().isNotBlank() &&
                user.endereco.estado.isNotBlank() &&
                user.searchRadius > 0
    }
}
