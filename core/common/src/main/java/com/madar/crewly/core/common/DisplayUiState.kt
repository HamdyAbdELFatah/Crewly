package com.madar.crewly.core.common

data class DisplayUiState(
    val contentState: ContentState = ContentState.Loading,
    val userCount: Int = 0
) : UiState

sealed class ContentState {
    data object Loading : ContentState()
    data object Empty : ContentState()
    data class Error(val message: UiText) : ContentState()
    data class Success(val users: List<Any>) : ContentState()
}

sealed class DisplayUiEvent : UiEvent
