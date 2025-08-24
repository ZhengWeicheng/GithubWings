package com.wings.githubwings.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wings.githubwings.model.bean.GitHubRepo

@Composable
fun RepoItem(
    repo: GitHubRepo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 仓库名称和所有者信息
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = repo.owner.avatar_url,
                    contentDescription = "Owner avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = repo.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = repo.owner.login,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // 仓库描述
            repo.description?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 底部信息栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 语言标签
                repo.language?.let {
                    LanguageBadge(language = it)
                }

                // 统计信息 - 水平排列，平均分布
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 星标数
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Stars",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatCount(repo.stargazers_count),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Fork数
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Forks",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatCount(repo.forks_count),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // 更新时间
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "更新于 ${formatRelativeTime(repo.updated_at)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LanguageBadge(language: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        val color = when (language.lowercase()) {
            "kotlin" -> MaterialTheme.colorScheme.primary
            "java" -> MaterialTheme.colorScheme.tertiary
            "python" -> MaterialTheme.colorScheme.secondary
            "javascript" -> MaterialTheme.colorScheme.error
            "typescript" -> MaterialTheme.colorScheme.primaryContainer
            "go" -> MaterialTheme.colorScheme.secondaryContainer
            "rust" -> MaterialTheme.colorScheme.tertiaryContainer
            else -> MaterialTheme.colorScheme.outline
        }

        androidx.compose.foundation.Canvas(
            modifier = Modifier.size(12.dp)
        ) {
            drawCircle(color = color)
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = language,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

// 格式化数字显示，例如 1000 -> 1k
fun formatCount(count: Int): String {
    return when {
        count >= 1000000 -> "${(count / 1000000f).format(1)}m"
        count >= 1000 -> "${(count / 1000f).format(1)}k"
        else -> count.toString()
    }
}

// 格式化相对时间显示
fun formatRelativeTime(dateString: String): String {
    return try {
        val date =
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
                .parse(dateString)
        val now = java.util.Date()
        val diff = now.time - date!!.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        when {
            days > 0 -> "${days}天前"
            hours > 0 -> "${hours}小时前"
            minutes > 0 -> "${minutes}分钟前"
            else -> "刚刚"
        }
    } catch (e: Exception) {
        dateString
    }
}

// 扩展函数用于格式化浮点数
fun Float.format(digits: Int) = java.math.BigDecimal(this.toString())
    .setScale(digits, java.math.RoundingMode.HALF_UP)
    .toDouble()
    .toString()