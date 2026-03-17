package com.madar.crewly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.madar.crewly.core.common.AppRoute
import com.madar.crewly.core.common.InputRoute
import com.madar.crewly.core.common.UsersRoute
import com.madar.crewly.core.ui.foundation.AppTheme
import com.madar.crewly.feature.display.DisplayViewModel
import com.madar.crewly.feature.display.UsersScreen
import com.madar.crewly.feature.input.InputScreen
import com.madar.crewly.feature.input.InputViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
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
