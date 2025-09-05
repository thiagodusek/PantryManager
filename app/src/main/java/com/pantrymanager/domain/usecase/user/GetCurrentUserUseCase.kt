package com.pantrymanager.domain.usecase.user

import com.pantrymanager.data.repository.UserFirebaseRepository
import com.pantrymanager.domain.entity.User
import javax.inject.Inject

/**
 * Use case para buscar dados do usuário atual
 */
class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserFirebaseRepository
) {
    suspend operator fun invoke(): Result<User?> {
        return userRepository.getCurrentUser()
    }
}
