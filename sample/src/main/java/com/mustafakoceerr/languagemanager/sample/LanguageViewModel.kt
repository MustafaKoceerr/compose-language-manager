package com.mustafakoceerr.languagemanager.sample

import androidx.lifecycle.ViewModel
import com.mustafakoceerr.languagemanager.LanguageManager
import com.mustafakoceerr.languagemanager.LanguageMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageManager: LanguageManager,
) : ViewModel() {

    val languageMode: StateFlow<LanguageMode> = languageManager.languageMode

    fun onLanguageSelected(mode: LanguageMode) {
        languageManager.setLanguage(mode)
    }
}
