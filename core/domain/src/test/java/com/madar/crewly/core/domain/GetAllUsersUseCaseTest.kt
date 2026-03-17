package com.madar.crewly.core.domain

import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var useCase: GetAllUsersUseCase
    private val usersFlow = MutableStateFlow<List<User>>(emptyList())

    @Before
    fun setup() {
        userRepository = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = usersFlow
            override fun getUserCount() = MutableStateFlow(0)
        }
        useCase = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return flow of users from repository`(): Unit = runBlocking {
        val users = listOf(
            User(1L, "John", 30, "Engineer", "Male", 1000L),
            User(2L, "Jane", 25, "Designer", "Female", 2000L)
        )
        usersFlow.value = users

        var result: List<User> = emptyList()
        useCase().collectLatest { result = it }

        assertEquals(2, result.size)
        assertEquals("John", result[0].name)
        assertEquals("Jane", result[1].name)
    }

    @Test
    fun `invoke should return empty flow when no users exist`(): Unit = runBlocking {
        usersFlow.value = emptyList()

        var result: List<User> = emptyList()
        useCase().collectLatest { result = it }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should return flow with single user`(): Unit = runBlocking {
        val users = listOf(
            User(1L, "Alice", 28, "Manager", "Female", 3000L)
        )
        usersFlow.value = users

        var result: List<User> = emptyList()
        useCase().collectLatest { result = it }

        assertEquals(1, result.size)
        assertEquals("Alice", result[0].name)
    }
}
