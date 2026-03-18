package com.madar.crewly.core.ui.atoms

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.draw.drawBehind
import com.madar.crewly.core.common.ui.UiText


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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowWidth by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Reverse),
        label = "glow_width"
    )

    val auraRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "aura_radius"
    )

    val isPopulated = value.isNotEmpty() || isFocused
    val labelOffset by animateDpAsState(if (isPopulated) (-12).dp else 0.dp, spring())
    val labelTextSize by animateFloatAsState(if (isPopulated) 12f else 16f, spring())
    val labelAlpha by animateFloatAsState(if (isPopulated) 0.8f else 0.5f, spring())

    val primaryColor = MaterialTheme.colorScheme.primary

    val auraPaint = remember(primaryColor, auraRadius) {
        androidx.compose.ui.graphics.Paint().apply {
            color = primaryColor.copy(alpha = 0.3f)
            asFrameworkPaint().apply {
                setShadowLayer(
                    auraRadius,
                    0f,
                    0f,
                    primaryColor.copy(alpha = 0.5f).toArgb()
                )
            }
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .drawBehind {
                    if (isFocused) {
                        drawContext.canvas.apply {
                            save()
                            drawRoundRect(
                                0f, 0f, size.width, size.height,
                                16.dp.toPx(), 16.dp.toPx(),
                                auraPaint
                            )
                            restore()
                        }
                    }
                }
                .graphicsLayer {
                    shape = RoundedCornerShape(16.dp)
                    clip = true
                }
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .border(
                    width = if (isFocused) glowWidth.dp else 1.dp,
                    brush = if (isFocused) {
                        Brush.sweepGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    } else if (error != null) {
                        SolidColor(MaterialTheme.colorScheme.error)
                    } else {
                        SolidColor(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = labelText,
                fontSize = labelTextSize.sp,
                color = if (error != null) MaterialTheme.colorScheme.error 
                        else if (isFocused) MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .offset(y = labelOffset)
                    .graphicsLayer { alpha = labelAlpha }
            )
            
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    val trimmedValue = if (maxLength != null && newValue.length > maxLength) {
                        newValue.take(maxLength)
                    } else {
                        newValue
                    }
                    onValueChange(trimmedValue)
                },
                interactionSource = interactionSource,
                enabled = enabled,
                keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
                visualTransformation = visualTransformation,
                singleLine = singleLine,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(bottom = 12.dp, top = 24.dp)
            )
        }

        if (error != null) {
            Text(
                text = error.asString(context),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 6.dp)
            )
        }
    }
}
