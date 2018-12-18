package com.example.ketrio.androidapp.data.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var fullName: String? = "",
    var phoneNumber: String? = "",
    var email: String? = "",
    var customPhoto: Boolean? = false
)