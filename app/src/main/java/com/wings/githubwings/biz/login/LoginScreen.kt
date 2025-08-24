package com.wings.githubwings.biz.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wings.githubwings.ui.common.BaseScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wings.githubwings.MainViewModel
import com.wings.githubwings.R
import com.wings.githubwings.framework.log.FLog

@Composable
fun LoginScreen(
    mainViewModel: MainViewModel,
    viewModel: LoginViewModel = viewModel(),
    back: () -> Unit,
    gotoProfile: () -> Unit
) {
    val loginState by mainViewModel.authState.collectAsState()
    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            gotoProfile()
        }
    }
    val context = LocalContext.current
    BaseScreen(
        viewModel = viewModel,
        title = "登录",
        showAppBar = true,
        onBackClick = back,
        idleContent = {},
    ) { padding, _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // GitHub Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "GitHub Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome to GitHub Client",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in with your GitHub account to access your repositories, create issues, and more.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        FLog.debug("Login", Uri.parse(viewModel.getAuthUrl()).toString())
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getAuthUrl()))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("Sign in with GitHub")
                }
            }
        }

    }
}
