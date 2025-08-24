package com.wings.githubwings.log


import android.util.Log
import com.wings.githubwings.framework.log.IFLog
import com.wings.githubwings.framework.log.LogLevel

class FLogger : IFLog {
    private var isLogEnabled = true
    private var mLogLevel = LogLevel.INFO.ordinal
    private var mLogPath = ""

    override fun getLogPath(): String {
        return mLogPath
    }

    override fun setLogLevel(level: LogLevel) {
        mLogLevel = level.ordinal
    }

    override fun isLogLevelEnabled(level: Int): Boolean {
        return level <= mLogLevel
    }

    override fun setSysLogEnabled(enabled: Boolean) {
        // 设置系统日志启用状态逻辑（如果需要）
    }

    override fun isLogEnable(): Boolean {
        return isLogEnabled
    }

    override fun setLogEnable(enable: Boolean) {
        isLogEnabled = enable
    }

    override fun setMaxFileCount(maxCount: Int) {
        // 设置最大文件数逻辑（如果需要）
    }

    override fun setMaxFileSize(byteSize: Int) {
        // 设置最大文件大小逻辑（如果需要）
    }

    override fun flushToDisk() {
        // 刷新到磁盘逻辑（如果需要）
    }

    override fun verbose(tag: String, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.v(tag, msg)
        }
    }

    override fun verbose(obj: Any, format: String, vararg args: Any) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.v(obj.toString(), String.format(format, *args))
        }
    }

    override fun debug(tag: String, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.d(tag, msg)
        }
    }

    override fun info(tag: String, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.i(tag, msg)
        }
    }

    override fun warn(tag: String, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.w(tag, msg)
        }
    }

    override fun error(tag: String, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.e(tag, msg)
        }
    }

    fun verbose(obj: Any, msg: String) {
        if (isLogEnabled && isLogLevelEnabled(1)) {
            Log.v(obj.toString(), msg)
        }
    }
}