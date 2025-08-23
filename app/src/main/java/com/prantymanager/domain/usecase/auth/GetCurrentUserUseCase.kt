package com.prantymanager.domain.usecase.auth

import com.prantymanager.domain.entity.User
import kotlinx.coroutines.flow.Flow
import com.prantymanager.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return userRepository.getCurrentUser()
    }
}
