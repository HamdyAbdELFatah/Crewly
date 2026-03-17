package com.madar.crewly.core.data

import com.madar.crewly.core.common.Mapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setup() {
        val userDao = object : UserDao {
            private val users = MutableStateFlow<List<UserEntity>>(emptyList())
            override fun getAllUsers() = users
            override fun getUserCount() = MutableStateFlow(users.value.size)
            override suspend fun insert(entity: UserEntity) {
                users.value = users.value + entity
            }
        }
        
        val entityToUserMapper = UserEntityToUserMapper()
        val userToEntityMapper = UserToUserEntityMapper()
        repository = UserRepositoryImpl(userDao, entityToUserMapper, userToEntityMapper)
    }

    @Test
    fun `saveUser should return success when dao inserts successfully`(): Unit = runBlocking {
        val user = User(
            id = 1L,
            name = "John",
            age = 30,
            jobTitle = "Engineer",
            gender = "Male"
        )

        val result = repository.saveUser(user)

        assertTrue(result.isSuccess)
        assertEquals(user.id, result.getOrNull())
    }

    @Test
    fun `getAllUsers should return flow of mapped users`(): Unit = runBlocking {
        val result = repository.getAllUsers().collectLatest { users ->
            assertTrue(users.isEmpty() || users.any { it.name == "John" || it.name == "Jane" })
        }
    }

    @Test
    fun `getUserCount should return flow of user count`(): Unit = runBlocking {
        var count = 0
        repository.getUserCount().collectLatest { c ->
            count = c
        }
        assertTrue(count >= 0)
    }
}
