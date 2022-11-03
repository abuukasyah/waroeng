package com.nubogana.palem.model

import java.time.LocalDateTime

data class User(
    val username: String,
    val id: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: LocalDateTime? = null,
    val telephoneNumber: String? = null,
    val password: String? = null,
    val createdTime: LocalDateTime? = null
)
