package com.wings.githubwings.biz.search

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.ui.common.RepoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSearchScreen(
    viewModel: SearchViewModel = viewModel(),
    back: () -> Unit,
    goToRepoDetail: (owner: String, repo: String) -> Unit
) {
    val uiState by viewModel.commonUIState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()
    val primaryColor = MaterialTheme.colorScheme.primary
    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = !isDarkTheme
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("搜索", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // 搜索输入框
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    label = { Text("输入仓库名称") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.searchRepositories(searchQuery, 1, false)
                            localFocusManager.clearFocus()
                        }
                    ),
                    singleLine = true
                )

                // 搜索结果
                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {
                    is CommonUIState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("请输入关键字搜索仓库")
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

                    is CommonUIState.Success<*> -> {
                        val searchContent = state.data as SearchUIContent
                        val repositories = searchContent.repositoriesList

                        Text(
                            text = "共找到 ${searchContent.totalCount} 个仓库",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val listState = rememberLazyListState()
                        // 监听列表滚动，实现上拉加载更多
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                // 确保列表不为空且有更多数据时才计算
                                if (repositories.isEmpty() || !searchContent.hasMoreRepos) {
                                    false
                                } else {
                                    val layoutInfo = listState.layoutInfo
                                    val totalItemsNumber = layoutInfo.totalItemsCount
                                    val lastVisibleItemIndex =
                                        (layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                            ?: 0) + 10 // 提前10个item开始加载

                                    lastVisibleItemIndex >= totalItemsNumber && totalItemsNumber > 0
                                }
                            }
                        }

                        LaunchedEffect(shouldLoadMore.value) {
                            // 添加额外的保护条件
                            if (shouldLoadMore.value &&
                                searchContent.hasMoreRepos &&
                                !searchContent.isLoadingMore &&
                                repositories.isNotEmpty() // 确保列表不为空
                            ) {
                                viewModel.loadMore()
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {
                            // 确保列表不为空时才渲染items
                            if (repositories.isNotEmpty()) {
                                items(
                                    items = repositories,
                                    key = { repo -> repo.id }
                                ) { repo ->
                                    RepoItem(
                                        repo = repo,
                                        onClick = {
                                            goToRepoDetail(
                                                repo.owner.login,
                                                repo.name
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            if (searchContent.hasMoreRepos) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }

                    is CommonUIState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("加载失败：${state.message}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        })
}