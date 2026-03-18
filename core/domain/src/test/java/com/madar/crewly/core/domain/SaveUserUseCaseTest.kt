package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SaveUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: SaveUserUseCase

    @Before
    fun setup() {
        userRepository = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(1L)
            override fun getAllUsers() = MutableStateFlow<List<User>>(emptyList())
            override fun getUserCount() = MutableStateFlow(0)
            override fun getUserById(userId: Long) = flowOf<User?>(null)
            override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
            override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
        }
        useCase = SaveUserUseCase(userRepository)
    }

    @Test
    fun `invoke should return success when repository saves user successfully`(): Unit = runBlocking {
        val user = User(
            id = 1L,
            name = "John Doe",
            age = 30,
            jobTitle = "Engineer",
            gender = "Male"
        )

        val result = useCase(user)

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository fails`(): Unit = runBlocking {
        userRepository = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.failure(RuntimeException("Error"))
            override fun getAllUsers() = MutableStateFlow<List<User>>(emptyList())
            override fun getUserCount() = MutableStateFlow(0)
            override fun getUserById(userId: Long) = flowOf<User?>(null)
            override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
            override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
        }
        useCase = SaveUserUseCase(userRepository)
        
        val user = User(
            id = 1L,
            name = "John Doe",
            age = 30,
            jobTitle = "Engineer",
            gender = "Male"
        )

        val result = useCase(user)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }
}
