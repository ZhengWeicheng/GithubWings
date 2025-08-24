package com.wings.githubwings.framework.navgation

import com.wings.githubwings.framework.utils.ToastUtil

/**
 * Navigation通用扩展函数
 */

/**
 * 显示成功Toast
 */
fun showSuccessToast(message: String) {
    ToastUtil.showSuccess(message)
}

/**
 * 显示错误Toast
 */
fun showErrorToast(message: String) {
    ToastUtil.showError(message)
}

/**
 * 显示警告Toast
 */
fun showWarningToast(message: String) {
    ToastUtil.showWarning(message)
}

/**
 * 显示普通Toast
 */
fun showToast(message: String) {
    ToastUtil.showShort(message)
}