package com.mustafakoceerr.languagemanager.internal

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Emits whenever the app locales may have changed without going through [DefaultLanguageManager.setLanguage]:
 * - a configuration change (e.g. language changed in system settings on API 33+),
 * - an activity creation (on API < 33 AppCompat only loads the stored locales once the first
 *   `AppCompatActivity` attaches its base context).
 */
internal fun Application.localeRefreshEvents(): Flow<Unit> = callbackFlow {
    val componentCallbacks = object : ComponentCallbacks {
        override fun onConfigurationChanged(newConfig: Configuration) {
            trySend(Unit)
        }

        @Deprecated("Deprecated in Java")
        override fun onLowMemory() = Unit
    }
    val activityCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            trySend(Unit)
        }

        override fun onActivityStarted(activity: Activity) = Unit
        override fun onActivityResumed(activity: Activity) = Unit
        override fun onActivityPaused(activity: Activity) = Unit
        override fun onActivityStopped(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
    }
    registerComponentCallbacks(componentCallbacks)
    registerActivityLifecycleCallbacks(activityCallbacks)
    awaitClose {
        unregisterComponentCallbacks(componentCallbacks)
        unregisterActivityLifecycleCallbacks(activityCallbacks)
    }
}
