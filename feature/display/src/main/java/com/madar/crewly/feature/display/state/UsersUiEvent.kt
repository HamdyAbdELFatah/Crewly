package com.madar.crewly.feature.display.state

import com.madar.crewly.core.common.base.UiEvent
import com.madar.crewly.core.common.ui.UiText

sealed class UsersUiEvent : UiEvent {
    data class NavigateToEdit(val userId: Long) : UsersUiEvent()
    data class ShowSnackbar(val message: UiText) : UsersUiEvent()
}
