package ru.plumsoftware.photoeditor.settings

import android.content.Context
import android.content.SharedPreferences

class Settings(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun saveStringPreference(key: String, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringPreference(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun saveBooleanPreference(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanPreference(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    companion object {
        private const val SP_NAME = "ru.plumsoftware.photoeditor.setting"
        const val SHOW_MORE_TIP_1 = "is_show_tip_1"
    }
}