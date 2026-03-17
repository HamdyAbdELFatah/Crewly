package com.madar.crewly.feature.input

import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.domain.SaveUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InputViewModelTest {

    private lateinit var viewModel: InputViewModel

    @Before
    fun setup() {
        val testDispatcher = StandardTestDispatcher()
        val dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = testDispatcher
            override val main: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
            override val unconfined: CoroutineDispatcher = testDispatcher
        }

        val saveUserUseCase = SaveUserUseCase(object : com.madar.crewly.core.data.UserRepository {
            override suspend fun saveUser(user: com.madar.crewly.core.data.User): Result<Long> = Result.success(1L)
            override fun getAllUsers() = kotlinx.coroutines.flow.flowOf(emptyList())
            override fun getUserCount() = kotlinx.coroutines.flow.MutableStateFlow(0)
        })

        viewModel = InputViewModel(saveUserUseCase, dispatchers)
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

        val state = viewModel.uiState.value
        assertEquals("John", state.name)
    }

    @Test
    fun `onEvent Submit with empty fields should show validation errors`() {
        viewModel.onEvent(UserFormEvent.Submit)

        val state = viewModel.uiState.value
        assertTrue(state.fieldErrors.isNotEmpty())
    }
}
