package com.formate.formate.utils

import android.content.Context

class PrefHelper(private val context: Context) {

    companion object {
        private const val PREF_KEY = "example"
    }

    fun setStringData(key: String, data: String?) {
        val pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        pref.edit().putString(key,data).apply()
    }

    fun getStringData(key: String): String? {
        val pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return pref.getString(key, null)
    }

}