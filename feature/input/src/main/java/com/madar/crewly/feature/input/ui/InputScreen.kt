package com.madar.crewly.feature.input.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.constants.AppConstants
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.common.validation.Field
import com.madar.crewly.core.ui.atoms.AppButton
import com.madar.crewly.core.ui.atoms.AppChipGroup
import com.madar.crewly.core.ui.atoms.AppTextField
import com.madar.crewly.core.ui.atoms.AppTopBar
import com.madar.crewly.core.ui.atoms.ButtonType
import com.madar.crewly.core.ui.foundation.AppDimens
import com.madar.crewly.feature.input.state.Gender
import com.madar.crewly.feature.input.state.InputUiEvent
import com.madar.crewly.feature.input.state.InputUiState
import com.madar.crewly.feature.input.state.UserFormEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InputScreen(
    uiState: InputUiState,
    onEvent: (UserFormEvent) -> Unit,
    onNavigate: () -> Unit,
    onBack: () -> Unit,
    onNavigateAndClear: () -> Unit,
    uiEvents: Flow<InputUiEvent>,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        uiEvents.collectLatest { event ->
            when (event) {
                is InputUiEvent.NavigateToUsers -> onNavigate()
                is InputUiEvent.NavigateToUsersAndClear -> onNavigateAndClear()
                is InputUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
                is InputUiEvent.NavigateBack -> onBack()
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            AppTopBar(
                title = if (uiState.isEditMode) {
                    UiText.StringResource(R.string.input_title_edit)
                } else {
                    UiText.StringResource(R.string.input_title_add)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.navigationBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.spacingM)
            ) {
                uiState.errorMessage?.let { error ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = androidx.compose.material3.MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(AppDimens.spacingM)
                    ) {
                        Text(
                            text = error.asString(context),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onErrorContainer,
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = if (uiState.errorMessage != null) 80.dp else AppDimens.spacingM,
                        start = AppDimens.spacingM,
                        end = AppDimens.spacingM,
                        bottom = AppDimens.spacingM
                    )
                    .imePadding()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.4f),
                                    Color.White.copy(alpha = 0.05f)
                                )
                            ),
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(AppDimens.spacingL)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
                    ) {
                        AnimatedColumnItem(index = 0) {
                            AppTextField(
                                value = uiState.name,
                                onValueChange = { onEvent(UserFormEvent.NameChanged(it)) },
                                error = uiState.fieldErrors[Field.NAME],
                                maxLength = AppConstants.NAME_MAX_LENGTH,
                                labelText = stringResource(R.string.name_label),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        AnimatedColumnItem(index = 1) {
                            AppTextField(
                                value = uiState.age,
                                onValueChange = { onEvent(UserFormEvent.AgeChanged(it)) },
                                error = uiState.fieldErrors[Field.AGE],
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                labelText = stringResource(R.string.age_label),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        AnimatedColumnItem(index = 2) {
                            AppTextField(
                                value = uiState.jobTitle,
                                onValueChange = { onEvent(UserFormEvent.JobChanged(it)) },
                                error = uiState.fieldErrors[Field.JOB_TITLE],
                                maxLength = AppConstants.JOB_MAX_LENGTH,
                                labelText = stringResource(R.string.job_label),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        AnimatedColumnItem(index = 3) {
                            Column {
                                AppChipGroup(
                                    options = Gender.entries,
                                    selectedOption = uiState.gender?.let { gender ->
                                        Gender.entries.find { it.displayName == gender }
                                    },
                                    onOptionSelected = { onEvent(UserFormEvent.GenderChanged(it.displayName)) },
                                    labelOf = { it.displayName },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                AnimatedVisibility(
                                    visible = uiState.fieldErrors[Field.GENDER] != null,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    uiState.fieldErrors[Field.GENDER]?.let { error ->
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = error.asString(context),
                                            color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(AppDimens.spacingM))

                        AnimatedColumnItem(index = 4) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.spacingS)
                            ) {
                                AppButton(
                                    text = if (uiState.isEditMode) {
                                        UiText.StringResource(R.string.update_button)
                                    } else {
                                        UiText.StringResource(R.string.save_button)
                                    },
                                    onClick = { onEvent(UserFormEvent.Submit) },
                                    isLoading = uiState.isLoading,
                                    enabled = uiState.fieldsValid,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (uiState.isEditMode) {
                                    AppButton(
                                        text = UiText.StringResource(R.string.cancel_button),
                                        onClick = { onEvent(UserFormEvent.Cancel) },
                                        type = ButtonType.Secondary,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedColumnItem(
    index: Int,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 80L)
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(400),
        label = "alpha"
    )
    val offsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .alpha(alpha)
            .offset(y = offsetY)
    ) {
        content()
    }
}
