package com.wings.githubwings.framework.properties

interface ISPPropertyInterface {

    fun getString(key: String, default: String): String

    fun getInt(key: String, default: Int): Int

    fun getLong(key: String, default: Long): Long

    fun getFloat(key: String, default: Float): Float

    fun getBoolean(key: String, default: Boolean): Boolean

    fun putString(key: String, value: String)

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putBoolean(key: String, value: Boolean)

    fun remove(key: String)

    fun clear()
}