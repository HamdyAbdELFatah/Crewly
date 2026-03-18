package com.madar.crewly.core.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data class InputRoute(val userId: Long = 0) : AppRoute

@Serializable
data object UsersRoute : AppRoute
