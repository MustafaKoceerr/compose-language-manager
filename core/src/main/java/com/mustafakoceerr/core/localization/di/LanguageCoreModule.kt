package com.mustafakoceerr.core.localization.di

import com.mustafakoceerr.core.localization.data.LanguageManagerImpl
import com.mustafakoceerr.core.localization.domain.repository.ILanguageManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds [LanguageManagerImpl] to [ILanguageManager]. Internal to hide implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LanguageCoreModule {
    @Binds
    @Singleton
    abstract fun bindLanguageManager(impl: LanguageManagerImpl): ILanguageManager
}