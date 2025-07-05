package com.example.myapplication

object CartManager {
    val cartList = ArrayList<CartItem>()

    fun addItem(item: CartItem) {
        cartList.add(item)
    }

    fun getItems(): List<CartItem> {
        return cartList
    }

    fun clearCart() {
        cartList.clear()
    }
}