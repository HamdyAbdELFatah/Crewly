package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepository(
    private val users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
) : UserRepository {
    
    private var nextId = 1L

    override suspend fun saveUser(user: User): Result<Long> {
        val newUser = user.copy(id = nextId++)
        users.value = users.value + newUser
        return Result.success(newUser.id)
    }

    override fun getAllUsers(): Flow<List<User>> = users

    override fun getUserCount(): Flow<Int> = MutableStateFlow(users.value.size)

    override fun getUserById(userId: Long): Flow<User?> = flowOf(users.value.find { it.id == userId })

    override suspend fun updateUser(user: User): Result<Unit> {
        users.value = users.value.map { if (it.id == user.id) user else it }
        return Result.success(Unit)
    }

    override suspend fun deleteUser(userId: Long): Result<Unit> {
        users.value = users.value.filter { it.id != userId }
        return Result.success(Unit)
    }

    fun addUser(user: User) {
        users.value = users.value + user
    }

    fun clearUsers() {
        users.value = emptyList()
    }
}
