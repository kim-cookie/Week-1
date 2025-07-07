package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    val cartList = ArrayList<CartItem>()
    private val gson = Gson()

    fun getCart(context: Context, userId: String): List<CartItem> {
        val prefs = getPrefs(context)
        val json = prefs.getString("cart_$userId", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveCart(context: Context, userId: String, items: List<CartItem>) {
        val prefs = getPrefs(context)
        val json = gson.toJson(items)
        prefs.edit().putString("cart_$userId", json).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    fun addItem(item: CartItem) {
        cartList.add(item)
    }

    fun getItems(): List<CartItem> {
        return cartList
    }

    fun removeItem(item: CartItem) {
        cartList.remove(item)
    }

    fun removeAll(items: List<CartItem>) {
        cartList.removeAll(items)
    }

    fun clearCart() {
        cartList.clear()
    }
}