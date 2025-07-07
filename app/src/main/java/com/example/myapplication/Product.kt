package com.example.myapplication

data class Product (
    val name: String,
    val price: String,
    val image: Int,
    var isLiked: Boolean = false
    )