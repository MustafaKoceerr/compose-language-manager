package com.mustafakoceerr.languagemanager.internal

import android.app.Application
import android.os.Build
import androidx.core.os.LocaleListCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
class LocaleControllerTest {

    private val context = ApplicationProvider.getApplicationContext<Application>()

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    fun `api 32 uses appcompat backport`() {
        assertThat(createLocaleController(context)).isInstanceOf(AppCompatLocaleController::class.java)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.N])
    fun `api 24 uses appcompat backport`() {
        assertThat(createLocaleController(context)).isInstanceOf(AppCompatLocaleController::class.java)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.BAKLAVA])
    fun `api 33 and above use platform locale manager`() {
        assertThat(createLocaleController(context)).isInstanceOf(PlatformLocaleController::class.java)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.TIRAMISU, Build.VERSION_CODES.BAKLAVA])
    fun `platform controller round-trips locales`() {
        val controller = PlatformLocaleController(context)

        controller.setApplicationLocales(LocaleListCompat.forLanguageTags("pt-BR"))
        assertThat(controller.getApplicationLocales().toLanguageTags()).isEqualTo("pt-BR")

        controller.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        assertThat(controller.getApplicationLocales().isEmpty).isTrue()
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.S_V2])
    fun `appcompat controller round-trips locales`() {
        val controller = AppCompatLocaleController()

        controller.setApplicationLocales(LocaleListCompat.forLanguageTags("tr"))
        assertThat(controller.getApplicationLocales().toLanguageTags()).isEqualTo("tr")

        controller.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        assertThat(controller.getApplicationLocales().isEmpty).isTrue()
    }
}
