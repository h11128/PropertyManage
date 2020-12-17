package com.jason.propertymanager.data.model

data class RegisterBody(
    val email: String,
    val name: String,
    val password: String,
    val type: String
)

data class LoginBody(
    val email: String,
    val password: String
)

data class UpdateUserBody(
    val __v: Int,
    val createdAt: String,
    val email: String,
    val name: String,
    val password: String,
    val type: String
)

