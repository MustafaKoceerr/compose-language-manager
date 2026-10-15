package com.mustafakoceerr.languagemanager.internal

import androidx.core.os.LocaleListCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mustafakoceerr.languagemanager.LanguageMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultLanguageManagerTest {

    private val controller = FakeLocaleController()
    private val refreshEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val scope = TestScope(UnconfinedTestDispatcher())

    private fun createManager() = DefaultLanguageManager(controller, refreshEvents, scope)

    @Test
    fun `initial mode is read from controller`() {
        controller.locales = LocaleListCompat.forLanguageTags("tr")

        assertThat(createManager().languageMode.value).isEqualTo(LanguageMode.Custom("tr"))
    }

    @Test
    fun `initial mode is system default when no app locales are set`() {
        assertThat(createManager().languageMode.value).isEqualTo(LanguageMode.SystemDefault)
    }

    @Test
    fun `setLanguage writes to controller and emits new mode`() = runTest {
        val manager = createManager()

        manager.languageMode.test {
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)

            manager.setLanguage(LanguageMode.Custom("pt-BR"))
            assertThat(controller.locales.toLanguageTags()).isEqualTo("pt-BR")
            assertThat(awaitItem()).isEqualTo(LanguageMode.Custom("pt-BR"))

            manager.setLanguage(LanguageMode.SystemDefault)
            assertThat(controller.locales.isEmpty).isTrue()
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)
        }
    }

    @Test
    fun `mode reflects platform normalization`() {
        val manager = createManager()

        manager.setLanguage(LanguageMode.Custom("pt-br"))

        assertThat(manager.languageMode.value).isEqualTo(LanguageMode.Custom("pt-BR"))
    }

    @Test
    fun `refresh event picks up external changes`() = runTest {
        val manager = createManager()

        manager.languageMode.test {
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)

            controller.locales = LocaleListCompat.forLanguageTags("tr")
            refreshEvents.emit(Unit)

            assertThat(awaitItem()).isEqualTo(LanguageMode.Custom("tr"))
        }
    }

    @Test
    fun `refresh without external change does not emit`() = runTest {
        val manager = createManager()

        manager.languageMode.test {
            assertThat(awaitItem()).isEqualTo(LanguageMode.SystemDefault)
            refreshEvents.emit(Unit)
            expectNoEvents()
        }
    }
}
