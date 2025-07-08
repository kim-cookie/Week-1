package com.example.myapplication

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ImageStorage {
    private const val PREFS_NAME = "ImageMapStorage"
    private val gson = Gson()

    fun saveUriList(context: Context, userId: String, key: String, list: List<Uri>) {
        val json = gson.toJson(list.map { it.toString() })
        val fullKey = "${userId}_$key"
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(fullKey, json)
            .apply()
    }

    fun loadUriList(context: Context, userId: String, key: String): SnapshotStateList<Uri> {
        val fullKey = "${userId}_$key"
        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(fullKey, null) ?: return SnapshotStateList()
        val type = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(json, type)
        return stringList.map { Uri.parse(it) }.toMutableStateList()
    }

    fun loadAllUris(context: Context, userId: String): Map<String, SnapshotStateList<Uri>> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.all.mapNotNull { (fullKey, value) ->
            if (value is String && fullKey.startsWith("${userId}_")) {
                val key = fullKey.removePrefix("${userId}_")
                val type = object : TypeToken<List<String>>() {}.type
                val list: List<String> = gson.fromJson(value, type)
                key to list.map { Uri.parse(it) }.toMutableStateList()
            } else null
        }.toMap()
    }
}

