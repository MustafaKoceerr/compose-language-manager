package com.mustafakoceerr.languagemanager.sample

import com.google.common.truth.Truth.assertThat
import com.mustafakoceerr.languagemanager.LanguageMode
import com.mustafakoceerr.languagemanager.testing.FakeLanguageManager
import org.junit.Test

class LanguageViewModelTest {

    private val languageManager = FakeLanguageManager(LanguageMode.Custom("tr"))
    private val viewModel = LanguageViewModel(languageManager)

    @Test
    fun `exposes the current language`() {
        assertThat(viewModel.languageMode.value).isEqualTo(LanguageMode.Custom("tr"))
    }

    @Test
    fun `selecting a language forwards it to the manager`() {
        viewModel.onLanguageSelected(LanguageMode.SystemDefault)

        assertThat(languageManager.setLanguageCalls).containsExactly(LanguageMode.SystemDefault)
        assertThat(viewModel.languageMode.value).isEqualTo(LanguageMode.SystemDefault)
    }

    @Test
    fun `reflects changes made outside the app`() {
        languageManager.simulateExternalChange(LanguageMode.Custom("pt-BR"))

        assertThat(viewModel.languageMode.value).isEqualTo(LanguageMode.Custom("pt-BR"))
    }
}
