package com.madar.crewly.core.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserEntity)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}
