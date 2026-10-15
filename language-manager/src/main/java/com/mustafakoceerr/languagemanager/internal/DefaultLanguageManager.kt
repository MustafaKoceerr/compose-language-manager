package com.mustafakoceerr.languagemanager.internal

import android.app.Application
import android.content.Context
import com.mustafakoceerr.languagemanager.LanguageManager
import com.mustafakoceerr.languagemanager.LanguageMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * [LanguageManager] backed by a [LocaleController]. The controller (i.e. the system or AppCompat)
 * is the single source of truth; [languageMode] is re-read from it after every write and refresh event.
 */
internal class DefaultLanguageManager(
    private val controller: LocaleController,
    refreshEvents: Flow<Unit>,
    scope: CoroutineScope,
) : LanguageManager {

    private val _languageMode = MutableStateFlow(readMode())
    override val languageMode: StateFlow<LanguageMode> = _languageMode.asStateFlow()

    init {
        refreshEvents.onEach { refresh() }.launchIn(scope)
    }

    override fun setLanguage(mode: LanguageMode) {
        controller.setApplicationLocales(mode.toLocaleList())
        refresh()
    }

    private fun refresh() {
        _languageMode.value = readMode()
    }

    private fun readMode(): LanguageMode = controller.getApplicationLocales().toLanguageMode()

    companion object {
        fun create(context: Context): DefaultLanguageManager {
            val application = context.applicationContext as Application
            return DefaultLanguageManager(
                controller = createLocaleController(application),
                refreshEvents = application.localeRefreshEvents(),
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
            )
        }
    }
}
