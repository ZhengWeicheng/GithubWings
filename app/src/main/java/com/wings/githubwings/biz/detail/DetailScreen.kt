package com.wings.githubwings.biz.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.wings.githubwings.ui.common.BaseScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.wings.githubwings.model.bean.GitHubRepo
import com.wings.githubwings.ui.common.LanguageBadge
import kotlin.random.Random

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = viewModel(),
    owner: String,
    repo: String,
    goToCreateIssue: (owner: String, repo: String) -> Unit,
    back: () -> Unit,
) {
    LaunchedEffect(key1 = owner, key2 = repo) {
        viewModel.getRepoDetail(owner, repo)
    }
    val context = LocalContext.current
    val reposState by viewModel.commonUIState.collectAsState()
    BaseScreen(
        viewModel = viewModel,
        title = "仓库详情",
        showAppBar = true,
        onBackClick = back
    ) { padding, data ->
        RepositoryDetailsContent(
            repository = data as GitHubRepo,
            padding = padding,
            onOpenInBrowser = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.html_url))
                context.startActivity(intent)
            },
            onAddIssue = {
                goToCreateIssue(owner, repo) // 传递addIssue回调
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepositoryDetailsContent(
    repository: GitHubRepo,
    padding: PaddingValues,
    onOpenInBrowser: () -> Unit,
    onAddIssue: () -> Unit = {}, // 添加onAddIssue参数
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(16.dp),
    ) {
        // Repository header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = repository.owner.avatar_url,
                contentDescription = "Owner avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "by ${repository.owner.login}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        repository.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Stats
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 将Row改为使用SpaceBetween排列，使标题在左侧，按钮在右侧
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Repository Stats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // 添加Issue按钮
                    Button(
                        onClick = onAddIssue, // 使用专门的onAddIssue回调
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Add Issue",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Issue",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Default.Star,
                        label = "Stars",
                        value = repository.stargazers_count.toString()
                    )

                    StatItem(
                        icon = Icons.Default.Send,
                        label = "Forks",
                        value = repository.fork.toString()
                    )

                    StatItem(
                        icon = Icons.Default.Edit,
                        label = "Issues",
                        value = repository.open_issues_count.toString()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language: ",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            if (repository.language != null) {
                LanguageBadge(language = repository.language)
            } else {
                Text(
                    text = "Not specified",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Topics
        if (!repository.topics.isNullOrEmpty()) {
            Text(
                text = "Topics:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repository.topics.forEach { topic ->
                    SuggestionChip(
                        onClick = { /* No action */ },
                        label = { Text(topic) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = getRandomMaterialColor()
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //TODO Open in browser button
        Button(
            onClick = onOpenInBrowser,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Open in browser"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "View on GitHub")
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// 获取随机Material颜色的辅助函数
@Composable
fun getRandomMaterialColor(): androidx.compose.ui.graphics.Color {
    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.errorContainer,
        MaterialTheme.colorScheme.surfaceVariant
    )

    return colors[Random.nextInt(colors.size)]
}
