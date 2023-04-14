package com.albertoalbaladejo.cabify.di

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}
