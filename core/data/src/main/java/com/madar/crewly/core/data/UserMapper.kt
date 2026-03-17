package com.madar.crewly.core.data

import com.madar.crewly.core.common.Mapper

object UserMapper : Mapper<UserEntity, User> {
    override fun map(input: UserEntity): User = User(
        id = input.id,
        name = input.name,
        age = input.age,
        jobTitle = input.jobTitle,
        gender = input.gender,
        createdAt = input.createdAt
    )
}

object UserToEntityMapper : Mapper<User, UserEntity> {
    override fun map(input: User): UserEntity = UserEntity(
        id = input.id,
        name = input.name,
        age = input.age,
        jobTitle = input.jobTitle,
        gender = input.gender,
        createdAt = input.createdAt
    )
}
