package com.albertoalbaladejo.cabify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = AndroidDispatcherProvider()
}