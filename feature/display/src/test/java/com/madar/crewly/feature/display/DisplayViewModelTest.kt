package com.madar.crewly.feature.display

import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.DeleteUserUseCase
import com.madar.crewly.core.domain.usecase.GetAllUsersUseCase
import com.madar.crewly.core.domain.usecase.GetUserCountUseCase
import com.madar.crewly.feature.display.state.UsersContentState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DisplayViewModelTest {

    private lateinit var viewModel: DisplayViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val usersFlow = MutableStateFlow<List<User>>(emptyList())
    private val countFlow = MutableStateFlow(0)

    private val fakeRepository = object : UserRepository {
        override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
        override fun getAllUsers() = usersFlow
        override fun getUserCount() = countFlow
        override fun getUserById(userId: Long) = flowOf<User?>(null)
        override suspend fun updateUser(user: User): Result<Unit> = Result.success(Unit)
        override suspend fun deleteUser(userId: Long): Result<Unit> = Result.success(Unit)
    }

    @Before
    fun setup() {
        val dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = testDispatcher
            override val main: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
            override val unconfined: CoroutineDispatcher = testDispatcher
        }

        viewModel = DisplayViewModel(
            getAllUsersUseCase = GetAllUsersUseCase(fakeRepository),
            deleteUserUseCase = DeleteUserUseCase(fakeRepository),
            getUserCountUseCase = GetUserCountUseCase(fakeRepository),
            dispatchers = dispatchers
        )
    }

    @Test
    fun `initial state should have loading content state`() {
        assertEquals(UsersContentState.Loading, viewModel.uiState.value.contentState)
    }

    @Test
    fun `when users list is empty, state should be Empty`() = runTest(testDispatcher) {
        usersFlow.value = emptyList()
        advanceUntilIdle()
        assertEquals(UsersContentState.Empty, viewModel.uiState.value.contentState)
    }

    @Test
    fun `when users list has items, state should be Success with users`() = runTest(testDispatcher) {
        val users = listOf(User(1L, "Alice", 28, "Manager", "Female", 1000L))
        usersFlow.value = users
        advanceUntilIdle()
        val content = viewModel.uiState.value.contentState
        assertTrue(content is UsersContentState.Success)
        assertEquals(users, (content as UsersContentState.Success).users)
    }

    @Test
    fun `userCount should be reflected in state`() = runTest(testDispatcher) {
        countFlow.value = 5
        usersFlow.value = List(5) { User(it.toLong(), "User $it", 25, "Dev", "Male") }
        advanceUntilIdle()
        assertEquals(5, viewModel.uiState.value.userCount)
    }
}
