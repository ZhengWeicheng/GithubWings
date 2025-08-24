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
    // Set status bar color
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
                title = { Text("Search", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
                // Search input field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    label = { Text("Enter repository name") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
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

                // Search results
                Spacer(modifier = Modifier.height(16.dp))

                when (val state = uiState) {
                    is CommonUIState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Please enter keywords to search for repositories")
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
                            text = "Found ${searchContent.totalCount} repositories",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val listState = rememberLazyListState()
                        // Listen to list scrolling to implement pull-up to load more
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                // Make sure the list is not empty and there is more data before calculating
                                if (repositories.isEmpty() || !searchContent.hasMoreRepos) {
                                    false
                                } else {
                                    val layoutInfo = listState.layoutInfo
                                    val totalItemsNumber = layoutInfo.totalItemsCount
                                    val lastVisibleItemIndex =
                                        (layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                            ?: 0) + 10 // Start loading 10 items in advance

                                    lastVisibleItemIndex >= totalItemsNumber && totalItemsNumber > 0
                                }
                            }
                        }

                        LaunchedEffect(shouldLoadMore.value) {
                            // Add extra protection conditions
                            if (shouldLoadMore.value &&
                                searchContent.hasMoreRepos &&
                                !searchContent.isLoadingMore &&
                                repositories.isNotEmpty() // Ensure the list is not empty
                            ) {
                                viewModel.loadMore()
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {
                            // Render items only when the list is not empty
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
                            Text("Load failed: ${state.message}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        })
}