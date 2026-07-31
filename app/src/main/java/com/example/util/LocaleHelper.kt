package com.example.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val LANGUAGE_KEY = "app_language"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getSavedLanguage(context: Context): String {
        return getPrefs(context).getString(LANGUAGE_KEY, "en") ?: "en"
    }

    fun saveLanguage(context: Context, langCode: String) {
        getPrefs(context).edit().putString(LANGUAGE_KEY, langCode).apply()
    }

    fun applyLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
