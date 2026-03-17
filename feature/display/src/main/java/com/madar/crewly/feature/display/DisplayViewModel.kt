package com.madar.crewly.feature.display

import androidx.lifecycle.viewModelScope
import com.madar.crewly.core.common.BaseViewModel
import com.madar.crewly.core.common.ContentState
import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.common.DisplayUiEvent
import com.madar.crewly.core.common.DisplayUiState
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.data.User
import com.madar.crewly.core.data.UserRepository
import com.madar.crewly.core.domain.GetAllUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DisplayViewModel(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val userRepository: UserRepository,
    dispatchers: DispatcherProvider
) : BaseViewModel<DisplayUiState, DisplayUiEvent>(dispatchers) {

    val users: StateFlow<List<User>> = getAllUsersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        updateState {
            copy(
                contentState = ContentState.Loading
            )
        }

        viewModelScope.launch(dispatchers.io) {
            combine(
                getAllUsersUseCase(),
                userRepository.getUserCount()
            ) { users, count ->
                Pair(users, count)
            }.collect { (users, count) ->
                updateState {
                    copy(
                        userCount = count,
                        contentState = when {
                            users.isEmpty() -> ContentState.Empty
                            else -> ContentState.Success(users)
                        }
                    )
                }
            }
        }
    }

    override fun initialState() = DisplayUiState()

    fun load() {
        updateState { copy(contentState = ContentState.Loading) }
    }

    override fun handleError(message: UiText) {
        updateState { copy(contentState = ContentState.Error(message)) }
    }
}
