package com.mustafakoceerr.core.localization.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.mustafakoceerr.core.localization.domain.model.LanguageMode
import com.mustafakoceerr.core.localization.domain.repository.ILanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Internal implementation using [AppCompatDelegate] to persist language settings.
 */
@Singleton
internal class LanguageManagerImpl @Inject constructor() : ILanguageManager {

    private val _languageMode = MutableStateFlow(getCurrentMode())

    override fun getLanguageMode(): Flow<LanguageMode> = _languageMode.asStateFlow()

    /**
     * Updates locale. Null or empty [code] reverts to System Default.
     */
    override suspend fun setLanguage(code: String?) {
        val shouldFollowSystem = code.isNullOrEmpty()
        val localeList = if (shouldFollowSystem) LocaleListCompat.getEmptyLocaleList() else LocaleListCompat.forLanguageTags(code)

        AppCompatDelegate.setApplicationLocales(localeList)

        _languageMode.value = if (shouldFollowSystem) LanguageMode.SystemDefault else LanguageMode.Custom(code)
    }

    private fun getCurrentMode(): LanguageMode {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.isEmpty) return LanguageMode.SystemDefault

        val code = currentLocales.get(0)?.language
        return if (code.isNullOrEmpty()) LanguageMode.SystemDefault else LanguageMode.Custom(code)
    }
}