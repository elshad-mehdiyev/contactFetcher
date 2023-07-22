package com.example.contact_list

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContentResolver(
        @ApplicationContext context: Context
    ) = context.contentResolver

    @Provides
    fun provideContactFetcher(
        contentResolver: ContentResolver
    ) = ContactFetcher(contentResolver)
}
