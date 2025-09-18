package com.pantrymanager.domain.usecase.user

import com.pantrymanager.data.repository.UserFirebaseRepository
import com.pantrymanager.domain.entity.User
import javax.inject.Inject

/**
 * Use case para salvar dados do usuário
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserFirebaseRepository
) {
    suspend operator fun invoke(user: User): Result<User> {
        return userRepository.saveUser(user)
    }
}
