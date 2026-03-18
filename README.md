# Crewly

A production-grade multi-module Android application for user management built with modern Android development practices.

## Demo

### Video Demo
🎬 **[Watch App Demo on Google Drive](https://drive.google.com/file/d/1I_6jyHnRwp_xz7s00e4aawWhczDqrbAV/view?usp=sharing)**

### Download APK
📥 **[Download Crewly Debug APK](https://drive.google.com/file/d/1kBHdZf1FuatsdVKdk8Dyy__J4i0JqTFY/view?usp=sharing)**

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
| `:core:common` | Base classes, Constants, Validators, Navigation routes |
| `:core:data` | Room 3.0 database, DAOs, Repository implementation |
| `:core:domain` | Domain models, Repository interfaces, Use cases |
| `:core:ui` | Design system (Foundation, Atoms, Molecules) |
| `:feature:input` | User input screen (Add/Edit) + ViewModel |
| `:feature:display` | Users list screen + ViewModel with pull-to-refresh |

### Package Structure

```
core/domain/
  ├── model/          # User data class
  ├── repository/     # UserRepository interface
  └── usecase/        # SaveUser, GetAllUsers, GetUserById, UpdateUser, DeleteUser, GetUserCount

core/common/
  ├── base/           # BaseViewModel, UiState, UiEvent, DispatcherProvider
  ├── constants/      # AppConstants
  ├── mapper/         # Mapper interface
  ├── navigation/     # AppRoute, InputRoute, UsersRoute
  ├── ui/             # UiText
  └── validation/     # InputValidator, ValidationResult, Field

feature/input/
  ├── ui/             # InputScreen composable
  └── state/          # InputUiState, InputUiEvent, UserFormEvent, Gender

feature/display/
  ├── ui/             # UsersScreen composable
  └── state/          # UsersUiState, UsersUiEvent, UsersContentState
```

## Features

### App Preview

| Splash Screen | Add User | User List |
|:---:|:---:|:---:|
| ![Splash](docs/screenshots/splash.png) | ![Add](docs/screenshots/add_user.png) | ![List](docs/screenshots/user_list.png) |

| Edit User | Swipe to Delete |
|:---:|:---:|
| ![Edit](docs/screenshots/edit_user.png) | ![Delete](docs/screenshots/swipe_delete.png) |

### Core Functionality
- **Add User**: Name, Age, Job Title, Gender (Male/Female)
- **Edit User**: Tap user card to edit existing user
- **Delete User**: Swipe-to-delete with visual feedback
- **User List**: Pull-to-refresh, animated entry effects

### Design System
- **Gold brand theme** with dynamic color disabled
- **Glass morphism** cards with 3D touch effects
- **Shimmer animations** on primary buttons
- **Custom text fields** with glow effects on focus
- **Smooth transitions** between screens

### Navigation Flow
- **Add Mode**: Add User → Navigate to List → Back to Add → Exit
- **Edit Mode**: List → Tap User → Edit → Save → Clear backstack to List → Exit

## Key Technologies

### Room 3.0

#### Why BundledSQLiteDriver is Required

Room 3.0 moved SQLite driver management out of the core library. You must explicitly include `androidx.sqlite:sqlite-bundled`:

```kotlin
Room.databaseBuilder<AppDatabase>(context, DB_NAME)
    .setDriver(BundledSQLiteDriver(Dispatchers.IO))
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
```

### Navigation 3

```kotlin
// Navigation 3 using NavBackStack
val navBackStack = remember {
    NavBackStack<AppRoute>().apply { add(InputRoute()) }
}

NavDisplay(
    backStack = navBackStack,
    onBack = { navBackStack.removeLastOrNull() },
    entryProvider = { key: AppRoute ->
        when (key) {
            is InputRoute -> NavEntry(key) { /* InputScreen */ }
            is UsersRoute -> NavEntry(key) { /* UsersScreen */ }
        }
    }
)
```

Key points:
- Routes implement `NavKey` + `@Serializable`
- Manual backstack management via `MutableList`
- Special handling for edit mode to clear backstack

### BaseViewModel Contract

```kotlin
abstract class BaseViewModel<S : UiState, E : UiEvent>(
    protected val dispatchers: DispatcherProvider
) : ViewModel() {
    val uiState: StateFlow<S>
    val uiEvent: Flow<E>
    
    protected fun updateState(reducer: S.() -> S)
    protected fun sendEvent(event: E)
    protected fun launch(block: suspend CoroutineScope.() -> Unit)
}
```

## Setup

### Prerequisites

- JDK 17+
- Android SDK 36
- Gradle 9.3+

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Build with no daemon (solves file lock issues)
./gradlew assembleDebug --no-daemon
```

## Dependencies

All versions defined in `gradle/libs.versions.toml`:

| Library | Version |
|---------|---------|
| Kotlin | 2.1.0 |
| AGP | 8.8.0 |
| Room | 3.0.0-alpha01 |
| Navigation 3 | 1.0.1 |
| Koin | 4.0.2 |
| Compose BOM | 2025.02.00 |
| Lifecycle | 2.9.0 |

## License

MIT License
