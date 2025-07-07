package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PurchaseManager {
    private val purchaseList = mutableListOf<CartItem>()
    private val gson = Gson()

    fun getPurchases(context: Context, userId: String): List<CartItem> {
        val prefs = getPrefs(context)
        val json = prefs.getString("purchase_$userId", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addPurchases(context: Context, userId: String, newItems: List<CartItem>) {
        val prefs = getPrefs(context)
        val current = getPurchases(context, userId).toMutableList()
        current.addAll(0, newItems) // 최근 구매가 위로
        prefs.edit().putString("purchase_$userId", gson.toJson(current)).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }


    fun addAll(items: List<CartItem>) {
        // Stack처럼 위에 쌓이도록 맨 앞에 추가
        purchaseList.addAll(0, items)
    }

    fun getAll(): List<CartItem> = purchaseList
}
