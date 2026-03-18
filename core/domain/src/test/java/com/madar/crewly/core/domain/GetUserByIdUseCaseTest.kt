package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetUserByIdUseCaseTest {

    private val existingUser = User(1L, "Alice", 28, "Manager", "Female", 1000L)

    private val repo = object : UserRepository {
        override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
        override fun getAllUsers() = flowOf(emptyList<User>())
        override fun getUserCount() = MutableStateFlow(0)
        override fun getUserById(userId: Long) = flowOf(if (userId == 1L) existingUser else null)
        override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
        override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
    }

    private lateinit var useCase: GetUserByIdUseCase

    @Before
    fun setup() {
        useCase = GetUserByIdUseCase(repo)
    }

    @Test
    fun `invoke should return user when user exists`(): Unit = runBlocking {
        var result: User? = null
        useCase(1L).collectLatest { result = it }
        assertEquals(existingUser, result)
    }

    @Test
    fun `invoke should return null when user does not exist`(): Unit = runBlocking {
        var result: User? = User(0L, "", 0, "", "")
        useCase(99L).collectLatest { result = it }
        assertNull(result)
    }
}
