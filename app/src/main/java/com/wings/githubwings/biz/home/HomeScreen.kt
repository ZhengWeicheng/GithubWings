package com.wings.githubwings.biz.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import com.wings.githubwings.ui.common.BaseScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.ui.common.RepoItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    goToRepositoryDetails: (owner: String, repo: String) -> Unit,
    goToProfile: () -> Unit,
    gotoSearch: () -> Unit,
) {
    val reposState by viewModel.commonUIState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    val isLogin by viewModel.isLogin.collectAsState()
    BaseScreen(
        viewModel = viewModel,
        title = "Recommend",
        showAppBar = true,
        navigation = {
            IconButton(onClick = { goToProfile() }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = if (isLogin) "login" else "profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = {
                gotoSearch()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "refresh",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    ) { padding, data ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {
            when (data) {
                is HomeUIContent -> {
                    RepoList(
                        repositories = data.repositoriesList,
                        isLoadingMore = data.isLoadingMore,
                        hasMoreData = data.hasMoreData,
                        uiContext = data,
                        viewModel = viewModel,
                    ) { owner, repo ->
                        goToRepositoryDetails(owner, repo)
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun RepoList(
    repositories: List<GitHubRepo>,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    uiContext: HomeUIContent,
    viewModel: HomeViewModel,
    onItemClick: (owner: String, repo: String) -> Unit
) {
    val listState = rememberLazyListState()
    // 监听列表滚动，实现上拉加载更多
    val shouldLoadMore = remember {
        derivedStateOf {
            // 确保列表不为空且有更多数据时才计算
            if (repositories.isEmpty() || !uiContext.hasMoreData) {
                false
            } else {
                val layoutInfo = listState.layoutInfo
                val totalItemsNumber = layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    (layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: 0) + 10 // 提前10个item开始加载

                totalItemsNumber in 1..lastVisibleItemIndex
            }
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        // 添加额外的保护条件
        if (shouldLoadMore.value &&
            uiContext.hasMoreData &&
            !uiContext.isLoadingMore &&
            repositories.isNotEmpty() // 确保列表不为空
        ) {
            viewModel.onLoadMore()
        }
    }
    if (repositories.isNotEmpty()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                count = repositories.size,
                key = { index -> repositories[index].id }
            ) { index ->
                val repo = repositories[index]
                RepoItem(
                    repo = repo,
                    onClick = {
                        onItemClick(
                            repo.owner.login,
                            repo.name
                        )
                    }
                )
            }


            if (hasMoreData && isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier.run {
                            fillMaxWidth()
                                .padding(16.dp)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}