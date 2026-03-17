package com.madar.crewly.feature.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.Field
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.ui.atoms.AppButton
import com.madar.crewly.core.ui.atoms.AppChipGroup
import com.madar.crewly.core.ui.atoms.AppTextField
import com.madar.crewly.core.ui.atoms.AppTopBar
import com.madar.crewly.core.ui.foundation.AppDimens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InputScreen(
    uiState: InputUiState,
    onEvent: (UserFormEvent) -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InputViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is InputUiEvent.NavigateToUsers -> onNavigate()
                is InputUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        event.message.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = UiText.StringResource(R.string.input_title)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppDimens.spacingM)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(AppDimens.spacingM)
        ) {
            AppTextField(
                value = uiState.name,
                onValueChange = { onEvent(UserFormEvent.NameChanged(it)) },
                error = uiState.fieldErrors[Field.NAME],
                maxLength = com.madar.crewly.core.common.AppConstants.NAME_MAX_LENGTH,
                labelText = stringResource(R.string.name_label),
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = uiState.age,
                onValueChange = { onEvent(UserFormEvent.AgeChanged(it)) },
                error = uiState.fieldErrors[Field.AGE],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                labelText = stringResource(R.string.age_label),
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = uiState.jobTitle,
                onValueChange = { onEvent(UserFormEvent.JobChanged(it)) },
                error = uiState.fieldErrors[Field.JOB_TITLE],
                maxLength = com.madar.crewly.core.common.AppConstants.JOB_MAX_LENGTH,
                labelText = stringResource(R.string.job_label),
                modifier = Modifier.fillMaxWidth()
            )

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

                uiState.fieldErrors[Field.GENDER]?.let { error ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = error.asString(context),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimens.spacingM))

            AppButton(
                text = UiText.StringResource(R.string.save_button),
                onClick = { onEvent(UserFormEvent.Submit) },
                isLoading = uiState.isLoading,
                enabled = uiState.fieldsValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
