package com.wings.githubwings.biz.create_issue

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.wings.githubwings.framework.network.base.NetworkResult
import com.wings.githubwings.framework.utils.ToastUtil.showError
import com.wings.githubwings.framework.utils.ToastUtil.showSuccess
import com.wings.githubwings.framework.viewmodel.CommonUIState
import com.wings.githubwings.framework.viewmodel.NavigationViewModel
import com.wings.githubwings.model.bean.IssueReq
import com.wings.githubwings.model.bean.IssueResp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CreateIssueViewModel : NavigationViewModel<CreateIssueRepository, IssueResp>() {
    var title by mutableStateOf("")

    var body by mutableStateOf("")

    fun createIssue(owner: String, repo: String) {
        // 检查标题和内容是否为空
        if (title.isBlank()) {
            showError("标题不能为空")
            return
        }

        if (body.isBlank()) {
            showError("内容不能为空")
            return
        }

        viewModelScope.launch {
            val issue = IssueReq(title = title, body = body)
            safeRequest(
                block = {
                    repository!!.createIssue(owner = owner, repo = repo, issue = issue)
                }).collectLatest { result ->
                _commonUIState.value = when (result) {
                    is NetworkResult.Success -> {
                        showSuccess("Issue创建成功")
                        CommonUIState.Idle
                    }

                    is NetworkResult.Error -> {
                        showError(result.message ?: "创建失败")
                        CommonUIState.Idle
                    }

                    is NetworkResult.Loading -> {
                        CommonUIState.Idle
                    }

                    is NetworkResult.Idle -> {
                        CommonUIState.Idle
                    }
                }
            }
        }
    }

    override fun createRepository(): CreateIssueRepository {
        return CreateIssueRepository()
    }
}