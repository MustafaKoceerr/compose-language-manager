package com.mustafakoceerr.languagemanager

import java.util.Locale

/**
 * The language's name in the language itself (endonym), e.g. `"Türkçe"` or `"Português (Brasil)"`.
 * Suitable for language pickers, where each option should be readable by its speakers.
 */
public val LanguageMode.Custom.nativeDisplayName: String
    get() = displayName(inLocale = locale)

/**
 * The language's name localized for [inLocale], e.g. `"Turkish"` for `Locale.ENGLISH`.
 */
public fun LanguageMode.Custom.displayName(inLocale: Locale): String =
    locale.getDisplayName(inLocale).replaceFirstChar { it.titlecase(inLocale) }
