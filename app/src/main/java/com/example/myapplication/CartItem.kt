package com.example.myapplication

import java.io.Serializable

data class CartItem (
    var name: String,
    var price: String,
    var image: Int,
    var size: String,
    var quantity: Int,
    var isSelected: Boolean = false,
    var purchaseDate: String? = null
) : Serializable