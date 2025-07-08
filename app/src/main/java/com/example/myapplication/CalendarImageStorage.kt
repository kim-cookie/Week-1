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

    fun saveUriList(context: Context, key: String, list: List<Uri>) {
        val json = gson.toJson(list.map { it.toString() })
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, json)
            .apply()
    }

    fun loadUriList(context: Context, key: String): SnapshotStateList<Uri> {
        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(key, null) ?: return SnapshotStateList()
        val type = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(json, type)
        return stringList.map { Uri.parse(it) }.toMutableStateList()
    }

    fun loadAllUris(context: Context): Map<String, SnapshotStateList<Uri>> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.all.mapNotNull { (key, value) ->
            if (value is String) {
                val type = object : TypeToken<List<String>>() {}.type
                val list: List<String> = gson.fromJson(value, type)
                key to list.map { Uri.parse(it) }.toMutableStateList()
            } else null
        }.toMap()
    }
}
