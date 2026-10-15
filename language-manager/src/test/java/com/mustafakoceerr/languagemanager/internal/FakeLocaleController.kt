package com.mustafakoceerr.languagemanager.internal

import androidx.core.os.LocaleListCompat

internal class FakeLocaleController(
    var locales: LocaleListCompat = LocaleListCompat.getEmptyLocaleList(),
) : LocaleController {
    override fun getApplicationLocales(): LocaleListCompat = locales

    override fun setApplicationLocales(locales: LocaleListCompat) {
        this.locales = locales
    }
}
