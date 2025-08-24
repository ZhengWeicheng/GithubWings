package com.wings.githubwings.framework.log

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    ASSERT
}

interface IFLog {
    /** 获取日志路径 */
    fun getLogPath(): String

    /** 设置日志等级 */
    fun setLogLevel(level: LogLevel)

    /** 检查日志等级是否启用 */
    fun isLogLevelEnabled(level: Int): Boolean

    /** 设置是否启用系统日志 */
    fun setSysLogEnabled(enabled: Boolean)

    /** 检查日志是否可用 */
    fun isLogEnable(): Boolean

    /** 设置日志是否可用 */
    fun setLogEnable(enable: Boolean)

    /** 设置最大文件数 */
    fun setMaxFileCount(maxCount: Int)

    /** 设置最大文件大小 */
    fun setMaxFileSize(byteSize: Int)

    /** 强制刷新日志到磁盘 */
    fun flushToDisk()
    fun verbose(tag: String, msg: String)
    fun debug(tag: String, msg: String)
    fun info(tag: String, msg: String)
    fun warn(tag: String, msg: String)
    fun error(tag: String, msg: String)

    fun verbose(obj: Any, format: String, vararg args: Any)
}