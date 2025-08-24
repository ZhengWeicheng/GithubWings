package com.wings.githubwings

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.wings.githubwings.navigate.AppNavGraph
import com.wings.githubwings.ui.theme.GithubDemoTheme
import com.wings.githubwings.framework.log.FLog

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置状态栏透明
        enableEdgeToEdge()
        // 设置状态栏透明
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        setContent {
            GithubDemoTheme {
                Surface {
                    AppNavGraph(mainViewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "github" && uri.host == "oauth") {
                val code = uri.getQueryParameter("code")
                if (code != null) {
                    FLog.debug("MainActivity", "Auth code received: $code")
                    mainViewModel.handleGithubAuthCode(code)
                } else {
                    val error = uri.getQueryParameter("error")
                    FLog.error("MainActivity", "Auth error: $error")
                }
            }
        }
    }
}

