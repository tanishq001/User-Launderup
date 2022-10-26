package com.example.launderup.data.models

data class UserLogin(
    val account_status: String,
    val token: String,
    val uid: String,
    val verified_at: Any
)