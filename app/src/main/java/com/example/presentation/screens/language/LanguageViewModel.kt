package com.example.presentation.screens.language

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.util.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    private val _selectedLanguage = MutableStateFlow("ta") // Default Tamil
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    fun loadSavedLanguage(context: Context) {
        viewModelScope.launch {
            val lang = LocaleHelper.getSavedLanguage(context).first()
            _selectedLanguage.value = lang
        }
    }

    fun selectLanguage(langCode: String) {
        _selectedLanguage.value = langCode
    }

    fun saveAndContinue(context: Context, onContinued: () -> Unit) {
        viewModelScope.launch {
            LocaleHelper.saveLanguage(context, _selectedLanguage.value)
            LocaleHelper.applyLocale(context, _selectedLanguage.value)
            onContinued()
        }
    }
}
