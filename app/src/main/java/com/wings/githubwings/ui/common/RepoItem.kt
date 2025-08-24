package com.wings.githubwings.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wings.githubwings.model.bean.GitHubRepo
import kotlin.random.Random

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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Repository name and owner information
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

            // Repository description
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

            // Bottom information bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language tag
                repo.language?.let {
                    LanguageBadge(language = it)
                }

                // Statistics - horizontally arranged, evenly distributed
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Star count
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

                    // Fork count
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

            // Update time
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Updated ${formatRelativeTime(repo.updated_at)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LanguageBadge(language: String) {
    val colorScheme = MaterialTheme.colorScheme
    // Generate a random background color
    val backgroundColor = remember(language, colorScheme) {
        val random = Random(language.hashCode())
        val colorList = listOf(
            colorScheme.primaryContainer,
            colorScheme.secondaryContainer,
            colorScheme.tertiaryContainer,
            colorScheme.errorContainer,
            colorScheme.primary.copy(alpha = 0.3f),
            colorScheme.secondary.copy(alpha = 0.3f),
            colorScheme.tertiary.copy(alpha = 0.3f),
            colorScheme.error.copy(alpha = 0.3f)
        )
        colorList[random.nextInt(colorList.size)]
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        val color = when (language.lowercase()) {
            "kotlin" -> colorScheme.primary
            "java" -> colorScheme.tertiary
            "python" -> colorScheme.secondary
            "javascript" -> colorScheme.error
            "typescript" -> colorScheme.primaryContainer
            "go" -> colorScheme.secondaryContainer
            "rust" -> colorScheme.tertiaryContainer
            else -> colorScheme.outline
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

// Format number display, e.g. 1000 -> 1k
fun formatCount(count: Int): String {
    return when {
        count >= 1000000 -> "${(count / 1000000f).format(1)}m"
        count >= 1000 -> "${(count / 1000f).format(1)}k"
        else -> count.toString()
    }
}

// Format relative time display
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
            days > 0 -> "${days} days ago"
            hours > 0 -> "${hours} hours ago"
            minutes > 0 -> "${minutes} minutes ago"
            else -> "just now"
        }
    } catch (e: Exception) {
        dateString
    }
}

// Extension function to format floating point numbers
fun Float.format(digits: Int) = java.math.BigDecimal(this.toString())
    .setScale(digits, java.math.RoundingMode.HALF_UP)
    .toDouble()
    .toString()