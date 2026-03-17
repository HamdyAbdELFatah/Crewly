package com.madar.crewly.feature.display

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.madar.crewly.core.common.ContentState
import com.madar.crewly.core.common.DisplayUiState
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.data.User
import com.madar.crewly.core.ui.atoms.AppTopBar
import com.madar.crewly.core.ui.foundation.AppDimens
import com.madar.crewly.core.ui.molecules.EmptyStateView
import com.madar.crewly.core.ui.molecules.ErrorView
import com.madar.crewly.core.ui.molecules.UserCard
import com.madar.crewly.core.ui.molecules.UserCardSkeleton
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    uiState: DisplayUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DisplayViewModel = koinViewModel()
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = UiText.StringResource(R.string.users_title),
                onBack = onBack,
                actions = {
                    if (uiState.userCount > 0) {
                        Badge(
                            modifier = Modifier.padding(end = AppDimens.spacingM)
                        ) {
                            Text("${uiState.userCount}")
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.contentState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "content_state"
        ) { contentState: ContentState ->
            when (contentState) {
                is ContentState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        repeat(3) {
                            UserCardSkeleton()
                        }
                    }
                }

                is ContentState.Empty -> {
                    EmptyStateView()
                }

                is ContentState.Error -> {
                    ErrorView(
                        message = contentState.message,
                        onRetry = { viewModel.load() }
                    )
                }

                is ContentState.Success -> {
                    @Suppress("UNCHECKED_CAST")
                    val userList = contentState.users as List<User>
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = { viewModel.load() },
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        UserList(users = userList)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserList(
    users: List<User>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = AppDimens.spacingS),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserCard(user = user)
        }
    }
}
