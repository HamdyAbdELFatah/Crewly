package com.madar.crewly.core.domain

import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}
