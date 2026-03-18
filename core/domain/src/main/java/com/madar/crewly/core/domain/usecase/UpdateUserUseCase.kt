package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return userRepository.updateUser(user)
    }
}
