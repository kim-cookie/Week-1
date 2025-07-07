package com.example.myapplication

object CartManager {
    val cartList = ArrayList<CartItem>()

    fun addItem(item: CartItem) {
        cartList.add(item)
    }

    fun getItems(): List<CartItem> {
        return cartList
    }

    fun removeItem(item: CartItem) {
        cartList.remove(item)
    }

    fun clearCart() {
        cartList.clear()
    }
}