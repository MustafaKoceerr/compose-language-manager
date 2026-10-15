package com.mustafakoceerr.languagemanager.internal

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class AppEventsTest {

    private val application = ApplicationProvider.getApplicationContext<Application>()

    @Test
    fun `configuration change emits`() = runTest {
        application.localeRefreshEvents().test {
            application.onConfigurationChanged(Configuration(application.resources.configuration))
            awaitItem()
        }
    }

    @Test
    fun `activity creation emits`() = runTest {
        application.localeRefreshEvents().test {
            Robolectric.buildActivity(Activity::class.java).create()
            awaitItem()
        }
    }
}
