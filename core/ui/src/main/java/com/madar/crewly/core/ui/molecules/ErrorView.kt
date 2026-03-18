package com.madar.crewly.core.ui.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.madar.crewly.core.common.R
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.ui.atoms.AppButton
import com.madar.crewly.core.ui.atoms.ButtonType
import com.madar.crewly.core.ui.foundation.AppDimens

@Composable
fun ErrorView(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(AppDimens.iconSizeLarge),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(AppDimens.spacingM))

        Text(
            text = message.asString(androidx.compose.ui.platform.LocalContext.current),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppDimens.spacingL))

        AppButton(
            text = UiText.StringResource(R.string.try_again),
            onClick = onRetry,
            type = ButtonType.Secondary
        )
    }
}
