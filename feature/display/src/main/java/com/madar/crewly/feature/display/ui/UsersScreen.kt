package com.madar.crewly.feature.display.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.R as CommonR
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.domain.model.User
import com.madar.crewly.core.ui.R as UiR
import com.madar.crewly.core.ui.atoms.AppTopBar
import com.madar.crewly.core.ui.foundation.AppDimens
import com.madar.crewly.core.ui.foundation.AppTheme
import com.madar.crewly.core.ui.molecules.EmptyStateView
import com.madar.crewly.core.ui.molecules.ErrorView
import com.madar.crewly.core.ui.molecules.UserCard
import com.madar.crewly.core.ui.molecules.UserCardSkeleton
import com.madar.crewly.feature.display.state.UsersContentState
import com.madar.crewly.feature.display.state.UsersUiEvent
import com.madar.crewly.feature.display.state.UsersUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    uiState: UsersUiState,
    onBack: () -> Unit,
    onUserClick: (Long) -> Unit,
    onUserDelete: (Long) -> Unit,
    onRefresh: () -> Unit,
    uiEvents: Flow<UsersUiEvent>,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        uiEvents.collectLatest { event ->
            when (event) {
                is UsersUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
                is UsersUiEvent.NavigateToEdit -> {
                    onUserClick(event.userId)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = UiText.StringResource(CommonR.string.users_title),
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.contentState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "content_state"
        ) { contentState ->
            when (contentState) {
                is UsersContentState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        repeat(3) {
                            UserCardSkeleton()
                        }
                    }
                }

                is UsersContentState.Empty -> {
                    EmptyStateView()
                }

                is UsersContentState.Error -> {
                    ErrorView(
                        message = contentState.message,
                        onRetry = onRefresh
                    )
                }

                is UsersContentState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = onRefresh,
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        UserList(
                            users = contentState.users,
                            onUserClick = onUserClick,
                            onUserDelete = onUserDelete
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserList(
    users: List<User>,
    onUserClick: (Long) -> Unit,
    onUserDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = AppDimens.spacingS),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(
            items = users,
            key = { _, user -> user.id }
        ) { index, user ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(user.id) {
                delay(index * 50L)
                isVisible = true
            }

            val scale by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0.8f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "item_scale"
            )

            val alpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "item_alpha"
            )

            SwipeToDismissUserCard(
                user = user,
                onClick = { onUserClick(user.id) },
                onDelete = { onUserDelete(user.id) },
                modifier = Modifier
                    .animateItem()
                    .scale(scale)
                    .alpha(alpha)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissUserCard(
    user: User,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.7f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            val progress = dismissState.progress
            val alpha = (progress * 2).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.error.copy(alpha = alpha * 0.3f)
                            )
                        )
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(UiR.string.delete),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(UiR.string.delete),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        UserCard(
            name = user.name,
            jobTitle = user.jobTitle,
            age = user.age,
            gender = user.gender,
            onClick = onClick
        )
    }
}
