package com.example.coffeeshop.Domain

data class UserModel(
    var userId: String = "",
    var phoneNumber: String = "",
    var fullName: String = "",
    var email: String = "",
    var password: String = "",
    var createdAt: Long = System.currentTimeMillis()
)

