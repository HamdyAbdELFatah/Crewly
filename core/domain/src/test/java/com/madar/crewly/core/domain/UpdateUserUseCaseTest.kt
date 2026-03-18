package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateUserUseCaseTest {

    private lateinit var useCase: UpdateUserUseCase

    private val repo = object : UserRepository {
        override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
        override fun getAllUsers() = flowOf(emptyList<User>())
        override fun getUserCount() = MutableStateFlow(0)
        override fun getUserById(userId: Long) = flowOf<User?>(null)
        override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
        override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
    }

    @Before
    fun setup() {
        useCase = UpdateUserUseCase(repo)
    }

    @Test
    fun `invoke should return success when user is updated`(): Unit = runBlocking {
        val user = User(1L, "Alice", 28, "Manager", "Female")
        val result = useCase(user)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`(): Unit = runBlocking {
        val failingRepo = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = flowOf(emptyList<User>())
            override fun getUserCount() = MutableStateFlow(0)
            override fun getUserById(userId: Long) = flowOf<User?>(null)
            override suspend fun updateUser(user: User): Result<Unit> =
                Result.failure(RuntimeException("Update failed"))
            override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
        }
        val result = UpdateUserUseCase(failingRepo)(User(1L, "Alice", 28, "Manager", "Female"))
        assertTrue(result.isFailure)
        assertEquals("Update failed", result.exceptionOrNull()?.message)
    }
}
