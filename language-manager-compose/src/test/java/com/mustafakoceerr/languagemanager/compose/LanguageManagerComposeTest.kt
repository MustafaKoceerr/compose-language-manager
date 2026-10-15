package com.mustafakoceerr.languagemanager.compose

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.mustafakoceerr.languagemanager.LanguageManager
import com.mustafakoceerr.languagemanager.LanguageMode
import com.mustafakoceerr.languagemanager.testing.FakeLanguageManager
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class LanguageManagerComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `provided manager is used`() {
        val fake = FakeLanguageManager()
        lateinit var resolved: LanguageManager

        composeRule.setContent {
            CompositionLocalProvider(LocalLanguageManager provides fake) {
                resolved = rememberLanguageManager()
            }
        }

        assertThat(resolved).isSameInstanceAs(fake)
    }

    @Test
    fun `process-wide manager is used by default`() {
        lateinit var resolved: LanguageManager
        lateinit var expected: LanguageManager

        composeRule.setContent {
            resolved = rememberLanguageManager()
            expected = LanguageManager.create(LocalContext.current)
        }

        assertThat(resolved).isSameInstanceAs(expected)
    }

    @Test
    fun `inspection mode uses an in-memory manager`() {
        lateinit var resolved: LanguageManager

        composeRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                resolved = rememberLanguageManager()
            }
        }

        assertThat(resolved.languageMode.value).isEqualTo(LanguageMode.SystemDefault)
        resolved.setLanguage(LanguageMode.Custom("tr"))
        assertThat(resolved.languageMode.value).isEqualTo(LanguageMode.Custom("tr"))
    }

    @Test
    fun `language mode state recomposes on change`() {
        val fake = FakeLanguageManager()

        composeRule.setContent {
            CompositionLocalProvider(LocalLanguageManager provides fake) {
                val mode by rememberLanguageMode()
                BasicText(mode.toString())
            }
        }
        composeRule.onNodeWithText(LanguageMode.SystemDefault.toString()).assertExists()

        fake.simulateExternalChange(LanguageMode.Custom("tr"))

        composeRule.onNodeWithText(LanguageMode.Custom("tr").toString()).assertExists()
    }

    @Test
    fun `system locale is the device locale`() {
        lateinit var systemLocale: Locale

        composeRule.setContent { systemLocale = rememberSystemLocale() }

        assertThat(systemLocale.language).isNotEmpty()
    }
}
