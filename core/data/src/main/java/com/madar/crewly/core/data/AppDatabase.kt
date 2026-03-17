package com.madar.crewly.core.data

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.madar.crewly.core.common.AppConstants

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
