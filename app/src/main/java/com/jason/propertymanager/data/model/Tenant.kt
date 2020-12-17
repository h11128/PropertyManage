package com.jason.propertymanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenant_table")
data class Tenant(
    val __v: Int,
    @PrimaryKey val _id: String,
    val email: String,
    val mobile: String,
    val name: String
)