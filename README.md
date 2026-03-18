# Crewly 🚀

<div align="center">

![Android](https://img.shields.io/badge/Android-36-success?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-purple?style=flat-square&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Compose-BOM%202025.02.00-blue?style=flat-square&logo=jetpackcompose)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-green?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-orange?style=flat-square)

**A production-grade multi-module Android application showcasing modern Android development practices with Clean Architecture, Jetpack Compose, and Material Design 3.**

</div>

---

## 🎬 Demo

| Video | APK |
|:---:|:---:|
| 🎬 **[Watch App Demo](https://drive.google.com/file/d/1I_6jyHnRwp_xz7s00e4aawWhczDqrbAV/view?usp=sharing)** | 📥 **[Download APK](https://drive.google.com/file/d/1kBHdZf1FuatsdVKdk8Dyy__J4i0JqTFY/view?usp=sharing)** |

---

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                         :app                                │
│              MainActivity • Koin DI • Navigation            │
└─────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  feature:input  │  │ feature:display │  │    core:ui      │
│   Add/Edit UI   │  │   List UI       │  │  Design System   │
└────────┬────────┘  └────────┬────────┘  └─────────────────┘
         │                   │
         └─────────┬─────────┘
                   ▼
┌─────────────────────────────────────────────────────────────┐
│                        :core:domain                          │
│     Models • Repository Interfaces • Use Cases               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                         :core:data                           │
│            Room 3.0 • DAOs • Repository Impl                 │
└─────────────────────────────────────────────────────────────┘
```

### Module Breakdown

| Module | Layer | Description |
|--------|-------|-------------|
| `:app` | Presentation | Application entry, Koin DI, Navigation 3 setup |
| `:feature:input` | Presentation | User input form (Add/Edit) with ViewModel |
| `:feature:display` | Presentation | Users list with pull-to-refresh, swipe-to-delete |
| `:core:ui` | Presentation | Reusable design system (Atoms, Molecules) |
| `:core:domain` | Domain | Business logic, use cases, repository interfaces |
| `:core:data` | Data | Room database, repository implementation |
| `:core:common` | Common | Shared utilities, BaseViewModel, validators |

### Package Structure

```
core/domain/
├── model/          # User data class
├── repository/     # UserRepository interface
└── usecase/       # SaveUser, GetAllUsers, GetUserById, etc.

core/common/
├── base/           # BaseViewModel, UiState, UiEvent
├── constants/      # AppConstants
├── mapper/         # Mapper<T, R>
├── navigation/     # AppRoute, InputRoute, UsersRoute
├── ui/             # UiText (serializable)
└── validation/     # InputValidator, ValidationResult

feature/input/
├── ui/             # InputScreen composable
└── state/          # InputUiState, InputUiEvent, UserFormEvent

feature/display/
├── ui/             # UsersScreen composable
└── state/          # UsersUiState, UsersUiEvent, UsersContentState
```

---

## ✨ Features

### Core Functionality
- ✅ **Add User** — Name, Age, Job Title, Gender (Male/Female)
- ✅ **Edit User** — Tap card to modify existing user
- ✅ **Delete User** — Swipe-to-delete with 70% threshold
- ✅ **User List** — Pull-to-refresh, staggered animations

### Design System
- 🎨 **Gold Brand Theme** — Material Design 3 with custom gold palette
- 🔮 **Glass Morphism** — Semi-transparent cards with blur effects
- ✨ **Shimmer Animation** — Loading states on primary buttons
- 🌟 **Glow Effects** — Focus states on text fields
- 🎭 **3D Touch** — Card press animations with rotation
- 🔄 **Smooth Transitions** — Screen navigation animations

### Navigation Flow
```
Add Mode:     Add User → Users List → Back → Add User → Exit
Edit Mode:    Users List → Tap User → Edit → Save → Users List → Exit
```

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 2.1.0 |
| **UI** | Jetpack Compose BOM 2025.02.00 |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Koin 4.0.2 |
| **Database** | Room 3.0.0-alpha01 |
| **Navigation** | Navigation 3 (androidx.navigation3) |
| **State Management** | StateFlow + SharedFlow |
| **Async** | Kotlin Coroutines + Flow |
| **Build** | Gradle 9.3.1 + AGP 8.8.0 |

---

## 🔑 Key Implementation Details

### Room 3.0 with BundledSQLiteDriver

```kotlin
Room.databaseBuilder<AppDatabase>(context, DB_NAME)
    .setDriver(BundledSQLiteDriver(Dispatchers.IO))
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
```

### Navigation 3 with NavBackStack

```kotlin
val navBackStack = remember {
    NavBackStack<AppRoute>().apply { add(InputRoute()) }
}

NavDisplay(
    backStack = navBackStack,
    onBack = { navBackStack.removeLastOrNull() },
    entryProvider = { key: AppRoute ->
        when (key) {
            is InputRoute -> NavEntry(key) { InputScreen(...) }
            is UsersRoute -> NavEntry(key) { UsersScreen(...) }
        }
    }
)
```

### BaseViewModel Pattern

```kotlin
abstract class BaseViewModel<S : UiState, E : UiEvent>(
    protected val dispatchers: DispatcherProvider
) : ViewModel() {
    abstract fun initialState(): S
    
    val uiState: StateFlow<S>
    val uiEvent: SharedFlow<E>
    
    protected fun updateState(reducer: S.() -> S)
    protected fun sendEvent(event: E)
    protected fun launch(block: suspend CoroutineScope.() -> Unit)
}
```

---

## 🏃 Build & Run

### Prerequisites
- JDK 17+
- Android SDK 36
- Gradle 9.3+

### Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Build with no daemon (Windows fix)
./gradlew assembleDebug --no-daemon

# Clean build
./gradlew clean assembleDebug
```

### Output
APK location: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📁 Project Structure

```
Crewly/
├── app/                      # Application module
│   └── src/main/
│       ├── java/com/madar/crewly/
│       │   ├── MainActivity.kt
│       │   └── di/AppModules.kt
│       └── res/
├── core/
│   ├── common/              # Shared utilities
│   ├── data/                 # Data layer (Room)
│   ├── domain/               # Business logic
│   └── ui/                   # Design system
├── feature/
│   ├── display/              # Users list feature
│   └── input/                # Add/Edit user feature
├── gradle/                   # Gradle wrapper
└── plan/                     # Implementation plan
```

---

## 📄 License

MIT License — see [CHANGELOG.md](CHANGELOG.md) for version history.

---

<div align="center">

**Built with ❤️ using Modern Android Development**

</div>
