package com.mustafakoceerr.languagemanager.internal

import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.mustafakoceerr.languagemanager.LanguageMode
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageModeMapperTest {

    @Test
    fun `system default maps to empty locale list`() {
        assertThat(LanguageMode.SystemDefault.toLocaleList().isEmpty).isTrue()
    }

    @Test
    fun `custom maps to single locale list`() {
        assertThat(LanguageMode.Custom("pt-BR").toLocaleList().toLanguageTags()).isEqualTo("pt-BR")
    }

    @Test
    fun `empty locale list maps to system default`() {
        assertThat(LocaleListCompat.getEmptyLocaleList().toLanguageMode()).isEqualTo(LanguageMode.SystemDefault)
    }

    @Test
    fun `region is preserved`() {
        assertThat(LocaleListCompat.forLanguageTags("pt-BR").toLanguageMode())
            .isEqualTo(LanguageMode.Custom("pt-BR"))
    }

    @Test
    fun `primary locale wins for multiple locales`() {
        assertThat(LocaleListCompat.forLanguageTags("tr,en-US").toLanguageMode())
            .isEqualTo(LanguageMode.Custom("tr"))
    }

    @Test
    fun `undetermined locale maps to system default`() {
        assertThat(LocaleListCompat.forLanguageTags("und").toLanguageMode()).isEqualTo(LanguageMode.SystemDefault)
    }
}
