package com.madar.crewly.feature.input.state

sealed class UserFormEvent {
    data class NameChanged(val value: String) : UserFormEvent()
    data class AgeChanged(val value: String) : UserFormEvent()
    data class JobChanged(val value: String) : UserFormEvent()
    data class GenderChanged(val value: String) : UserFormEvent()
    data object Submit : UserFormEvent()
    data object Cancel : UserFormEvent()
}
