package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object UserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_LIST = "user_list"
    private const val KEY_LOGGED_IN_USER = "logged_in_user"

    private val gson = Gson()

    fun signUp(context: Context, newUser: UserAccount): Boolean {
        val userList = getAllUsers(context).toMutableList()
        // 이미 존재하는 ID면 실패
        if (userList.any { it.id == newUser.id }) return false

        newUser.signUpDate = getToday()
        newUser.lastLoginDate = getToday()
        userList.add(newUser)
        saveUserList(context, userList)
        return true
    }

    fun login(context: Context, id: String, password: String): Boolean {
        val userList = getAllUsers(context).toMutableList()
        val index = userList.indexOfFirst { it.id == id && it.password == password }

        if (index == -1) return false

        userList[index].lastLoginDate = getToday()
        saveUserList(context, userList)

        getPrefs(context).edit().putString(KEY_LOGGED_IN_USER, gson.toJson(userList[index])).apply()
        return true
    }

    fun logout(context: Context) {
        getPrefs(context).edit().remove(KEY_LOGGED_IN_USER).apply()
    }

    fun getLoggedInUser(context: Context): UserAccount? {
        val json = getPrefs(context).getString(KEY_LOGGED_IN_USER, null) ?: return null
        return gson.fromJson(json, UserAccount::class.java)
    }

    fun getAllUsers(context: Context): List<UserAccount> {
        val json = getPrefs(context).getString(KEY_USER_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<UserAccount>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveUserList(context: Context, users: List<UserAccount>) {
        val json = gson.toJson(users)
        getPrefs(context).edit().putString(KEY_USER_LIST, json).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getToday(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")  // 한국 시간대 명시
        return sdf.format(Date())
    }
}
