package com.madar.crewly.feature.display

import com.madar.crewly.core.common.BaseViewModel
import com.madar.crewly.core.common.ContentState
import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.common.DisplayUiEvent
import com.madar.crewly.core.common.DisplayUiState
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.domain.UserRepository
import com.madar.crewly.core.domain.GetAllUsersUseCase
import kotlinx.coroutines.flow.combine

class DisplayViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val userRepository: UserRepository,
    dispatchers: DispatcherProvider
) : BaseViewModel<DisplayUiState, DisplayUiEvent>(dispatchers) {

    init {
        observeUsers()
    }

    override fun initialState() = DisplayUiState()

    private fun observeUsers() {
        launch {
            combine(
                getAllUsersUseCase(),
                userRepository.getUserCount()
            ) { users, count ->
                Pair(users, count)
            }.collect { (users, count) ->
                updateState {
                    copy(
                        userCount = count,
                        isRefreshing = false,
                        contentState = when {
                            users.isEmpty() -> ContentState.Empty
                            else -> ContentState.Success(users)
                        }
                    )
                }
            }
        }
    }

    fun refresh() {
        updateState { copy(isRefreshing = true) }
    }

    override fun handleError(message: UiText) {
        updateState { copy(contentState = ContentState.Error(message)) }
    }
}
