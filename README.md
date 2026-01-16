# Compose Language Manager

A lightweight Android library that manages **in-app language switching** using AndroidX AppCompat’s locale APIs (`AppCompatDelegate.setApplicationLocales()`), exposed via a clean contract (`Flow<LanguageMode>`) and integrated with **Hilt** (implementation hidden).

---

## Features

- ✅ Switch between **System Default** and **Custom language** (language code)
- ✅ Observe current language mode via `Flow<LanguageMode>`
- ✅ Update language via a simple API (`setLanguage(code)`)
- ✅ Persists language selection using AppCompat locales
- ✅ Hilt module included (implementation remains internal)

---

## Requirements (Must-Haves)

### 1) Hilt version (mandatory)

- **Hilt: 2.54+ is required (minimum).**
- This library relies on the newer Hilt/KSP2-related setup introduced after 2.54.  
  **Hilt versions below 2.54 are not supported and will not work.**

### 2) Android < 13 (mandatory manifest entry)

For correct behavior **below Android 13**, you **must** add the following service to your app `AndroidManifest.xml`:

```xml
<service
    android:name="androidx.appcompat.app.AppLocalesMetadataHolderService"
    android:enabled="false"
    android:exported="false">
    <meta-data
        android:name="autoStoreLocales"
        android:value="true" />
</service>
```

---

## Installation (JitPack)

### 1) Add JitPack repository

Add this to your `settings.gradle(.kts)`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2) Add dependency

```kotlin
dependencies {
    implementation("com.github.MustafaKoceerr:compose-language-manager:1.0.0")
}
```

---

## Public API

### `ILanguageManager`

```kotlin
interface ILanguageManager {
    fun getLanguageMode(): Flow<LanguageMode>
    suspend fun setLanguage(code: String?)
}
```

### `LanguageMode`

```kotlin
sealed interface LanguageMode {
    data object SystemDefault : LanguageMode
    data class Custom(val code: String) : LanguageMode
}
```

### Use Cases

- `ObserveLanguageModeUseCase` → observes current language mode as `Flow<LanguageMode>`
- `UpdateLanguageUseCase` → updates the language (`null` or empty → System Default)

---

## How it Works

- `setLanguage(null)` or `setLanguage("")` → reverts to **System Default**
- `setLanguage("tr")` → sets a **Custom** language code
- Current mode is derived from `AppCompatDelegate.getApplicationLocales()`
- Changes are applied via `AppCompatDelegate.setApplicationLocales(...)`

---

## Usage Example (Recommended Integration)

Below is a practical example showing how to integrate the library into a **Compose + MVI** screen with clean mapping:

- Library returns a **string code** (e.g., `"tr"`)
- Your app maps it to an app-owned enum (`AppLanguage`)
- Unknown codes (e.g., `"fr"`) are safely handled by falling back to **System Default**

### AppLanguage.kt

```kotlin
package com.mustafakoceerr.languageapplication.domain.model

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    TURKISH("tr");

    companion object {
        // Convert string code (e.g., "tr") to your app enum
        fun getByCode(code: String?): AppLanguage? {
            return entries.find { it.code == code }
        }
    }
}
```

### LanguageSelection.kt

```kotlin
package com.mustafakoceerr.languageapplication.domain.model

sealed interface LanguageSelection {
    data object SystemDefault : LanguageSelection
    data class Custom(val language: AppLanguage) : LanguageSelection
}
```

### LanguageContract.kt

```kotlin
package com.mustafakoceerr.languageapplication.mvi

data class LanguageState(
    val selection: LanguageSelection = LanguageSelection.SystemDefault,
    val availableLanguages: List<AppLanguage> = AppLanguage.entries
)

sealed class LanguageIntent {
    // null means "System Default"
    data class SelectLanguage(val language: AppLanguage?) : LanguageIntent()
}

sealed class LanguageEffect
```

### LanguageScreen.kt

```kotlin
package com.mustafakoceerr.languageapplication.mvi

@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 1) System Default option
        LanguageItem(
            text = stringResource(R.string.system_default),
            isSelected = state.selection is LanguageSelection.SystemDefault,
            onClick = {
                viewModel.handleIntent(LanguageIntent.SelectLanguage(null))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // 2) Supported languages
        LazyColumn {
            items(state.availableLanguages) { language ->

                val isSelected = (state.selection as? LanguageSelection.Custom)?.language == language

                LanguageItem(
                    text = getLanguageName(language),
                    isSelected = isSelected,
                    onClick = {
                        viewModel.handleIntent(LanguageIntent.SelectLanguage(language))
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun getLanguageName(language: AppLanguage): String {
    return when (language) {
        AppLanguage.ENGLISH -> stringResource(R.string.lang_english)
        AppLanguage.TURKISH -> stringResource(R.string.lang_turkish)
    }
}
```

### LanguageViewModel.kt

```kotlin
package com.mustafakoceerr.languageapplication.mvi

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val observeLanguageModeUseCase: ObserveLanguageModeUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LanguageState())
    val state: StateFlow<LanguageState> = _state.asStateFlow()

    init {
        observeLanguageChanges()
    }

    fun handleIntent(intent: LanguageIntent) {
        when (intent) {
            is LanguageIntent.SelectLanguage -> changeLanguage(intent.language)
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            observeLanguageModeUseCase().collect { mode ->
                // Mapping: Library (LanguageMode) -> App (LanguageSelection)
                val selection = when (mode) {
                    is LanguageMode.SystemDefault -> LanguageSelection.SystemDefault
                    is LanguageMode.Custom -> {
                        val appLanguage = AppLanguage.getByCode(mode.code)
                        if (appLanguage != null) {
                            LanguageSelection.Custom(appLanguage)
                        } else {
                            // Unknown code (e.g., library returns "fr" but app doesn't support it)
                            LanguageSelection.SystemDefault
                        }
                    }
                }
                _state.update { it.copy(selection = selection) }
            }
        }
    }

    private fun changeLanguage(language: AppLanguage?) {
        viewModelScope.launch {
            // Mapping: App (Enum) -> Library (String Code)
            updateLanguageUseCase(language?.code)
        }
    }
}
```

---

## Notes

- Passing `null` (or an empty string) to `setLanguage(...)` is the canonical way to revert to **System Default**.
- The recommended approach is to **own supported languages in the app** (e.g., `AppLanguage`) and map to/from the library’s string codes.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
