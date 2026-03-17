package com.madar.crewly.core.data

import com.madar.crewly.core.common.Mapper
import com.madar.crewly.core.common.mapList
import com.madar.crewly.core.domain.UserRepository
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
}
