package com.wings.githubwings.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wings.githubwings.framework.navgation.MainDestinations
import com.wings.githubwings.MainViewModel
import com.wings.githubwings.biz.create_issue.CreateIssueScreen
import com.wings.githubwings.biz.detail.DetailScreen
import com.wings.githubwings.biz.home.HomeScreen
import com.wings.githubwings.biz.login.LoginScreen
import com.wings.githubwings.biz.mine.MineScreen
import com.wings.githubwings.biz.search.NewSearchScreen

@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {
    val isLogin = mainViewModel.authState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 首页
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                goToRepositoryDetails = { owner, repo ->
                    navController.navigate("${MainDestinations.DETAIL_ROUTE}/$owner/$repo")
                },
                gotoSearch = {
                    navController.navigate(MainDestinations.SEARCH_ROUTE)
                },
                goToProfile = {
                    if (isLogin.value) {
                        navController.navigate(MainDestinations.PROFILE_ROUTE)
                    } else {
                        navController.navigate(MainDestinations.LOGIN_ROUTE)
                    }
                }
            )
        }
        // 详情
        composable(
            MainDestinations.DETAIL_ROUTE_WITH_ARG,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            DetailScreen(
                owner = owner,
                repo = repo,
                goToCreateIssue = { owner, repo ->
                    navController.navigate(
                        "${MainDestinations.CREATE_ISSUE_ROUTE}/$owner/$repo"
                    )
                },
                back = {
                    navController.popBackStack()
                })
        }
        // 创建issue
        composable(
            MainDestinations.CREATE_ISSUE_ROUTE_WITH_ARG, arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            CreateIssueScreen(
                owner = owner,
                repo = repo,
                back = {
                    navController.popBackStack()
                })
        }
        // 登录
        composable(MainDestinations.LOGIN_ROUTE) {
            LoginScreen(
                mainViewModel = mainViewModel,
                back = {
                    navController.popBackStack()
                },
                gotoProfile = {
//                    navController.navigate(MainDestinations.PROFILE_ROUTE)
                    // 在跳转到登录界面之前先退出当前用户
                    navController.navigate(MainDestinations.PROFILE_ROUTE) {
                        // 清除返回栈，确保用户退出后无法通过返回键回到个人资料页
                        popUpTo(MainDestinations.HOME_ROUTE) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        // 个人资料页
        composable(MainDestinations.PROFILE_ROUTE) {
            MineScreen(mainViewModel = mainViewModel,
                back = { navController.popBackStack() },
                goToLogin = {
                    // 在跳转到登录界面之前先退出当前用户
                    navController.navigate(MainDestinations.LOGIN_ROUTE) {
                        // 清除返回栈，确保用户退出后无法通过返回键回到个人资料页
                        popUpTo(MainDestinations.HOME_ROUTE) {
                            inclusive = false
                        }
                    }
                },
                goToRepoDetail = { owner, repo ->
                    navController.navigate(
                        "${MainDestinations.DETAIL_ROUTE}/$owner/$repo"
                    )
                }
            )
        }
        // 搜索界面
        composable(MainDestinations.SEARCH_ROUTE) {
            NewSearchScreen(
                back = {
                    navController.popBackStack()
                },
                goToRepoDetail = { owner, repo ->
                    navController.navigate(
                        "${MainDestinations.DETAIL_ROUTE}/$owner/$repo"
                    )
                }
            )
        }
    }
}

