package com.example.launderup.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class LanguageLocalHelper {
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "LaunderUp"

    fun setLocale(context: Context, language: String): Context {

        persist(context,language)
        return updateResource(context,language)
    }

    private fun persist(context: Context, language: String) {
        sharedPreferences=context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("SELECTED_LANGUAGE", language)
        editor.apply()
        editor.commit()
    }

    private fun updateResource(context: Context, language: String): Context {
        val locale= Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun updateResourceLegacy(context: Context, language: String): Context {
        val locale= Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration,resources.displayMetrics)
        return context
    }
}