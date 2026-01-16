package com.mustafakoceerr.core.localization.domain.usecase

import com.mustafakoceerr.core.localization.domain.repository.ILanguageManager
import javax.inject.Inject

/**
 * Updates the app language. Null/Empty code reverts to System Default.
 */
class UpdateLanguageUseCase @Inject constructor(
    private val manager: ILanguageManager
) {
    suspend operator fun invoke(code: String?) = manager.setLanguage(code)
}