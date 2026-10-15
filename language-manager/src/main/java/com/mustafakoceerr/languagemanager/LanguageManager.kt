package com.mustafakoceerr.languagemanager

import android.content.Context
import androidx.annotation.MainThread
import com.mustafakoceerr.languagemanager.internal.DefaultLanguageManager
import kotlinx.coroutines.flow.StateFlow

/**
 * Observes and changes the per-app language.
 *
 * On API 33+ the framework `LocaleManager` is used, so the selection is stored by the system and stays in
 * sync with *Settings > Apps > App languages*. On API 24–32 the official AppCompat backport
 * (`AppCompatDelegate.setApplicationLocales`) is used, which requires activities to extend `AppCompatActivity`.
 *
 * Obtain the process-wide instance with [LanguageManager.create].
 */
public interface LanguageManager {

    /**
     * The current language selection. Also reflects changes made outside the app,
     * e.g. from the system per-app language settings on API 33+.
     */
    public val languageMode: StateFlow<LanguageMode>

    /**
     * Applies [mode] to the whole app. Visible activities are recreated by the platform (API 33+)
     * or by AppCompat (API < 33) to pick up the new resources.
     */
    @MainThread
    public fun setLanguage(mode: LanguageMode)

    public companion object {
        @Volatile
        private var instance: LanguageManager? = null

        /** Returns the process-wide [LanguageManager]. Safe to call repeatedly. */
        public fun create(context: Context): LanguageManager =
            instance ?: synchronized(this) {
                instance ?: DefaultLanguageManager.create(context.applicationContext).also { instance = it }
            }
    }
}
