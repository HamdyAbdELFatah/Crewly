package com.madar.crewly.feature.display

import androidx.lifecycle.viewModelScope
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.base.BaseViewModel
import com.madar.crewly.core.common.base.DispatcherProvider
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.domain.usecase.DeleteUserUseCase
import com.madar.crewly.core.domain.usecase.GetAllUsersUseCase
import com.madar.crewly.core.domain.usecase.GetUserCountUseCase
import com.madar.crewly.feature.display.state.UsersContentState
import com.madar.crewly.feature.display.state.UsersUiEvent
import com.madar.crewly.feature.display.state.UsersUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DisplayViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserCountUseCase: GetUserCountUseCase,
    dispatchers: DispatcherProvider
) : BaseViewModel<UsersUiState, UsersUiEvent>(dispatchers) {

    private var observeJob: Job? = null

    init {
        observeUsers()
    }

    override fun initialState() = UsersUiState()

    private fun observeUsers() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            combine(
                getAllUsersUseCase(),
                getUserCountUseCase()
            ) { users, count ->
                Pair(users, count)
            }.catch { e ->
                handleError(UiText.StringResource(
                    R.string.error_generic, arrayOf(e.localizedMessage ?: "")))
            }.collect { (users, count) ->
                updateState {
                    copy(
                        userCount = count,
                        isRefreshing = false,
                        contentState = when {
                            users.isEmpty() -> UsersContentState.Empty
                            else -> UsersContentState.Success(users)
                        }
                    )
                }
            }
        }
    }

    fun refresh() {
        updateState { copy(isRefreshing = true) }
        observeUsers()
    }

    fun deleteUser(userId: Long) {
        launch {
            deleteUserUseCase(userId)
                .onSuccess {
                    sendEvent(UsersUiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.user_deleted)))
                }
                .onFailure { e ->
                    sendEvent(UsersUiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_delete, arrayOf(e.localizedMessage ?: ""))))
                }
        }
    }

    override fun handleError(message: UiText) {
        updateState { copy(contentState = UsersContentState.Error(message)) }
    }
}
