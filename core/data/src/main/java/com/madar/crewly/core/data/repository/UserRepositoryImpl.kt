package com.madar.crewly.core.data.repository

import com.madar.crewly.core.common.mapper.Mapper
import com.madar.crewly.core.common.mapper.mapList
import com.madar.crewly.core.data.local.dao.UserDao
import com.madar.crewly.core.data.local.entity.UserEntity
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val entityToUserMapper: Mapper<UserEntity, User>,
    private val userToEntityMapper: Mapper<User, UserEntity>
) : UserRepository {

    override suspend fun saveUser(user: User): Result<Long> {
        return try {
            val entity = userToEntityMapper.map(user)
            val rowId = userDao.insert(entity)
            Result.success(rowId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entityToUserMapper.mapList(entities)
        }
    }

    override fun getUserCount(): Flow<Int> = userDao.getUserCount()

    override fun getUserById(userId: Long): Flow<User?> {
        return userDao.getUserById(userId).map { entity ->
            entity?.let { entityToUserMapper.map(it) }
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return try {
            val entity = userToEntityMapper.map(user)
            userDao.update(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: Long): Result<Unit> {
        return try {
            userDao.deleteById(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}