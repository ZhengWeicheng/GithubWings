package com.wings.githubwings.framework.log

import kotlin.concurrent.Volatile

object FLog {
    @Volatile
    lateinit var mLogger: IFLog
    fun init(impl: IFLog) {
        mLogger = impl
    }

    fun debug(tag: String, msg: String) {
        mLogger.debug(tag, msg)
    }

    fun verbose(tag: String, msg: String) {
        mLogger.verbose(tag, msg)
    }

    fun info(tag: String, msg: String) {
        mLogger.info(tag, msg)
    }

    fun warn(tag: String, msg: String) {
        mLogger.warn(tag, msg)
    }

    fun error(tag: String, msg: String) {
        mLogger.error(tag, msg)
    }

    fun doLog(level: Int, tag: String, msg: String) {
        when (level) {
            1 -> mLogger.debug(tag, msg)
            0 -> mLogger.verbose(tag, msg)
            2 -> mLogger.info(tag, msg)
            3 -> mLogger.warn(tag, msg)
            4 -> mLogger.error(tag, msg)
            else -> {}
        }
    }

    fun safeLog(msg: String): String {
        return if (msg.length > 4) "${
            msg.substring(
                0,
                3
            )
        }*****${msg.substring(msg.length - 3)}" else msg
    }

    /** 获取日志路径 */
    fun getLogPath(): String {
        return mLogger.getLogPath()!!
    }

    /** 设置日志等级 */
    fun setLogLevel(level: LogLevel) {
        mLogger.setLogLevel(level)
    }

    /** 检查日志等级是否启用 */
    fun isLogLevelEnabled(level: Int): Boolean {
        return mLogger.isLogLevelEnabled(level)
    }

    /** 设置是否启用系统日志 */
    fun setSysLogEnabled(enabled: Boolean) {
        mLogger.setSysLogEnabled(enabled)
    }

    /** 检查日志是否可用 */
    fun isLogEnable(): Boolean {
        return mLogger.isLogEnable()
    }

    /** 设置日志是否可用 */
    fun setLogEnable(enable: Boolean) {
        mLogger.setLogEnable(enable)
    }

    /** 设置最大文件数 */
    fun setMaxFileCount(maxCount: Int) {
        mLogger.setMaxFileCount(maxCount)
    }

    /** 设置最大文件大小 */
    fun setMaxFileSize(byteSize: Int) {
        mLogger.setMaxFileSize(byteSize)
    }

    /** 强制刷新日志到磁盘 */
    fun flushToDisk() {
        mLogger.flushToDisk()
    }
}