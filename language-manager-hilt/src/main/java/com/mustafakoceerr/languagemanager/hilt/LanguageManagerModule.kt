package com.mustafakoceerr.languagemanager.hilt

import android.content.Context
import com.mustafakoceerr.languagemanager.LanguageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the process-wide [LanguageManager] to the Hilt [SingletonComponent].
 */
@Module
@InstallIn(SingletonComponent::class)
public object LanguageManagerModule {

    @Provides
    @Singleton
    public fun provideLanguageManager(@ApplicationContext context: Context): LanguageManager =
        LanguageManager.create(context)
}
