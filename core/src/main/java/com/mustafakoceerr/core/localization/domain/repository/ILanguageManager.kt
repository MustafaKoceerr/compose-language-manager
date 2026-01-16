package com.mustafakoceerr.core.localization.domain.repository

import com.mustafakoceerr.core.localization.domain.model.LanguageMode
import kotlinx.coroutines.flow.Flow

/**
 * Contract for observing and updating the application language.
 */
interface ILanguageManager {
    fun getLanguageMode(): Flow<LanguageMode>
    suspend fun setLanguage(code: String?)
}