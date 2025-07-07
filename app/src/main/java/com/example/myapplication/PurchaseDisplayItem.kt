package com.example.myapplication

sealed class PurchaseDisplayItem {
    data class Header(val date: String) : PurchaseDisplayItem()
    data class Product(val item: CartItem) : PurchaseDisplayItem()
}
