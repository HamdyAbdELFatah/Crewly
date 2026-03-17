package com.madar.crewly.core.data

data class User(
    val id: Long = 0,
    val name: String,
    val age: Int,
    val jobTitle: String,
    val gender: String,
    val createdAt: Long = System.currentTimeMillis()
)
