package com.madar.crewly.feature.input.state

import com.madar.crewly.core.common.base.UiState
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.common.validation.Field
import com.madar.crewly.core.common.validation.ValidationResult

data class InputUiState(
    val userId: Long? = null,
    val name: String = "",
    val age: String = "",
    val jobTitle: String = "",
    val gender: String? = null,
    val fieldErrors: Map<Field, UiText> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
) : UiState {
    val isEditMode: Boolean
        get() = userId != null && userId > 0

    val fieldsValid: Boolean
        get() = name.isNotBlank() && age.isNotBlank() && jobTitle.isNotBlank() && gender != null
}
