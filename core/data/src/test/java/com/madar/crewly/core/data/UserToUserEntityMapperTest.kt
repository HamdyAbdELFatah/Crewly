package com.madar.crewly.core.data

import com.madar.crewly.core.common.Mapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserToUserEntityMapperTest {

    private lateinit var mapper: Mapper<User, UserEntity>

    @Before
    fun setup() {
        mapper = UserToUserEntityMapper()
    }

    @Test
    fun `map should convert User to UserEntity correctly`() {
        val user = User(
            id = 1L,
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male",
            createdAt = 1000L
        )

        val result = mapper.map(user)

        assertEquals(1L, result.id)
        assertEquals("John Doe", result.name)
        assertEquals(30, result.age)
        assertEquals("Software Engineer", result.jobTitle)
        assertEquals("Male", result.gender)
        assertEquals(1000L, result.createdAt)
    }

    @Test
    fun `map should handle user with default values`() {
        val user = User(
            name = "Jane",
            age = 25,
            jobTitle = "Designer",
            gender = "Female"
        )

        val result = mapper.map(user)

        assertEquals(0L, result.id)
        assertEquals("Jane", result.name)
        assertEquals(25, result.age)
        assertEquals("Designer", result.jobTitle)
        assertEquals("Female", result.gender)
    }

    @Test
    fun `map should handle empty strings`() {
        val user = User(
            id = 5L,
            name = "",
            age = 0,
            jobTitle = "",
            gender = "",
            createdAt = 500L
        )

        val result = mapper.map(user)

        assertEquals(5L, result.id)
        assertEquals("", result.name)
        assertEquals(0, result.age)
        assertEquals("", result.jobTitle)
        assertEquals("", result.gender)
        assertEquals(500L, result.createdAt)
    }
}
