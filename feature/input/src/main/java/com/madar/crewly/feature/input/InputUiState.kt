package com.madar.crewly.feature.input

import com.madar.crewly.core.common.Field
import com.madar.crewly.core.common.UiEvent
import com.madar.crewly.core.common.UiState
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.common.ValidationResult

data class InputUiState(
    val name: String = "",
    val age: String = "",
    val jobTitle: String = "",
    val gender: String? = null,
    val fieldErrors: Map<Field, UiText> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
) : UiState {
    val fieldsValid: Boolean
        get() = name.isNotBlank() && age.isNotBlank() && jobTitle.isNotBlank() && gender != null
}

sealed class InputUiEvent : UiEvent {
    data object NavigateToUsers : InputUiEvent()
    data class ShowSnackbar(val message: UiText) : InputUiEvent()
}

sealed class UserFormEvent {
    data class NameChanged(val value: String) : UserFormEvent()
    data class AgeChanged(val value: String) : UserFormEvent()
    data class JobChanged(val value: String) : UserFormEvent()
    data class GenderChanged(val value: String) : UserFormEvent()
    data object Submit : UserFormEvent()
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    companion object {
        val entries = listOf(MALE, FEMALE, OTHER)
    }
}
