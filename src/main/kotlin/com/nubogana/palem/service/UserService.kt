package com.nubogana.palem.service

import com.nubogana.palem.model.User

interface UserService {

    fun findUser(userId: String): User

    fun createUser(user: User): Boolean

    fun updateUser(user: User): Boolean

    fun getUserToken(user: User): String
}
