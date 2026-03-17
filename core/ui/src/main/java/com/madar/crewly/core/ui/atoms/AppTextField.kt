package com.madar.crewly.core.ui.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.common.UiText

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                val trimmedValue = if (maxLength != null && newValue.length > maxLength) {
                    newValue.take(maxLength)
                } else {
                    newValue
                }
                onValueChange(trimmedValue)
            },
            label = { Text(labelText) },
            isError = error != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            keyboardOptions = keyboardOptions.copy(
                imeAction = imeAction,
                keyboardType = keyboardOptions.keyboardType
            ),
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.bodyLarge
        )

        if (error != null) {
            Text(
                text = error.asString(context),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
