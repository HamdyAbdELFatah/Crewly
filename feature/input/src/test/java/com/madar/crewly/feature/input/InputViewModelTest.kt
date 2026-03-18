package com.madar.crewly.feature.input

import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import com.madar.crewly.core.domain.usecase.GetUserByIdUseCase
import com.madar.crewly.core.domain.usecase.SaveUserUseCase
import com.madar.crewly.core.domain.usecase.UpdateUserUseCase
import com.madar.crewly.feature.input.state.UserFormEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InputViewModelTest {

    private lateinit var viewModel: InputViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakeRepository = object : UserRepository {
        override suspend fun saveUser(user: User): Result<Long> = Result.success(1L)
        override fun getAllUsers() = flowOf(emptyList<User>())
        override fun getUserCount() = MutableStateFlow(0)
        override fun getUserById(userId: Long) = flowOf<User?>(
            User(userId, "Existing", 30, "Engineer", "Male")
        )
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

        viewModel = InputViewModel(
            saveUserUseCase = SaveUserUseCase(fakeRepository),
            updateUserUseCase = UpdateUserUseCase(fakeRepository),
            getUserByIdUseCase = GetUserByIdUseCase(fakeRepository),
            dispatchers = dispatchers
        )
    }

    @Test
    fun `initial state should have empty fields`() {
        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.age)
        assertEquals("", state.jobTitle)
        assertEquals(null, state.gender)
        assertTrue(state.fieldErrors.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun `onEvent NameChanged should update name field`() {
        viewModel.onEvent(UserFormEvent.NameChanged("John"))
        assertEquals("John", viewModel.uiState.value.name)
    }

    @Test
    fun `onEvent AgeChanged should update age field`() {
        viewModel.onEvent(UserFormEvent.AgeChanged("25"))
        assertEquals("25", viewModel.uiState.value.age)
    }

    @Test
    fun `onEvent JobChanged should update jobTitle field`() {
        viewModel.onEvent(UserFormEvent.JobChanged("Developer"))
        assertEquals("Developer", viewModel.uiState.value.jobTitle)
    }

    @Test
    fun `onEvent Submit with empty fields should show validation errors`() {
        viewModel.onEvent(UserFormEvent.Submit)
        assertTrue(viewModel.uiState.value.fieldErrors.isNotEmpty())
    }

    @Test
    fun `resetState should clear all fields`() {
        viewModel.onEvent(UserFormEvent.NameChanged("John"))
        viewModel.resetState()
        assertEquals("", viewModel.uiState.value.name)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadUser with invalid id should not update state`() {
        viewModel.loadUser(0L)
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
