package com.madar.crewly.feature.input.state

import com.madar.crewly.core.common.base.UiEvent
import com.madar.crewly.core.common.ui.UiText

sealed class InputUiEvent : UiEvent {
    data object NavigateToUsers : InputUiEvent()
    data object NavigateToUsersAndClear : InputUiEvent()
    data object NavigateBack : InputUiEvent()
    data class ShowSnackbar(val message: UiText) : InputUiEvent()
}
