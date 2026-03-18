package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: Long): Flow<User?> {
        return userRepository.getUserById(userId)
    }
}
