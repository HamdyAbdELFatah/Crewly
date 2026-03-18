package com.madar.crewly.core.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.madar.crewly.core.common.constants.AppConstants
import com.madar.crewly.core.data.local.dao.UserDao
import com.madar.crewly.core.data.local.entity.UserEntity

@androidx.room3.Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

fun createAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder<AppDatabase>(
        context,
        AppConstants.DATABASE_NAME
    ).build()
}