package com.wings.githubwings.framework.navgation

import android.os.Bundle
import androidx.navigation.NavController

// 定义导航目的地
object MainDestinations {
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
    const val SETTINGS_ROUTE = "settings"
    const val DETAIL_ROUTE = "detail"
    const val DETAIL_OWNER = "owner"
    const val DETAIL_REPO = "repo"
    const val SCREEN_INTENT = "screen_data"
    const val DETAIL_ROUTE_WITH_ARG = "$DETAIL_ROUTE/{$DETAIL_OWNER}/{$DETAIL_REPO}"
    const val CREATE_ISSUE_ROUTE = "create_issue"
    const val CREATE_ISSUE_ROUTE_WITH_ARG = "$CREATE_ISSUE_ROUTE/{$DETAIL_OWNER}/{$DETAIL_REPO}"
    const val LOGIN_ROUTE = "login"
    const val SEARCH_ROUTE = "search"
}

// 简化导航并传递数据的扩展函数
fun NavController.navigateTo(
    route: String,
    data: Bundle? = null
) {
    if (data != null) {
        this.currentBackStackEntry?.arguments?.putBundle(MainDestinations.SCREEN_INTENT, data)
    }
    this.navigate(route)
}