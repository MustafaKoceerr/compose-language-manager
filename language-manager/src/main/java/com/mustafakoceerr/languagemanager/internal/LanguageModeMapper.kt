package com.mustafakoceerr.languagemanager.internal

import androidx.core.os.LocaleListCompat
import com.mustafakoceerr.languagemanager.LanguageMode

internal fun LanguageMode.toLocaleList(): LocaleListCompat = when (this) {
    LanguageMode.SystemDefault -> LocaleListCompat.getEmptyLocaleList()
    is LanguageMode.Custom -> LocaleListCompat.forLanguageTags(languageTag)
}

/** Maps app locales to a [LanguageMode]; only the primary locale is significant. */
internal fun LocaleListCompat.toLanguageMode(): LanguageMode {
    val primary = this[0] ?: return LanguageMode.SystemDefault
    if (primary.language.isEmpty()) return LanguageMode.SystemDefault
    return LanguageMode.Custom(primary.toLanguageTag())
}
