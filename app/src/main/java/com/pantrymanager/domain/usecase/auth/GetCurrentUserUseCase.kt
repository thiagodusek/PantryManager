package com.pantrymanager.domain.usecase.auth

import com.pantrymanager.domain.entity.User
import kotlinx.coroutines.flow.Flow
import com.pantrymanager.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return userRepository.getCurrentUser()
    }
}
