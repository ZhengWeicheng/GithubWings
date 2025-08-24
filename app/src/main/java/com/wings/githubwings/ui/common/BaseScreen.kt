package com.wings.githubwings.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : NavigationViewModel<*, *>> BaseScreen(
    viewModel: T,
    title: String = "",
    showAppBar: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    topContent: @Composable() ((PaddingValues) -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    navigation: @Composable() (() -> Unit)? = null,
    idleContent: @Composable() ((PaddingValues) -> Unit)? = null,
    content: @Composable (PaddingValues, Any?) -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val primaryColor = MaterialTheme.colorScheme.primary
    val reposState by viewModel.commonUIState.collectAsState()

    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = !isDarkTheme
        )
    }

    Scaffold(
        topBar = {
            if (showAppBar) {
                CenterAlignedTopAppBar(
                    title = { Text(title, style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        if (navigation != null) {
                            navigation()
                        } else {
                            if (onBackClick != null) {
                                IconButton(onClick = onBackClick) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "返回",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }

                    },
                    actions = actions
                )
            }
        },
        content = { padding ->
            if (topContent != null) {
                topContent(padding)
            }
            when (val result = reposState) {
                is CommonUIState.Idle -> {
                    if (idleContent != null) {
                        idleContent(padding)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("等待数据加载...")
                        }
                    }
                }

                is CommonUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CommonUIState.Success -> {
                    content(padding, result.data)
                }

                is CommonUIState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(16.dp))
                            Text("加载失败：${result.message}")
                            Button(onClick = { viewModel.retry() }) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        }
    )
}
