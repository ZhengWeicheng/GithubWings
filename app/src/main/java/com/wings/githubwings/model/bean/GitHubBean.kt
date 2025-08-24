package com.wings.githubwings.model.bean

data class GitHubRepo(
    val id: Long,
    val name: String,
    val full_name: String,
    val owner: User,
    val html_url: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    val created_at: String,
    val updated_at: String,
    val pushed_at: String,
    val homepage: String?,
    val size: Int,
    val stargazers_count: Int,
    val watchers_count: Int,
    val language: String?,
    val forks_count: Int,
    val open_issues_count: Int,
    val topics: List<String>?,
    val default_branch: String
)

data class User(
    val login: String,
    val id: Long,
    val avatar_url: String,
    val url: String,
    val html_url: String,
    val repos_url: String,
    val type: String
)

data class RepoSearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GitHubRepo>
)

data class IssueReq(
    val title: String,
    val body: String,
    val labels: List<String>? = null
)

data class IssueResp(
    val id: Long,
    val number: Int,
    val title: String,
    val user: User,
    val state: String,
    val created_at: String,
    val body: String?
)