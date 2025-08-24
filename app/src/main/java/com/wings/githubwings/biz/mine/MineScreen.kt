package com.wings.githubwings.biz.mine

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wings.githubwings.ui.common.BaseScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.wings.githubwings.MainViewModel
import com.wings.githubwings.model.bean.User
import com.wings.githubwings.ui.common.RepoItem

@Composable
fun MineScreen(
    viewModel: MineViewModel = viewModel(),
    mainViewModel: MainViewModel,
    back: () -> Unit,
    goToLogin: () -> Unit,
    goToRepoDetail: (owner: String, repo: String) -> Unit,

    ) {
    val showLogoutDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    ShowLogoutDialog(showLogoutDialog, goToLogin, mainViewModel)
    BaseScreen(
        viewModel = viewModel,
        title = "Profile",
        showAppBar = true,
        onBackClick = back,
        actions = {
            IconButton(onClick = {
                showLogoutDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout"
                )
            }
        }
    ) { padding, data ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val mineContent = data as MineUIContent
            val user = mineContent.user!!
            item {
                UserInfoLayout(
                    user = user,
                    onOpenGitHub = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.html_url))
                        context.startActivity(intent)
                    }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                Text(
                    text = "Repositories",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            val repositories = mineContent.repositoriesList
            items(
                count = repositories.size,
                key = { index -> repositories[index].id }
            ) { index ->
                val repo = repositories[index]
                RepoItem(
                    repo = repo,
                    onClick = {
                        goToRepoDetail(
                            repo.owner.login,
                            repo.name
                        )
                    }
                )
            }

            // Load more repositories
            if (mineContent.hasMoreRepos) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        LaunchedEffect(Unit) {
                            viewModel.loadMore()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLogoutDialog(
    showLogoutDialog: MutableState<Boolean>,
    goToLogin: () -> Unit,
    mainViewModel: MainViewModel
) {
    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout from your GitHub account?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog.value = false
                        mainViewModel.logout()
                        goToLogin()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun UserInfoLayout(
    user: User,
    onOpenGitHub: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatar_url,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.login,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = onOpenGitHub) {
            Icon(
//                imageVector = Icons.Default.OpenInBrowser,
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Open in GitHub"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("View on GitHub")
        }
    }
}