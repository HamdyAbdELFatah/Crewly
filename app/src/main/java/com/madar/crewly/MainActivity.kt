package com.madar.crewly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ExperimentalNavigation3Api
import com.madar.crewly.core.common.AppRoute
import com.madar.crewly.core.common.InputRoute
import com.madar.crewly.core.common.UiText
import com.madar.crewly.core.common.UsersRoute
import com.madar.crewly.core.ui.foundation.AppTheme
import com.madar.crewly.core.ui.foundation.ErrorBoundary
import com.madar.crewly.feature.display.DisplayViewModel
import com.madar.crewly.feature.display.UsersScreen
import com.madar.crewly.feature.input.InputScreen
import com.madar.crewly.feature.input.InputViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        var keepSplashScreen by mutableStateOf(true)
        
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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

@Composable
private fun SplashScreenContent(
    onTimeout: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(300)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(500)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }
    
    LaunchedEffect(Unit) {
        delay(800)
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
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
                painter = painterResource(id = com.madar.crewly.R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp),
                tint = Color.White
            )
        }

        Text(
            text = "Crewly",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .alpha(textAlpha.value),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalNavigation3Api::class)
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
            NavBackStack<AppRoute>().apply { add(InputRoute) }
        }

        NavDisplay(
            backStack = navBackStack,
            onBack = { navBackStack.removeLastOrNull() },
            entryProvider = { key: AppRoute ->
                when (key) {
                    is InputRoute -> NavEntry(key) {
                        val vm = koinViewModel<InputViewModel>()
                        val uiState by vm.uiState.collectAsStateWithLifecycle()

                        InputScreen(
                            uiState = uiState,
                            onEvent = vm::onEvent,
                            onNavigate = { navBackStack.add(UsersRoute) },
                            viewModel = vm
                        )
                    }

                    is UsersRoute -> NavEntry(key) {
                        val vm = koinViewModel<DisplayViewModel>()
                        val uiState by vm.uiState.collectAsStateWithLifecycle()

                        UsersScreen(
                            uiState = uiState,
                            onBack = { navBackStack.removeLastOrNull() },
                            viewModel = vm
                        )
                    }
                }
            }
        )
    }
}
