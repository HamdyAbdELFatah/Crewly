package com.madar.crewly.core.ui.atoms

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.ui.foundation.AppDimens

@Composable
fun <T> AppChipGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    labelOf: (T) -> String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.chipSpacing)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                label = { Text(labelOf(option)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
