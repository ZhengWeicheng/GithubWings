package com.wings.githubwings.framework.utils

import android.widget.Toast
import com.wings.githubwings.framework.BaseCore

/**
 * 通用Toast工具类
 */
object ToastUtil {

    /**
     * 显示短时间Toast
     * @param message 要显示的消息
     */
    fun showShort(message: String) {
        Toast.makeText(BaseCore.app, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示长时间Toast
     * @param message 要显示的消息
     */
    fun showLong(message: String) {
        Toast.makeText(BaseCore.app, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 显示成功Toast
     * @param message 要显示的消息
     */
    fun showSuccess(message: String) {
        // 目前使用普通Toast，后续可以自定义样式
        showShort("成功: $message")
    }

    /**
     * 显示错误Toast
     * @param message 要显示的消息
     */
    fun showError(message: String) {
        // 目前使用普通Toast，后续可以自定义样式
        showShort("错误: $message")
    }

    /**
     * 显示警告Toast
     * @param message 要显示的消息
     */
    fun showWarning(message: String) {
        // 目前使用普通Toast，后续可以自定义样式
        showShort("警告: $message")
    }
}