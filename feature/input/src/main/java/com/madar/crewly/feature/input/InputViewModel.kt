package com.madar.crewly.feature.input

import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.base.BaseViewModel
import com.madar.crewly.core.common.base.DispatcherProvider
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.common.validation.InputValidator
import com.madar.crewly.core.common.validation.ValidationResult
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.usecase.SaveUserUseCase
import com.madar.crewly.core.domain.usecase.UpdateUserUseCase
import com.madar.crewly.core.domain.usecase.GetUserByIdUseCase
import com.madar.crewly.feature.input.state.InputUiEvent
import com.madar.crewly.feature.input.state.InputUiState
import com.madar.crewly.feature.input.state.UserFormEvent
import kotlinx.coroutines.flow.firstOrNull

class InputViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    dispatchers: DispatcherProvider
) : BaseViewModel<InputUiState, InputUiEvent>(dispatchers) {

    override fun initialState() = InputUiState()

    override fun handleError(message: UiText) =
        updateState { copy(errorMessage = message) }

    fun resetState() {
        updateState { InputUiState() }
    }

    fun loadUser(userId: Long) {
        if (userId <= 0) return
        updateState { copy(userId = userId, isLoading = true) }

        launch {
            val user = getUserByIdUseCase(userId).firstOrNull()
            user?.let {
                updateState {
                    copy(
                        name = it.name,
                        age = it.age.toString(),
                        jobTitle = it.jobTitle,
                        gender = it.gender,
                        isLoading = false
                    )
                }
            } ?: run {
                updateState { copy(isLoading = false) }
                sendEvent(InputUiEvent.ShowSnackbar(UiText.StringResource(R.string.user_not_found)))
            }
        }
    }

    fun onEvent(event: UserFormEvent) = when (event) {
        is UserFormEvent.NameChanged -> updateState { copy(name = event.value) }
        is UserFormEvent.AgeChanged -> updateState { copy(age = event.value) }
        is UserFormEvent.JobChanged -> updateState { copy(jobTitle = event.value) }
        is UserFormEvent.GenderChanged -> updateState { copy(gender = event.value) }
        UserFormEvent.Submit -> submit()
        UserFormEvent.Cancel -> sendEvent(InputUiEvent.NavigateBack)
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

        launch {
            val user = User(
                id = state.userId ?: 0,
                name = state.name.trim(),
                age = state.age.toIntOrNull() ?: 0,
                jobTitle = state.jobTitle.trim(),
                gender = state.gender ?: ""
            )

            val result = if (state.isEditMode) {
                updateUserUseCase(user)
            } else {
                saveUserUseCase(user).map { }
            }

            result
                .onSuccess {
                    val message = if (state.isEditMode) {
                        UiText.StringResource(R.string.user_updated)
                    } else {
                        UiText.StringResource(R.string.user_saved)
                    }
                    sendEvent(InputUiEvent.ShowSnackbar(message))
                    sendEvent(
                        if (state.isEditMode) InputUiEvent.NavigateToUsersAndClear 
                        else InputUiEvent.NavigateToUsers
                    )
                }
                .onFailure { e ->
                    updateState { copy(errorMessage =
                        UiText.StringResource(R.string.error_save, arrayOf(e.localizedMessage ?: ""))) }
                }
        }
    }
}
