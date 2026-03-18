package com.madar.crewly.core.ui.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madar.crewly.core.common.ui.UiText
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.toArgb
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
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "button_scale"
    )

    val primaryGradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    val disabledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    val containerBackground = when {
        !enabled -> Modifier.background(disabledColor)
        type == ButtonType.Primary -> Modifier.background(primaryGradient)
        type == ButtonType.Secondary -> Modifier.background(Color.Transparent)
        type == ButtonType.Destructive -> Modifier.background(MaterialTheme.colorScheme.error)
        else -> Modifier.background(primaryGradient)
    }

    val contentColor = when {
        !enabled -> disabledContentColor
        type == ButtonType.Primary -> MaterialTheme.colorScheme.onPrimary
        type == ButtonType.Secondary -> MaterialTheme.colorScheme.primary
        type == ButtonType.Destructive -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.onPrimary
    }

    val borderModifier = if (type == ButtonType.Secondary && enabled) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
    } else if (type == ButtonType.Secondary && !enabled) {
        Modifier.border(2.dp, disabledColor, RoundedCornerShape(16.dp))
    } else {
        Modifier
    }

    // Shimmer and Pulse animations
    val infiniteTransition = rememberInfiniteTransition(label = "button_infinite")
    
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shadowRadius by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shadow_radius"
    )

    val shadowColor = if (type == ButtonType.Primary) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent

    val shadowPaint = remember(shadowColor, shadowRadius) {
        androidx.compose.ui.graphics.Paint().apply {
            color = shadowColor
            asFrameworkPaint().apply {
                setShadowLayer(
                    shadowRadius,
                    0f,
                    8f,
                    shadowColor.toArgb()
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(if (enabled && !isLoading) scale else 1f)
            .drawWithContent {
                // Draw drop shadow manually for better performance and color control
                if (enabled && type == ButtonType.Primary && !isLoading) {
                    drawContext.canvas.apply {
                        save()
                        drawRoundRect(
                            0f, 0f, size.width, size.height, 
                            16.dp.toPx(), 16.dp.toPx(), 
                            shadowPaint
                        )
                        restore()
                    }
                }
                drawContent()
            }
            .clip(RoundedCornerShape(16.dp))
            .then(borderModifier)
            .then(containerBackground)
            .drawWithContent {
                drawContent()
                // Draw shimmering sweep overlay
                if (enabled && type == ButtonType.Primary && !isLoading) {
                    val shimmerBrush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        start = Offset(shimmerTranslate, -shimmerTranslate),
                        end = Offset(shimmerTranslate + 400f, shimmerTranslate + 400f)
                    )
                    drawRect(
                        brush = shimmerBrush,
                        blendMode = BlendMode.Overlay
                    )
                }
            }
            .pointerInput(enabled && !isLoading) {
                if (!enabled || isLoading) return@pointerInput
                detectTapGestures(
                    onPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isPressed = true
                        tryAwaitRelease()
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isPressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.animation.AnimatedContent(
            targetState = isLoading,
            label = "button_loading_state"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = contentColor,
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    text = text.asString(context),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        fontSize = 16.sp
                    ),
                    color = contentColor
                )
            }
        }
    }
}
