# Compose Language Manager

[![CI](https://github.com/MustafaKoceerr/compose-language-manager/actions/workflows/ci.yml/badge.svg)](https://github.com/MustafaKoceerr/compose-language-manager/actions/workflows/ci.yml)
[![JitPack](https://jitpack.io/v/MustafaKoceerr/compose-language-manager.svg)](https://jitpack.io/#MustafaKoceerr/compose-language-manager)

A lightweight Android library for **in-app (per-app) language switching**, built on the official platform APIs and
exposed as a `StateFlow<LanguageMode>`. It doesn't depend on any DI framework. Hilt support comes in an optional module.

## How it works

The library follows Google's [per-app language preferences](https://developer.android.com/guide/topics/resources/app-languages) guidance:

| API level | Mechanism | Persistence |
|---|---|---|
| 33+ (Android 13+) | Framework `LocaleManager` | Stored by the system, synced with *Settings > Apps > App languages* |
| 24–32 | AppCompat backport `AppCompatDelegate.setApplicationLocales()` | AppCompat `autoStoreLocales` (declared by this library's manifest) |

The choice is made at runtime behind an internal `LocaleController` abstraction. The system (or AppCompat) is the single
source of truth: `languageMode` is re-read after every change and also reflects changes made outside the app, such as from
the system settings.

## Requirements

- **minSdk 24**
- **`AppCompatActivity`** with an AppCompat theme (e.g. `Theme.AppCompat.DayNight.NoActionBar`). The AppCompat backport
  only applies locales to AppCompat activities on API < 33.
- Recommended for API 33+: list your app's languages in the system settings:

  ```kotlin
  // app/build.gradle.kts
  android {
      androidResources { generateLocaleConfig = true }
  }
  ```

  ```properties
  # app/src/main/res/resources.properties
  unqualifiedResLocale=en
  ```

You don't need to add any manifest entries. The `AppLocalesMetadataHolderService` is merged in from the library.

## Installation (JitPack)

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.github.MustafaKoceerr.compose-language-manager:language-manager:2.1.0")

    // Optional (each includes language-manager)
    implementation("com.github.MustafaKoceerr.compose-language-manager:language-manager-compose:2.1.0")
    implementation("com.github.MustafaKoceerr.compose-language-manager:language-manager-hilt:2.1.0")
    testImplementation("com.github.MustafaKoceerr.compose-language-manager:language-manager-testing:2.1.0")
}
```

To try an unreleased branch, use `<branch>-SNAPSHOT` as the version (e.g. `v2-rework-SNAPSHOT`).

## Modules

| Module | Purpose |
|---|---|
| `:language-manager` | Core API, no DI |
| `:language-manager-compose` | Optional Compose helpers: `rememberLanguageMode()`, `rememberSystemLocale()`, preview support |
| `:language-manager-hilt` | Optional Hilt module that provides `LanguageManager` as a singleton |
| `:language-manager-testing` | `FakeLanguageManager` for unit tests and previews |
| `:sample` | Compose demo app |

## API

```kotlin
public sealed interface LanguageMode {
    data object SystemDefault : LanguageMode
    data class Custom(val languageTag: String) : LanguageMode // BCP 47: "tr", "pt-BR"
}

public interface LanguageManager {
    val languageMode: StateFlow<LanguageMode>
    @MainThread fun setLanguage(mode: LanguageMode)

    companion object {
        fun create(context: Context): LanguageManager // process-wide instance
    }
}
```

`Custom` keeps the full language tag, including region and script. Tags read back are normalized by the platform, so
`"pt-br"` is reported as `"pt-BR"`.

### Display names

```kotlin
LanguageMode.Custom("pt-BR").nativeDisplayName        // "Português (Brasil)" — use in language pickers
LanguageMode.Custom("tr").displayName(Locale.ENGLISH)  // "Turkish"
```

## Usage

### Without DI

```kotlin
val languageManager = LanguageManager.create(context)

languageManager.setLanguage(LanguageMode.Custom("tr"))
languageManager.setLanguage(LanguageMode.SystemDefault)
```

### With Hilt

Add `:language-manager-hilt` and inject `LanguageManager`:

```kotlin
@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageManager: LanguageManager,
) : ViewModel() {

    val languageMode: StateFlow<LanguageMode> = languageManager.languageMode

    fun onLanguageSelected(mode: LanguageMode) = languageManager.setLanguage(mode)
}
```

```kotlin
@Composable
fun LanguageScreen(viewModel: LanguageViewModel = hiltViewModel()) {
    val languageMode by viewModel.languageMode.collectAsStateWithLifecycle()
    // render options, call viewModel.onLanguageSelected(...)
}
```

See [`sample`](sample/src/main/java/com/mustafakoceerr/languagemanager/sample) for a complete Compose screen.

### Compose without DI (`language-manager-compose`)

```kotlin
@Composable
fun LanguageSettings() {
    val languageManager = rememberLanguageManager()
    val languageMode by rememberLanguageMode(languageManager)
    val systemLocale = rememberSystemLocale() // device language, ignoring the app override

    // e.g. "System default (English)"
    val systemLabel = "System default (${LanguageMode.Custom(systemLocale.language).nativeDisplayName})"
    // render options, call languageManager.setLanguage(...)
}
```

- `rememberLanguageManager()` returns the process-wide manager. In `@Preview` it returns an in-memory manager, so
  previews don't touch platform APIs.
- Use `LocalLanguageManager` to provide a different manager, e.g. a fake in UI tests:
  `CompositionLocalProvider(LocalLanguageManager provides FakeLanguageManager()) { ... }`

### Testing (`language-manager-testing`)

```kotlin
val languageManager = FakeLanguageManager(LanguageMode.Custom("tr"))
val viewModel = LanguageViewModel(languageManager)

viewModel.onLanguageSelected(LanguageMode.SystemDefault)
assertThat(languageManager.setLanguageCalls).containsExactly(LanguageMode.SystemDefault)

languageManager.simulateExternalChange(LanguageMode.Custom("pt-BR")) // e.g. changed in system settings
```

> Decide in your app which languages you support (for example with an app-owned enum) and map them to and from
> `LanguageMode`. If you use use cases, they belong in your app's domain layer.

## License

This project is licensed under the MIT License. See [LICENSE.txt](LICENSE.txt) for details.
