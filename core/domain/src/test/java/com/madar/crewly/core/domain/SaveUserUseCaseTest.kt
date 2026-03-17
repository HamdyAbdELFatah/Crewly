package com.madar.crewly.core.domain

import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
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
