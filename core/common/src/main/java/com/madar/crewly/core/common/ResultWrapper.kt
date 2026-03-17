package com.madar.crewly.core.common

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: UiText, val throwable: Throwable? = null) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun <R> map(transform: (T) -> R): ResultWrapper<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }

    inline fun onSuccess(action: (T) -> Unit): ResultWrapper<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (UiText, Throwable?) -> Unit): ResultWrapper<T> {
        if (this is Error) action(message, throwable)
        return this
    }

    inline fun onLoading(action: () -> Unit): ResultWrapper<T> {
        if (this is Loading) action()
        return this
    }
}
