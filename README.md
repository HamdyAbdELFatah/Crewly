# Crewly

A production-grade multi-module Android application for user management built with modern Android development practices.

## Architecture

### Module Graph

```
:app (Application layer - MainActivity, Koin setup)
    ↓
:feature:input → :core:domain → :core:data
    ↓                   ↓           ↓
:feature:display ← :core:common ← :core:ui
```

### Modules

| Module | Purpose |
|--------|---------|
| `:app` | Application entry point, Koin DI setup, Navigation 3 |
| `:core:common` | BaseViewModel, UiState, UiEvent, Dispatchers, Validators |
| `:core:data` | Room 3.0 database, DAOs, Repository implementation |
| `:core:domain` | Use cases (SaveUser, GetAllUsers) |
| `:core:ui` | Design system (Foundation, Atoms, Molecules) |
| `:feature:input` | User input screen + ViewModel |
| `:feature:display` | Users list screen + ViewModel |

## Key Technologies

### Room 3.0

#### Why BundledSQLiteDriver is Required

Room 3.0 moved SQLite driver management out of the core library. Previously, Room bundled its own SQLite driver, but now you must explicitly include `androidx.sqlite:sqlite-bundled` and use `BundledSQLiteDriver()` when building your database:

```kotlin
Room.databaseBuilder<AppDatabase>(context, DB_NAME)
    .setDriver(BundledSQLiteDriver(Dispatchers.IO))
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
```

This change provides:
- Better control over SQLite driver versions
- Improved dependency management
- Support for custom SQLite implementations

#### withWriteTransaction vs runInTransaction

Room 3.0 removed `runInTransaction()` and replaced it with `withWriteTransaction()`:

```kotlin
// Room 3.0 (CORRECT)
roomDatabase.withWriteTransaction { transaction ->
    // perform writes within transaction
}
```

The new API is:
- Flow-based and suspend-friendly
- Better integrated with coroutines
- More type-safe

### Navigation 3

#### Backstack vs NavController

Navigation 3 (navigation-compose 1.0.1) uses a completely different paradigm from Navigation 2:

```kotlin
// Navigation 3 (CORRECT) - using NavBackStack
val backStack = rememberNavBackStack(InputRoute)

NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
        entry<InputRoute> { /* screen content */ }
    }
)
```

Key differences from NavController:
- No `NavHost` or `NavGraph`
- Routes implement `NavKey` + `@Serializable`
- Manual backstack management via `MutableList`
- Entry decorators for Scene, SavedState, ViewModelStore

### BaseViewModel Contract

The generic `BaseViewModel<S : UiState, E : UiEvent>` provides:

```kotlin
abstract class BaseViewModel<S : UiState, E : UiEvent>(
    protected val dispatchers: DispatcherProvider
) : ViewModel() {
    // Generic state flow with WhileSubscribed(5000)
    val uiState: StateFlow<S>
    
    // Loading state for UI feedback
    val isLoading: StateFlow<Boolean>
    
    // One-time events channel
    val uiEvent: Flow<E>
    
    // Helper functions
    protected fun updateState(reducer: S.() -> S)
    protected fun sendEvent(event: E)
    protected fun launch(block: suspend CoroutineScope.() -> Unit)
    protected fun launchWithLoading(block: suspend CoroutineScope.() -> Unit)
}
```

Subclass benefits:
- Zero boilerplate for state management
- Consistent error handling pattern
- Automatic loading state management
- Built-in coroutine dispatching

## Setup

### Prerequisites

- JDK 17+
- Android SDK 35
- Gradle 8.8+

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Run lint
./gradlew lint

# Release build (requires signing config)
./gradlew assembleRelease
```

### Environment Variables

For release builds, configure in GitHub Secrets:
- `KEYSTORE_BASE64`: Base64-encoded keystore
- `KEYSTORE_PASSWORD`: Keystore password
- `KEY_ALIAS`: Key alias
- `KEY_PASSWORD`: Key password

## CI/CD

### Workflows

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| CI | Push to main/develop, PR to main | Lint → Test → Build Debug |
| CD | Push to main | Build Release APK |

## Dependencies (Verified Versions)

All versions are defined in `gradle/libs.versions.toml`:

| Library | Version |
|---------|---------|
| Kotlin | 2.1.0 |
| AGP | 8.8.0 |
| Room 3.0 | 3.0.0-alpha01 |
| Navigation 3 | 1.0.1 |
| Koin | 4.0.2 |
| Compose BOM | 2025.02.00 |
| Lifecycle | 2.9.0 |

## Time Estimate

- Initial setup: ~2 hours
- Core common module: ~3 hours
- Data layer (Room 3.0): ~4 hours
- Domain layer: ~2 hours
- UI design system: ~4 hours
- Feature screens: ~4 hours
- CI/CD: ~1 hour

**Total: ~20 hours**

## License

MIT License
