package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.DeleteUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteUserUseCaseTest {

    private lateinit var useCase: DeleteUserUseCase

    @Before
    fun setup() {
        val repo = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = flowOf(emptyList<User>())
            override fun getUserCount() = MutableStateFlow(0)
            override fun getUserById(userId: Long) = flowOf<User?>(null)
            override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
            override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
        }
        useCase = DeleteUserUseCase(repo)
    }

    @Test
    fun `invoke should return success when repository deletes successfully`(): Unit = runBlocking {
        val result = useCase(1L)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`(): Unit = runBlocking {
        val failingRepo = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = flowOf(emptyList<User>())
            override fun getUserCount() = MutableStateFlow(0)
            override fun getUserById(userId: Long) = flowOf<User?>(null)
            override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
            override suspend fun deleteUser(userId: Long): Result<Unit> =
                Result.failure(RuntimeException("Delete failed"))
        }
        val result = DeleteUserUseCase(failingRepo)(1L)
        assertTrue(result.isFailure)
    }
}
