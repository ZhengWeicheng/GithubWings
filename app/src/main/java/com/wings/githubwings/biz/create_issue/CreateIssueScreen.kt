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
        title = "New Issue",
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
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Please enter the issue title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.body,
                onValueChange = { viewModel.body = it },
                label = { Text(text = "Content") },
                placeholder = { Text(text = "Please describe the issue in detail...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                supportingText = {
                    Text("Please describe the issue in as much detail as possible, including reproduction steps, expected results and actual results")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Wrap the button with Box and set center alignment
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { viewModel.createIssue(owner, repo) },
                    modifier = Modifier
                        .fillMaxWidth(0.33f) // Set to one third of the screen width
                        .height(48.dp), // Set height to 48dp
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer, // Add background color
                    ),
                    shape = MaterialTheme.shapes.medium // Use medium rounded corners from MaterialTheme
                ) {
                    Text(
                        text = "Submit",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}