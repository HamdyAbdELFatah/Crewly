package com.madar.crewly.core.common

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Composable
fun String.isValidAge(): Boolean {
    val age = this.toIntOrNull() ?: return false
    return age in AppConstants.AGE_MIN..AppConstants.AGE_MAX
}

@Composable
fun String.isValidName(): Boolean {
    return this.isNotBlank() && this.length <= AppConstants.NAME_MAX_LENGTH
}

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object InputRoute : AppRoute

@Serializable
data object UsersRoute : AppRoute
