package com.madar.crewly.core.ui.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.ui.foundation.AppDimens

@Composable
fun UserCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.spacingM, vertical = AppDimens.spacingS),
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimens.cardElevation),
        shape = RoundedCornerShape(AppDimens.cardRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.cardPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmer()
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmer()
                )
            }
        }
    }
}

@Composable
private fun Modifier.shimmer(): Modifier {
    return this
        .background(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.6f),
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 0.6f)
                )
            )
        )
}
