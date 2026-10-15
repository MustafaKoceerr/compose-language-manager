package com.mustafakoceerr.languagemanager.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.LocaleManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mustafakoceerr.languagemanager.LanguageManager
import com.mustafakoceerr.languagemanager.LanguageMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

/**
 * Overrides the [LanguageManager] returned by [rememberLanguageManager] for a subtree,
 * e.g. to supply a fake in UI tests or previews:
 *
 * ```
 * CompositionLocalProvider(LocalLanguageManager provides FakeLanguageManager(LanguageMode.Custom("tr"))) {
 *     LanguageSettings()
 * }
 * ```
 */
public val LocalLanguageManager: ProvidableCompositionLocal<LanguageManager?> =
    staticCompositionLocalOf { null }

/**
 * Returns the [LanguageManager] for this composition: the one provided through [LocalLanguageManager] if any,
 * an in-memory manager in previews (`LocalInspectionMode`), otherwise the process-wide [LanguageManager.create].
 */
@Composable
public fun rememberLanguageManager(): LanguageManager {
    val provided = LocalLanguageManager.current
    val inInspectionMode = LocalInspectionMode.current
    val context = LocalContext.current
    return remember(provided, inInspectionMode, context) {
        when {
            provided != null -> provided
            inInspectionMode -> PreviewLanguageManager()
            else -> LanguageManager.create(context)
        }
    }
}

/**
 * Collects [LanguageManager.languageMode] as [State], lifecycle-aware.
 */
@Composable
public fun rememberLanguageMode(
    languageManager: LanguageManager = rememberLanguageManager(),
): State<LanguageMode> = languageManager.languageMode.collectAsStateWithLifecycle()

/**
 * The device's primary locale, ignoring the app-specific language. Useful for labelling the
 * "system default" option, e.g. `"System default (English)"`. Updates when the configuration changes.
 */
@Composable
public fun rememberSystemLocale(): Locale {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    return remember(context, configuration) {
        LocaleManagerCompat.getSystemLocales(context)[0] ?: Locale.getDefault()
    }
}

/** Preview-only manager: keeps the selection in memory so previews stay interactive without platform APIs. */
private class PreviewLanguageManager : LanguageManager {
    override val languageMode = MutableStateFlow<LanguageMode>(LanguageMode.SystemDefault)

    override fun setLanguage(mode: LanguageMode) {
        languageMode.value = mode
    }
}
