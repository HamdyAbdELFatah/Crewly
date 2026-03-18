package com.madar.crewly.core.ui.foundation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.R as CommonR
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.ui.R as UiR
import com.madar.crewly.core.ui.atoms.AppButton

@Composable
fun ErrorBoundary(
    message: UiText,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.spacingL),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(UiR.string.error_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(AppDimens.spacingM))

            Text(
                text = message.asString(context),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(AppDimens.spacingL))

                AppButton(
                    text = UiText.StringResource(UiR.string.retry_button),
                    onClick = onRetry,
                    modifier = Modifier.padding(horizontal = AppDimens.spacingL)
                )
            }
        }
    }
}
