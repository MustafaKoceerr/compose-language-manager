package com.mustafakoceerr.core.localization.domain.model

/**
 * Represents language state: either [SystemDefault] or [Custom] with a language code.
 */
sealed interface LanguageMode {
    data object SystemDefault : LanguageMode
    data class Custom(val code: String) : LanguageMode
}