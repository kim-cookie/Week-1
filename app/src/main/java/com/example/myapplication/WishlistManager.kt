package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WishlistManager {
    private val gson = Gson()

    fun getWishlist(context: Context, userId: String): MutableList<Product> {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("wishlist_$userId", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveWishlist(context: Context, userId: String, list: List<Product>) {
        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val json = gson.toJson(list)
        prefs.edit().putString("wishlist_$userId", json).apply()
    }
}
