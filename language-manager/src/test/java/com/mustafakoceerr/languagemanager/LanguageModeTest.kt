package com.mustafakoceerr.languagemanager

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Locale

class LanguageModeTest {

    @Test
    fun `custom accepts well-formed tags`() {
        assertThat(LanguageMode.Custom("tr").locale).isEqualTo(Locale.forLanguageTag("tr"))
        assertThat(LanguageMode.Custom("pt-BR").locale.country).isEqualTo("BR")
        assertThat(LanguageMode.Custom("zh-Hant-TW").locale.script).isEqualTo("Hant")
    }

    @Test
    fun `custom rejects malformed tags`() {
        listOf("", " ", "pt_BR", "not a tag").forEach { tag ->
            assertThrows(IllegalArgumentException::class.java) { LanguageMode.Custom(tag) }
        }
    }
}
