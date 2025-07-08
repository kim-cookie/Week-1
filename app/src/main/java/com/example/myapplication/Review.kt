package com.example.myapplication

import androidx.annotation.Keep

@Keep
data class Review(
    val itemId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = 0L
)