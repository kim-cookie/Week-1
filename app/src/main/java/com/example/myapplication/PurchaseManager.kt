package com.example.myapplication

object PurchaseManager {
    private val purchaseList = mutableListOf<CartItem>()

    fun addAll(items: List<CartItem>) {
        // Stack처럼 위에 쌓이도록 맨 앞에 추가
        purchaseList.addAll(0, items)
    }

    fun getAll(): List<CartItem> = purchaseList
}
