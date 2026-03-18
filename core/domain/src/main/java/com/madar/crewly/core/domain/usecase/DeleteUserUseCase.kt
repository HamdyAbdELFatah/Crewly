package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.repository.UserRepository

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long): Result<Unit> {
        return userRepository.deleteUser(userId)
    }
}
