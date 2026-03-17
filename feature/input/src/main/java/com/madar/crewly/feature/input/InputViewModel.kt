package com.madar.crewly.feature.input

import com.madar.crewly.core.common.BaseViewModel
import com.madar.crewly.core.common.DispatcherProvider
import com.madar.crewly.core.common.Field
import com.madar.crewly.core.common.InputValidator
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.common.ValidationResult
import com.madar.crewly.core.common.mapList
import com.madar.crewly.core.data.User
import com.madar.crewly.core.domain.SaveUserUseCase

class InputViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    dispatchers: DispatcherProvider
) : BaseViewModel<InputUiState, InputUiEvent>(dispatchers) {

    override fun initialState() = InputUiState()

    override fun handleError(message: UiText) =
        updateState { copy(errorMessage = message) }

    fun onEvent(event: UserFormEvent) = when (event) {
        is UserFormEvent.NameChanged -> updateState { copy(name = event.value) }
        is UserFormEvent.AgeChanged -> updateState { copy(age = event.value) }
        is UserFormEvent.JobChanged -> updateState { copy(jobTitle = event.value) }
        is UserFormEvent.GenderChanged -> updateState { copy(gender = event.value) }
        UserFormEvent.Submit -> submit()
    }

    private fun submit() {
        val state = uiState.value
        val validationResults = InputValidator.validate(
            name = state.name,
            age = state.age,
            jobTitle = state.jobTitle,
            gender = state.gender
        )

        val fieldErrors = validationResults
            .filter { it.value is ValidationResult.Invalid }
            .mapValues { (_, result) -> (result as ValidationResult.Invalid).error }

        if (fieldErrors.isNotEmpty()) {
            updateState { copy(fieldErrors = fieldErrors) }
            return
        }

        updateState { copy(fieldErrors = emptyMap()) }

        launchWithLoading {
            val user = User(
                name = state.name.trim(),
                age = state.age.toIntOrNull() ?: 0,
                jobTitle = state.jobTitle.trim(),
                gender = state.gender ?: ""
            )

            saveUserUseCase(user)
                .onSuccess {
                    sendEvent(InputUiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.user_saved)))
                    sendEvent(InputUiEvent.NavigateToUsers)
                }
                .onFailure { e ->
                    updateState { copy(errorMessage =
                        UiText.StringResource(R.string.error_save, arrayOf(e.localizedMessage ?: ""))) }
                }
        }
    }
}
