package com.madar.crewly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.madar.crewly.core.common.navigation.AppRoute
import com.madar.crewly.core.common.navigation.InputRoute
import com.madar.crewly.core.common.navigation.UsersRoute
import com.madar.crewly.core.common.ui.UiText
import com.madar.crewly.core.ui.foundation.AppTheme
import com.madar.crewly.core.ui.foundation.ErrorBoundary
import com.madar.crewly.feature.display.DisplayViewModel
import com.madar.crewly.feature.display.ui.UsersScreen
import com.madar.crewly.feature.input.InputViewModel
import com.madar.crewly.feature.input.ui.InputScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private object SplashConfig {
    const val SPLASH_DELAY_MS = 2500L
    const val SCALE_ANIM_DELAY_MS = 300L
    const val SCALE_ANIM_DURATION_MS = 500
    const val ALPHA_ANIM_DELAY_MS = 500L
    const val ALPHA_ANIM_DURATION_MS = 500
    const val TEXT_ALPHA_ANIM_DELAY_MS = 800L
    const val TEXT_ALPHA_ANIM_DURATION_MS = 400
    const val LOGO_SIZE_DP = 160
    const val TITLE_FONT_SIZE = 36
    const val TITLE_BOTTOM_PADDING_DP = 100
    const val ANIMATION_PHASE_DURATION = 20000
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        var keepSplashScreen by mutableStateOf(true)
        
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        FluidBackground()
                        
                        if (keepSplashScreen) {
                            SplashScreenContent {
                                keepSplashScreen = false
                            }
                        } else {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FluidBackground() {
    val transition = rememberInfiniteTransition(label = "mesh")
    val rawPhase by transition.animateFloat(
        initialValue = 0f, 
        targetValue = 2f * Math.PI.toFloat(), 
        animationSpec = infiniteRepeatable(tween(SplashConfig.ANIMATION_PHASE_DURATION, easing = LinearEasing)),
        label = "phase"
    )
    
    val color1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    val color2 = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(width * (0.5f + 0.5f * kotlin.math.sin(rawPhase)), height * 0.2f),
                radius = width * 1.2f
            )
        )
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(width * (0.5f + 0.5f * kotlin.math.cos(rawPhase)), height * 0.8f),
                radius = width * 1.2f
            )
        )
    }
}

@Composable
private fun SplashScreenContent(
    onTimeout: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(SplashConfig.SCALE_ANIM_DELAY_MS)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = SplashConfig.SCALE_ANIM_DURATION_MS,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(SplashConfig.ALPHA_ANIM_DELAY_MS)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = SplashConfig.ALPHA_ANIM_DURATION_MS,
                easing = FastOutSlowInEasing
            )
        )
    }
    
    LaunchedEffect(Unit) {
        delay(SplashConfig.TEXT_ALPHA_ANIM_DELAY_MS)
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = SplashConfig.TEXT_ALPHA_ANIM_DURATION_MS,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(SplashConfig.SPLASH_DELAY_MS)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(com.madar.crewly.core.ui.R.drawable.ic_app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(SplashConfig.LOGO_SIZE_DP.dp),
                tint = Color.Unspecified
            )
        }

        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = SplashConfig.TITLE_BOTTOM_PADDING_DP.dp)
                .alpha(textAlpha.value),
            fontSize = SplashConfig.TITLE_FONT_SIZE.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun MainScreen() {
    var error by remember { mutableStateOf<UiText?>(null) }

    if (error != null) {
        ErrorBoundary(
            message = error!!,
            onRetry = { error = null }
        )
    } else {
        val navBackStack = remember {
            NavBackStack<AppRoute>().apply { add(InputRoute()) }
        }

        NavDisplay(
            backStack = navBackStack,
            onBack = { navBackStack.removeLastOrNull() },
            entryProvider = { key: AppRoute ->
                when (key) {
                    is InputRoute -> NavEntry(key) {
                        val vm = koinViewModel<InputViewModel>()
                        val uiState by vm.uiState.collectAsStateWithLifecycle()

                        LaunchedEffect(key.userId) {
                            vm.resetState()
                            if (key.userId > 0) {
                                vm.loadUser(key.userId)
                            }
                        }

                        InputScreen(
                            uiState = uiState,
                            onEvent = vm::onEvent,
                            onNavigate = { navBackStack.add(UsersRoute) },
                            onBack = { navBackStack.removeLastOrNull() },
                            uiEvents = vm.uiEvent,
                            onNavigateAndClear = {
                                navBackStack.clear()
                                navBackStack.add(InputRoute())
                                navBackStack.add(UsersRoute)
                            }
                        )
                    }

                    is UsersRoute -> NavEntry(key) {
                        val vm = koinViewModel<DisplayViewModel>()
                        val uiState by vm.uiState.collectAsStateWithLifecycle()

                        UsersScreen(
                            uiState = uiState,
                            onBack = { navBackStack.removeLastOrNull() },
                            onUserClick = { userId ->
                                navBackStack.add(InputRoute(userId))
                            },
                            onUserDelete = { userId ->
                                vm.deleteUser(userId)
                            },
                            onRefresh = vm::refresh,
                            uiEvents = vm.uiEvent
                        )
                    }
                }
            }
        )
    }
}
