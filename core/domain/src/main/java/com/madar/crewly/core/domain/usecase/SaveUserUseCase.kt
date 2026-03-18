package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Long> {
        return userRepository.saveUser(user)
    }
}
