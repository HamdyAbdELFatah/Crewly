package com.madar.crewly.core.domain.repository

import com.madar.crewly.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User): Result<Long>
    fun getAllUsers(): Flow<List<User>>
    fun getUserCount(): Flow<Int>
    fun getUserById(userId: Long): Flow<User?>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun deleteUser(userId: Long): Result<Unit>
}
