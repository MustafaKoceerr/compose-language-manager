package com.mustafakoceerr.languagemanager

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Locale

class DisplayNamesTest {

    @Test
    fun `native display name is the endonym`() {
        assertThat(LanguageMode.Custom("tr").nativeDisplayName).isEqualTo("Türkçe")
        assertThat(LanguageMode.Custom("en").nativeDisplayName).isEqualTo("English")
    }

    @Test
    fun `native display name includes region`() {
        assertThat(LanguageMode.Custom("pt-BR").nativeDisplayName).isEqualTo("Português (Brasil)")
    }

    @Test
    fun `native display name is capitalized for lowercase endonyms`() {
        assertThat(LanguageMode.Custom("fr").nativeDisplayName).isEqualTo("Français")
    }

    @Test
    fun `display name is localized for the given locale`() {
        assertThat(LanguageMode.Custom("tr").displayName(Locale.ENGLISH)).isEqualTo("Turkish")
        assertThat(LanguageMode.Custom("en").displayName(Locale.forLanguageTag("tr"))).isEqualTo("İngilizce")
    }
}
