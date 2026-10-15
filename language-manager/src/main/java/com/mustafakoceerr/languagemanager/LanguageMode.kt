package com.mustafakoceerr.languagemanager

import java.util.Locale

/**
 * The application language selection.
 */
public sealed interface LanguageMode {

    /** No app-specific language: the app follows the device language. */
    public data object SystemDefault : LanguageMode

    /**
     * An app-specific language.
     *
     * @property languageTag a well-formed IETF BCP 47 language tag, e.g. `"tr"` or `"pt-BR"`.
     * Values read back from [LanguageManager.languageMode] are normalized by the platform
     * (e.g. `"pt-br"` is reported as `"pt-BR"`).
     */
    public data class Custom(val languageTag: String) : LanguageMode {
        init {
            require(Locale.forLanguageTag(languageTag).language.isNotEmpty()) {
                "\"$languageTag\" is not a well-formed BCP 47 language tag"
            }
        }

        /** The [Locale] for [languageTag]. */
        val locale: Locale get() = Locale.forLanguageTag(languageTag)
    }
}
