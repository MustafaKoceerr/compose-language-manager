package com.mustafakoceerr.languagemanager.testing

import com.mustafakoceerr.languagemanager.LanguageManager
import com.mustafakoceerr.languagemanager.LanguageMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory [LanguageManager] for unit tests and Compose previews. Doesn't touch any platform API.
 *
 * ```
 * val languageManager = FakeLanguageManager(LanguageMode.Custom("tr"))
 * val viewModel = LanguageViewModel(languageManager)
 * viewModel.onLanguageSelected(LanguageMode.SystemDefault)
 * assertThat(languageManager.setLanguageCalls).containsExactly(LanguageMode.SystemDefault)
 * ```
 */
public class FakeLanguageManager(
    initialMode: LanguageMode = LanguageMode.SystemDefault,
) : LanguageManager {

    private val _languageMode = MutableStateFlow(initialMode)
    override val languageMode: StateFlow<LanguageMode> = _languageMode.asStateFlow()

    private val _setLanguageCalls = mutableListOf<LanguageMode>()

    /** Every mode passed to [setLanguage], in call order. */
    public val setLanguageCalls: List<LanguageMode> get() = _setLanguageCalls.toList()

    override fun setLanguage(mode: LanguageMode) {
        _setLanguageCalls += mode
        _languageMode.value = mode
    }

    /**
     * Simulates a change made outside the app, e.g. from the system per-app language settings.
     * Not recorded in [setLanguageCalls].
     */
    public fun simulateExternalChange(mode: LanguageMode) {
        _languageMode.value = mode
    }
}
