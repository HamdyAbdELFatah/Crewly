package com.madar.crewly.core.domain

import com.madar.crewly.core.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User): Result<Long>
    fun getAllUsers(): Flow<List<User>>
    fun getUserCount(): Flow<Int>
}
