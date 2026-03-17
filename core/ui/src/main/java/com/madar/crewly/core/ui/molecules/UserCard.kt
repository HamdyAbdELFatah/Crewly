package com.madar.crewly.core.ui.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.data.User
import com.madar.crewly.core.ui.foundation.AppDimens

@Composable
fun UserCard(
    user: User,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.spacingM, vertical = AppDimens.spacingS),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = AppDimens.cardElevation
        ),
        shape = RoundedCornerShape(AppDimens.cardRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.cardPadding)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = user.jobTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppDimens.spacingS),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.chipSpacing)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text("${user.age} years") }
                )
                AssistChip(
                    onClick = { },
                    label = { Text(user.gender) }
                )
            }
        }
    }
}
