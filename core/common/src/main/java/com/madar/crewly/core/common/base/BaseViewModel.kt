package com.madar.crewly.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.ui.UiText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, E : UiEvent>(
    protected val dispatchers: DispatcherProvider
) : ViewModel() {

    abstract fun initialState(): S

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<E>(replay = 0, extraBufferCapacity = 1)
    val uiEvent: SharedFlow<E> = _uiEvent.asSharedFlow()

    protected fun updateState(reducer: S.() -> S) {
        _uiState.value = _uiState.value.reducer()
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch { _uiEvent.emit(event) }
    }

    protected fun launch(
        onError: (UiText) -> Unit = { msg -> handleError(msg) },
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatchers.io) {
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onError(UiText.StringResource(
                    R.string.error_generic, arrayOf(e.localizedMessage ?: "")))
            }
        }
    }

    protected open fun handleError(message: UiText) {}

    override fun onCleared() {
        super.onCleared()
        _uiEvent.resetReplayCache()
    }
}
