package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}
