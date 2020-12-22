package com.jason.propertymanager.data.model

import com.jason.propertymanager.other.default_string

data class RegisterBody(val email: String,
                        val name: String,
                        val password: String,
                        val type: String,
                        val landlordEmail: String = email)

data class LoginBody(val email: String, val password: String)

data class UploadPropertyBody(val address: String = default_string,
                              val city: String = default_string,
                              val country: String = default_string,
                              val image: String = default_string,
                              val latitude: String = default_string,
                              val longitude: String = default_string,
                              val mortageInfo: Boolean,
                              val propertyStatus: Boolean,
                              val purchasePrice: String = default_string,
                              val state: String,
                              val userId: String,
                              val userType: String)

data class UpdateUserBody(val __v: Int,
                          val createdAt: String,
                          val email: String,
                          val name: String,
                          val password: String,
                          val type: String)

