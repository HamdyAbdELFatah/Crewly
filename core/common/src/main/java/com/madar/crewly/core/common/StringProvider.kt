package com.madar.crewly.core.common

import android.content.Context

interface StringProvider {
    fun getString(resId: Int, vararg args: Any): String
}

class AndroidStringProvider(private val context: Context) : StringProvider {
    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}
