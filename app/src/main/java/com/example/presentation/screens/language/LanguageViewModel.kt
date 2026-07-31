package com.example.presentation.screens.language

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.util.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageViewModel : ViewModel() {
    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    fun loadSavedLanguage(context: Context) {
        val lang = LocaleHelper.getSavedLanguage(context)
        _selectedLanguage.value = lang
    }

    fun selectLanguage(langCode: String) {
        _selectedLanguage.value = langCode
    }

    fun saveAndContinue(context: Context, onContinued: () -> Unit) {
        try {
            LocaleHelper.saveLanguage(context, _selectedLanguage.value)
            LocaleHelper.applyLocale(context, _selectedLanguage.value)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            onContinued() // Guarantee navigation happens even if there's an error
        }
    }
}
