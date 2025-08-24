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
import androidx.compose.runtime.getValue
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
    val listState = rememberLazyListState()
    val reposState by viewModel.commonUIState.collectAsState()
    LaunchedEffect(listState) {
        snapshotFlow {
            if (reposState is CommonUIState.Success) {
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                lastVisibleItem >= totalItems - 3 // 当用户接近底部时加载更多
            } else false
        }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore && reposState is CommonUIState.Success) {
                viewModel.onLoadMore()
            }
        }
    }
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    val isLogin by viewModel.isLogin.collectAsState()
    BaseScreen(
        viewModel = viewModel,
        title = "首页",
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
                        listState = listState,
                        isLoadingMore = data.isLoadingMore,
                        hasMoreData = data.hasMoreData,
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
    listState: LazyListState,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onItemClick: (owner: String, repo: String) -> Unit
) {
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