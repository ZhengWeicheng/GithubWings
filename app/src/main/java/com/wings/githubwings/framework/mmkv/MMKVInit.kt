package com.wings.githubwings.framework.mmkv

import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import com.wings.githubwings.framework.properties.ISPPropertyInterface

class MMKVInit : ISPPropertyInterface {
    private lateinit var mmkv: MMKV

    fun getSpImpl(c: Context, name: String): SharedPreferences {
        MMKV.initialize(c)
        mmkv = MMKV.mmkvWithID(name, MMKV.SINGLE_PROCESS_MODE)
        return mmkv
    }

    override fun getString(key: String, default: String): String {
        return mmkv.getString(key, default) ?: default
    }

    override fun getInt(key: String, default: Int): Int {
        return mmkv.getInt(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return mmkv.getLong(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return mmkv.getFloat(key, default)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return mmkv.getBoolean(key, default)
    }

    override fun putString(key: String, value: String) {
        mmkv.putString(key, value)
    }

    override fun putInt(key: String, value: Int) {
        mmkv.putInt(key, value)
    }

    override fun putLong(key: String, value: Long) {
        mmkv.putLong(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        mmkv.putFloat(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.putBoolean(key, value)
    }

    override fun remove(key: String) {
        mmkv.remove(key)
    }

    override fun clear() {
        mmkv.clearAll()
    }
}