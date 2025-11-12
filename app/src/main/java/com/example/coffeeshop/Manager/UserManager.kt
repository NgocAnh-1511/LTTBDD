package com.example.coffeeshop.Manager

import android.content.Context
import android.content.SharedPreferences
import com.example.coffeeshop.Domain.UserModel
import com.google.gson.Gson

class UserManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val userKey = "current_user"
    private val isLoggedInKey = "is_logged_in"

    fun saveUser(user: UserModel) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit()
            .putString(userKey, userJson)
            .putBoolean(isLoggedInKey, true)
            .apply()
    }

    fun getCurrentUser(): UserModel? {
        val userJson = sharedPreferences.getString(userKey, null)
        return if (userJson != null) {
            gson.fromJson(userJson, UserModel::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(isLoggedInKey, false)
    }

    fun logout() {
        sharedPreferences.edit()
            .remove(userKey)
            .putBoolean(isLoggedInKey, false)
            .apply()
    }

    fun getUserId(): String? {
        return getCurrentUser()?.userId
    }

    fun getPhoneNumber(): String? {
        return getCurrentUser()?.phoneNumber
    }

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString("auth_token", token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove("auth_token")
            .apply()
    }
}

