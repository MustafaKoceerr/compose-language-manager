package com.mustafakoceerr.languagemanager.testing

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mustafakoceerr.languagemanager.LanguageMode
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FakeLanguageManagerTest {

    @Test
    fun `starts with system default by default`() {
        assertThat(FakeLanguageManager().languageMode.value).isEqualTo(LanguageMode.SystemDefault)
    }

    @Test
    fun `starts with the given mode`() {
        assertThat(FakeLanguageManager(LanguageMode.Custom("tr")).languageMode.value)
            .isEqualTo(LanguageMode.Custom("tr"))
    }

    @Test
    fun `setLanguage updates state and records calls`() = runTest {
        val manager = FakeLanguageManager()

        manager.languageMode.test {
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)

            manager.setLanguage(LanguageMode.Custom("pt-BR"))
            manager.setLanguage(LanguageMode.SystemDefault)

            assertThat(awaitItem()).isEqualTo(LanguageMode.Custom("pt-BR"))
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)
        }
        assertThat(manager.setLanguageCalls)
            .containsExactly(LanguageMode.Custom("pt-BR"), LanguageMode.SystemDefault)
            .inOrder()
    }

    @Test
    fun `external change updates state without recording a call`() {
        val manager = FakeLanguageManager()

        manager.simulateExternalChange(LanguageMode.Custom("tr"))

        assertThat(manager.languageMode.value).isEqualTo(LanguageMode.Custom("tr"))
        assertThat(manager.setLanguageCalls).isEmpty()
    }
}
