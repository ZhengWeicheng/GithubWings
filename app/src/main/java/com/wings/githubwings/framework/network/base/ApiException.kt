package com.wings.githubwings.framework.network.base

import java.io.IOException

/**
 * Custom API exception class
 */
class ApiException(
    message: String,
    val code: Int? = null,
    cause: Throwable? = null
) : IOException(message, cause)