package com.wings.githubwings.framework.service.center

import kotlin.reflect.KClass

/**
 * The initialization mode for the KMService annotation.
 * @param Lazy - The service will be initialized when it is first used.
 * @param Sync - The service will be initialized when the application starts.
 * @param Async - The service will be initialized asynchronously when the application starts.
 */
enum class Initialization {
    Lazy,
    Sync,
    Async
}

/**
 * Annotation for marking a class as a service.
 * @param name - as a key to save this service in ServiceCenter.
 * @param initMode - The initialization mode of the service.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class KMService(
    val initMode: Initialization = Initialization.Lazy,
    val name: String = "",
    val serviceInterface: KClass<*> = Nothing::class,
)
