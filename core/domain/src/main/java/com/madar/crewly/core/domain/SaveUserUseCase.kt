package com.madar.crewly.core.domain

import com.madar.crewly.core.common.ResultWrapper
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Long> {
        return userRepository.saveUser(user)
    }
}
