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

    /** 찜 여부 확인 */
    fun isLiked(context: Context, userId: String, product: Product): Boolean {
        val wishlist = getWishlist(context, userId)
        return wishlist.any { it.name == product.name && it.price == product.price }
    }

    /** 찜 상태 toggle (있으면 제거, 없으면 추가) */
    fun toggleWishlist(context: Context, userId: String, product: Product) {
        val wishlist = getWishlist(context, userId)
        val exists = wishlist.any { it.name == product.name && it.price == product.price }

        if (exists) {
            wishlist.removeAll { it.name == product.name && it.price == product.price }
        } else {
            wishlist.add(product)
        }

        saveWishlist(context, userId, wishlist)
    }
}
