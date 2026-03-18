package com.madar.crewly.core.ui.molecules

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.madar.crewly.core.ui.foundation.AppDimens

@Composable
fun UserCard(
    name: String,
    jobTitle: String,
    age: Int,
    gender: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    var touchX by remember { mutableFloatStateOf(0.5f) }
    var touchY by remember { mutableFloatStateOf(0.5f) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val rotationX by animateFloatAsState(
        targetValue = if (isPressed) (touchY - 0.5f) * 15f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "rotX"
    )
    
    val rotationY by animateFloatAsState(
        targetValue = if (isPressed) -(touchX - 0.5f) * 20f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "rotY"
    )

    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.spacingM, vertical = AppDimens.spacingS)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.rotationX = rotationX
                this.rotationY = rotationY
                shadowElevation = if (isPressed) 6.dp.toPx() else 14.dp.toPx()
                shape = RoundedCornerShape(24.dp)
                clip = true
            }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .drawWithContent {
                drawContent()
                // Dynamic Glass Highlight (Refraction)
                if (isPressed) {
                    val highlightBrush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        center = Offset(touchX * size.width, touchY * size.height),
                        radius = size.width * 0.8f
                    )
                    drawRect(
                        brush = highlightBrush,
                        blendMode = BlendMode.Overlay
                    )
                }
            }
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        Color.Transparent,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val startX = offset.x
                        val startY = offset.y
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isPressed = true
                        touchX = offset.x / size.width
                        touchY = offset.y / size.height
                        
                        val released = tryAwaitRelease()
                        isPressed = false
                        
                        if (released) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            val deltaX = kotlin.math.abs(offset.x - startX)
                            val deltaY = kotlin.math.abs(offset.y - startY)
                            if (deltaX < 10 && deltaY < 10) {
                                onClick?.invoke()
                            }
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.spacingL)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = jobTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppDimens.spacingM),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.chipSpacing)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text("$age years") },
                    shape = RoundedCornerShape(12.dp)
                )
                AssistChip(
                    onClick = { },
                    label = { Text(gender) },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
