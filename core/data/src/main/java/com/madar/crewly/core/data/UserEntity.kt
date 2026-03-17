package com.madar.crewly.core.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val age: Int,
    val jobTitle: String,
    val gender: String,
    val createdAt: Long = System.currentTimeMillis()
)
