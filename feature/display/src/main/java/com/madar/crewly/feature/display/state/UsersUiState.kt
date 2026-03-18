package com.madar.crewly.feature.display.state

import com.madar.crewly.core.common.base.UiState
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.domain.model.User

data class UsersUiState(
    val contentState: UsersContentState = UsersContentState.Loading,
    val userCount: Int = 0,
    val isRefreshing: Boolean = false
) : UiState

sealed class UsersContentState {
    data object Loading : UsersContentState()
    data object Empty : UsersContentState()
    data class Error(val message: UiText) : UsersContentState()
    data class Success(val users: List<User>) : UsersContentState()
}
