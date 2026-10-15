# Compose Language Manager

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

## Modules

| Module | Purpose |
|---|---|
| `:language-manager` | Core API, no DI |
| `:language-manager-hilt` | Optional Hilt module that provides `LanguageManager` as a singleton |
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

> Decide in your app which languages you support (for example with an app-owned enum) and map them to and from
> `LanguageMode`. If you use use cases, they belong in your app's domain layer.

## License

This project is licensed under the MIT License. See [LICENSE.txt](LICENSE.txt) for details.
