package com.madar.crewly.core.ui.foundation

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700), // Pure Gold
    onPrimary = Color(0xFF322C00),
    primaryContainer = Color(0xFF4B4200),
    onPrimaryContainer = Color(0xFFFFF199),
    secondary = Color(0xFFD4AF37), // Metallic Gold
    onSecondary = Color(0xFF3A3000),
    secondaryContainer = Color(0xFF554600),
    onSecondaryContainer = Color(0xFFFFE174),
    tertiary = Color(0xFFE5E1E6), // Platinum/Silver accent
    onTertiary = Color(0xFF303033),
    tertiaryContainer = Color(0xFF47474A),
    onTertiaryContainer = Color(0xFFF5F5F5),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF0F0E0D), // Deep Charcoal/Black
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF0F0E0D),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF4D4639),
    onSurfaceVariant = Color(0xFFD1C5B4),
    outline = Color(0xFF9A8F80),
    inverseOnSurface = Color(0xFF0F0E0D),
    inverseSurface = Color(0xFFE6E1E5),
    inversePrimary = Color(0xFFD4AF37),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF725C00), // Deep Gold
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE074),
    onPrimaryContainer = Color(0xFF231B00),
    secondary = Color(0xFF6B5D2E), // Muted Gold
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF5E1A7),
    onSecondaryContainer = Color(0xFF231B04),
    tertiary = Color(0xFF49654B), // Emerald/Sage accent
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCBF0C9),
    onTertiaryContainer = Color(0xFF06210C),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1E1B16),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1E1B16),
    surfaceVariant = Color(0xFFEBE2CF),
    onSurfaceVariant = Color(0xFF4C4639),
    outline = Color(0xFF7D7667),
    inverseOnSurface = Color(0xFFF7F0E7),
    inverseSurface = Color(0xFF33302A),
    inversePrimary = Color(0xFFEBC248),
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

