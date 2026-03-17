package com.madar.crewly.core.ui.atoms

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.ui.foundation.AppDimens

enum class ButtonType {
    Primary, Secondary, Destructive
}

@Composable
fun AppButton(
    text: UiText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    type: ButtonType = ButtonType.Primary
) {
    val buttonModifier = modifier.height(AppDimens.buttonHeight)

    when (type) {
        ButtonType.Primary -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = text.asString(androidx.compose.ui.platform.LocalContext.current),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        ButtonType.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = text.asString(androidx.compose.ui.platform.LocalContext.current),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        ButtonType.Destructive -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onError,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = text.asString(androidx.compose.ui.platform.LocalContext.current),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
