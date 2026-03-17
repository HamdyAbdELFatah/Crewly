package com.madar.crewly.core.data

import com.madar.crewly.core.common.Mapper

class UserEntityToUserMapper : Mapper<UserEntity, User> {
    override fun map(input: UserEntity): User {
        return User(
            id = input.id,
            name = input.name,
            age = input.age,
            jobTitle = input.jobTitle,
            gender = input.gender,
            createdAt = input.createdAt
        )
    }
}

class UserToUserEntityMapper : Mapper<User, UserEntity> {
    override fun map(input: User): UserEntity {
        return UserEntity(
            id = input.id,
            name = input.name,
            age = input.age,
            jobTitle = input.jobTitle,
            gender = input.gender,
            createdAt = input.createdAt
        )
    }
}
