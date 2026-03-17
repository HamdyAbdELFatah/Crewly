package com.madar.crewly.feature.display

import com.madar.crewly.core.common.ContentState
import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository
import com.madar.crewly.core.domain.GetAllUsersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DisplayViewModelTest {

    private lateinit var viewModel: DisplayViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        val dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = testDispatcher
            override val main: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
            override val unconfined: CoroutineDispatcher = testDispatcher
        }

        val getAllUsersUseCase = GetAllUsersUseCase(object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = flowOf(emptyList())
            override fun getUserCount() = MutableStateFlow(0)
        })

        val userRepository = object : UserRepository {
            override suspend fun saveUser(user: User): Result<Long> = Result.success(0L)
            override fun getAllUsers() = flowOf(emptyList())
            override fun getUserCount() = MutableStateFlow(0)
        }

        viewModel = DisplayViewModel(getAllUsersUseCase, userRepository, dispatchers)
    }

    @Test
    fun `initial state should have loading content state`() {
        val state = viewModel.uiState.value
        assertEquals(ContentState.Loading, state.contentState)
    }
}
