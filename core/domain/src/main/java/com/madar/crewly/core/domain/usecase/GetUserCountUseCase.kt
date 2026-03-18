package com.madar.crewly.core.domain.usecase

import com.madar.crewly.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserCountUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Int> = userRepository.getUserCount()
}
