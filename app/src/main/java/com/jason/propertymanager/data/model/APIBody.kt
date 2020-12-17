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

data class UploadPropertyBody(
    val __v: Int,
    val address: String,
    val city: String,
    val country: String,
    val image: String,
    val latitude: String,
    val longitude: String,
    val mortageInfo: Boolean,
    val propertyStatus: Boolean,
    val purchasePrice: String,
    val state: String,
    val userId: String,
    val userType: String
)

data class UpdateUserBody(
    val __v: Int,
    val createdAt: String,
    val email: String,
    val name: String,
    val password: String,
    val type: String
)

