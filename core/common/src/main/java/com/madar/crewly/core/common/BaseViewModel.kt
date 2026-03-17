package com.madar.crewly.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface UiState
interface UiEvent

abstract class BaseViewModel<S : UiState, E : UiEvent>(
    protected val dispatchers: DispatcherProvider
) : ViewModel() {

    abstract fun initialState(): S

    private val _uiState by lazy { MutableStateFlow(initialState()) }
    val uiState: StateFlow<S> by lazy {
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState()
        )
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uiEvent = Channel<E>(Channel.BUFFERED)
    val uiEvent: Flow<E> = _uiEvent.receiveAsFlow()

    protected fun updateState(reducer: S.() -> S) {
        _uiState.value = _uiState.value.reducer()
    }

    protected fun sendEvent(event: E) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    protected fun launch(
        showLoading: Boolean = false,
        onError: (UiText) -> Unit = { msg -> handleError(msg) },
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatchers.io) {
            if (showLoading) _isLoading.value = true
            runCatching { block() }
                .onFailure { e ->
                    onError(UiText.StringResource(
                        R.string.error_generic, arrayOf(e.localizedMessage ?: "")))
                }
            if (showLoading) _isLoading.value = false
        }
    }

    protected fun launchWithLoading(
        block: suspend CoroutineScope.() -> Unit
    ) = launch(showLoading = true, block = block)

    protected open fun handleError(message: UiText) {}

    override fun onCleared() {
        super.onCleared()
        _uiEvent.close()
    }
}
