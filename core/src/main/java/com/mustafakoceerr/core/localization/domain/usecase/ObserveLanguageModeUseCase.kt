package com.mustafakoceerr.core.localization.domain.usecase

import com.mustafakoceerr.core.localization.domain.model.LanguageMode
import com.mustafakoceerr.core.localization.domain.repository.ILanguageManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes the current language mode flow.
 */
class ObserveLanguageModeUseCase @Inject constructor(
    private val manager: ILanguageManager
) {
    operator fun invoke(): Flow<LanguageMode> = manager.getLanguageMode()
}