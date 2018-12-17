package com.example.ketrio.androidapp.utils

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