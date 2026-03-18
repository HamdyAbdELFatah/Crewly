package com.madar.crewly.core.common.ui

import android.content.Context
import kotlinx.serialization.Serializable

@Serializable
sealed class UiText {
    @Serializable
    data class DynamicString(val text: String) : UiText()

    @Serializable
    data class StringResource(
        val resId: Int,
        val args: Array<String> = emptyArray()
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId, *args)
        }
    }
}
