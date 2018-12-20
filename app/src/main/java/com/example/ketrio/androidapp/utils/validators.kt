package com.example.ketrio.androidapp.utils

import java.util.regex.Pattern

fun isValidEmail(target: CharSequence?): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isValidMobile(target: CharSequence?): Boolean {
    return android.util.Patterns.PHONE.matcher(target).matches()
}

fun isValidFullname(target: CharSequence?): Boolean {
    target?.run {
        return this.split(" ").filter { s -> s.length > 0 }.size > 1
    }
    return false
}

fun isValidPassword(target: CharSequence?): Boolean {
    val PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
    return PASSWORD_PATTERN.matcher(target).matches()
}