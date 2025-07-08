package com.example.myapplication

data class Product (
    val name: String,
    val price: String,
    val image: String,
    var isLiked: Boolean = false
    )