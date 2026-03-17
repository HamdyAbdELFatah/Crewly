package com.madar.crewly.core.common

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: UiText) : ValidationResult()

    val isValid: Boolean get() = this is Valid
}
