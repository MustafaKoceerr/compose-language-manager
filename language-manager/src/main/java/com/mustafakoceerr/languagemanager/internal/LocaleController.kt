package com.mustafakoceerr.languagemanager.internal

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Reads and writes the per-app locales. Implementations are chosen by API level in [createLocaleController].
 */
internal interface LocaleController {
    fun getApplicationLocales(): LocaleListCompat
    fun setApplicationLocales(locales: LocaleListCompat)
}

/** API 33+: framework per-app language API. The system persists the selection. */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class PlatformLocaleController(context: Context) : LocaleController {

    private val localeManager = context.getSystemService(LocaleManager::class.java)

    override fun getApplicationLocales(): LocaleListCompat =
        LocaleListCompat.wrap(localeManager.applicationLocales)

    override fun setApplicationLocales(locales: LocaleListCompat) {
        localeManager.applicationLocales = LocaleList.forLanguageTags(locales.toLanguageTags())
    }
}

/**
 * API < 33: official AppCompat backport. Persistence relies on the `autoStoreLocales` service
 * declared in this library's manifest; locales are applied to `AppCompatActivity` instances only.
 */
internal class AppCompatLocaleController : LocaleController {

    override fun getApplicationLocales(): LocaleListCompat = AppCompatDelegate.getApplicationLocales()

    override fun setApplicationLocales(locales: LocaleListCompat) {
        AppCompatDelegate.setApplicationLocales(locales)
    }
}

internal fun createLocaleController(context: Context): LocaleController =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PlatformLocaleController(context)
    } else {
        AppCompatLocaleController()
    }
