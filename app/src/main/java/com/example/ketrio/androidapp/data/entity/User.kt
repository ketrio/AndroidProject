package com.example.ketrio.androidapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "full_name") var fullName: String?,
    @ColumnInfo(name = "phone_number") var phoneNumber: String?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "profile_image") var profileImage: ByteArray
)