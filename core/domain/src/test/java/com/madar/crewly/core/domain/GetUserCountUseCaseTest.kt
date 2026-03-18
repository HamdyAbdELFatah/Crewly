package com.madar.crewly.core.domain

import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.usecase.GetUserCountUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUserCountUseCaseTest {

    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var useCase: GetUserCountUseCase

    @Before
    fun setup() {
        fakeRepository = FakeUserRepository()
        useCase = GetUserCountUseCase(fakeRepository)
    }

    @Test
    fun `invoke should return flow of user count`(): Unit = runBlocking {
        fakeRepository.addUser(User(1L, "John", 30, "Engineer", "Male"))
        fakeRepository.addUser(User(2L, "Jane", 25, "Designer", "Female"))

        var count = 0
        useCase().collectLatest { count = it }

        assertEquals(2, count)
    }

    @Test
    fun `invoke should return zero when no users`(): Unit = runBlocking {
        var count = -1
        useCase().collectLatest { count = it }

        assertEquals(0, count)
    }

    @Test
    fun `invoke should reflect count after user deletion`(): Unit = runBlocking {
        fakeRepository.addUser(User(1L, "John", 30, "Engineer", "Male"))
        fakeRepository.addUser(User(2L, "Jane", 25, "Designer", "Female"))

        fakeRepository.deleteUser(1L)

        var count = 0
        useCase().collectLatest { count = it }

        assertEquals(1, count)
    }
}
