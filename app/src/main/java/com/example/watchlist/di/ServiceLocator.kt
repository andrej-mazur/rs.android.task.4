package com.example.watchlist.di

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
object ServiceLocator {

    private val instances = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T : Any> register(instance: T) = register(T::class, instance)

    fun <T : Any> register(kClass: KClass<T>, instance: T) {
        instances[kClass] = instance
    }

    inline fun <reified T : Any> locate() = locate(T::class)

    fun <T : Any> locate(kClass: KClass<T>): T = instances[kClass] as T
}

inline fun <reified T : Any> locateLazy(): Lazy<T> = lazy { ServiceLocator.locate(T::class) }