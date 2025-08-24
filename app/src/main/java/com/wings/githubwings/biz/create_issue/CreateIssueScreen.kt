package com.wings.githubwings.biz.create_issue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wings.githubwings.ui.common.BaseScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateIssueScreen(
    viewModel: CreateIssueViewModel = viewModel(),
    owner: String,
    repo: String,
    back: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        title = "新建问题",
        showAppBar = true,
        onBackClick = back,
        idleContent = { padding ->
            EditIssueScreen(padding, owner, repo, viewModel)
        },
    ) { padding, data ->

    }
}

@Composable
fun EditIssueScreen(
    padding: PaddingValues,
    owner: String,
    repo: String,
    viewModel: CreateIssueViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text(text = "标题") },
                placeholder = { Text(text = "请输入问题标题") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.body,
                onValueChange = { viewModel.body = it },
                label = { Text(text = "内容") },
                placeholder = { Text(text = "请详细描述问题...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                supportingText = {
                    Text("请尽可能详细地描述问题，包括复现步骤、预期结果和实际结果")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 使用Box将按钮包装起来，并设置居中对齐
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { viewModel.createIssue(owner, repo) },
                    modifier = Modifier
                        .fillMaxWidth(0.33f) // 设置为屏幕宽度的三分之一
                        .height(48.dp), // 设置高度为48dp
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer, // 添加背景色
                    ),
                    shape = MaterialTheme.shapes.medium // 使用MaterialTheme的中等圆角
                ) {
                    Text(
                        text = "提交",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}