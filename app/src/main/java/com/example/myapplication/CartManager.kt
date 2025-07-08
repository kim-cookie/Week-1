package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.util.convertResIdToUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    val cartList = ArrayList<CartItem>()
    private val gson = Gson()

    fun getCart(context: Context, userId: String): List<CartItem> {
        val prefs = getPrefs(context)
        val json = prefs.getString("cart_$userId", null) ?: return emptyList()

        val rawListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val rawList: List<Map<String, Any>> = gson.fromJson(json, rawListType)

        val migratedList = rawList.map { raw ->
            val name = raw["name"] as? String ?: ""
            val price = raw["price"] as? String ?: ""
            val size = raw["size"] as? String ?: ""
            val quantity = (raw["quantity"] as? Double)?.toInt() ?: 1
            val isSelected = raw["isSelected"] as? Boolean ?: false
            val purchaseDate = raw["purchaseDate"] as? String

            val imageField = raw["image"]
            val imageUri = when (imageField) {
                is Double -> convertResIdToUri(context, imageField.toInt()) // 예전 Int → URI 변환
                is String -> imageField
                else -> ""
            }

            CartItem(
                name = name,
                price = price,
                image = imageUri,
                size = size,
                quantity = quantity,
                isSelected = isSelected,
                purchaseDate = purchaseDate
            )
        }

        // ⏩ 변환 후 다시 저장 (한 번만 실행됨)
        saveCart(context, userId, migratedList)

        return migratedList
    }


//    fun getCart(context: Context, userId: String): List<CartItem> {
//        val prefs = getPrefs(context)
//        val json = prefs.getString("cart_$userId", null) ?: return emptyList()
//        val type = object : TypeToken<List<CartItem>>() {}.type
//        return gson.fromJson(json, type)
//    }

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