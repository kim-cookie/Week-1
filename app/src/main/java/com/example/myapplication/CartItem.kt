package com.example.myapplication

import java.io.Serializable

data class CartItem (
    var name: String,
    var price: String,
    var image: String,
    var size: String,
    var quantity: Int,
    var isSelected: Boolean = false,
    var purchaseDate: String? = null
) : Serializable