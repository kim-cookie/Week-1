package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.util.convertResIdToUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PurchaseManager {
    private val purchaseList = mutableListOf<CartItem>()
    private val gson = Gson()


    fun getPurchases(context: Context, userId: String): List<CartItem> {
        val prefs = getPrefs(context)
        val json = prefs.getString("purchase_$userId", null) ?: return emptyList()

        // 🧠 1. Map 형태로 파싱
        val rawListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val rawList: List<Map<String, Any>> = gson.fromJson(json, rawListType)

        // 🧠 2. 마이그레이션 수행
        val migratedList = rawList.map { raw ->
            val name = raw["name"] as? String ?: ""
            val price = raw["price"] as? String ?: ""
            val size = raw["size"] as? String ?: ""
            val quantity = (raw["quantity"] as? Double)?.toInt() ?: 1
            val isSelected = raw["isSelected"] as? Boolean ?: false
            val purchaseDate = raw["purchaseDate"] as? String
            val imageField = raw["image"]

            val imageUri = when (imageField) {
                is Double -> convertResIdToUri(context, imageField.toInt())
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

        // 🧠 3. 변환된 결과 다시 저장
        savePurchases(context, userId, migratedList)

        return migratedList
    }

    private fun savePurchases(context: Context, userId: String, items: List<CartItem>) {
        val prefs = getPrefs(context)
        prefs.edit().putString("purchase_$userId", gson.toJson(items)).apply()
    }

//    fun getPurchases(context: Context, userId: String): List<CartItem> {
//        val prefs = getPrefs(context)
//        val json = prefs.getString("purchase_$userId", null) ?: return emptyList()
//        val type = object : TypeToken<List<CartItem>>() {}.type
//        return gson.fromJson(json, type)
//    }

    fun addPurchases(context: Context, userId: String, newItems: List<CartItem>) {
        val current = getPurchases(context, userId).toMutableList()
        current.addAll(0, newItems) // 최근 구매가 위로
        savePurchases(context, userId, current)
    }

//    fun addPurchases(context: Context, userId: String, newItems: List<CartItem>) {
//        val prefs = getPrefs(context)
//        val current = getPurchases(context, userId).toMutableList()
//        current.addAll(0, newItems) // 최근 구매가 위로
//        prefs.edit().putString("purchase_$userId", gson.toJson(current)).apply()
//    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }


    fun addAll(items: List<CartItem>) {
        // Stack처럼 위에 쌓이도록 맨 앞에 추가
        purchaseList.addAll(0, items)
    }

    fun getAll(): List<CartItem> = purchaseList
}
