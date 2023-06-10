package com.pmsk.pemasokita.utils

import android.content.Context
import android.content.SharedPreferences
import com.pmsk.pemasokita.ui.fragment.ProfileFragment

object SharedPrefManager {
    private const val PREF_NAME = "MyPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}