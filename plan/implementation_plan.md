# Crewly Audit — Implementation Plan (v2)

Fix all issues from the comprehensive engineering audit, with **package organization** as a first-class concern.

---

## Phase 0 — Package Organization

All modules except `core:data` have flat package structures. This phase restructures them into proper Clean Architecture sub-packages.

---

### `core:domain` — Before vs After

```
BEFORE (flat):                          AFTER (organized):
core/domain/                            core/domain/
├── DeleteUserUseCase.kt                ├── model/
├── GetAllUsersUseCase.kt               │   └── User.kt
├── GetUserByIdUseCase.kt               ├── repository/
├── GetUserCountUseCase.kt              │   └── UserRepository.kt
├── SaveUserUseCase.kt                  └── usecase/
├── UpdateUserUseCase.kt                    ├── DeleteUserUseCase.kt
├── User.kt                                 ├── GetAllUsersUseCase.kt
└── UserRepository.kt                       ├── GetUserByIdUseCase.kt
                                            ├── GetUserCountUseCase.kt
                                            ├── SaveUserUseCase.kt
                                            └── UpdateUserUseCase.kt
```

**Files to move & update package declarations:**

| File | Old Package | New Package |
|------|------------|-------------|
| `User.kt` | `core.domain` | `core.domain.model` |
| `UserRepository.kt` | `core.domain` | `core.domain.repository` |
| 6 × `*UseCase.kt` | `core.domain` | `core.domain.usecase` |

**Cascade**: All imports in `core:data`, `feature:input`, `feature:display`, `app`, and tests must be updated.

---

### `core:common` — Before vs After

```
BEFORE (flat):                          AFTER (organized):
core/common/                            core/common/
├── AppConstants.kt                     ├── constants/
├── BaseViewModel.kt                    │   └── AppConstants.kt
├── DispatcherProvider.kt               ├── base/
├── DisplayUiState.kt  ← DELETE         │   ├── BaseViewModel.kt
├── Extensions.kt                       │   ├── DispatcherProvider.kt
├── InputValidator.kt                   │   └── UiState.kt  (UiState + UiEvent interfaces)
├── Mapper.kt                           ├── mapper/
├── UiText.kt                           │   └── Mapper.kt
└── ValidationResult.kt                 ├── navigation/
                                        │   └── Routes.kt  (AppRoute, InputRoute, UsersRoute)
                                        ├── ui/
                                        │   └── UiText.kt
                                        └── validation/
                                            ├── InputValidator.kt
                                            └── ValidationResult.kt
```

**Key changes:**
- **DELETE** `DisplayUiState.kt` (feature state leaked into common)
- **DELETE** `Extensions.kt` — Move routes to `navigation/Routes.kt`, remove duplicated `isValidAge()`/`isValidName()`
- **Split** `BaseViewModel.kt` — Extract `UiState`/`UiEvent` interfaces into `base/UiState.kt`

---

### `feature:input` — Before vs After

```
BEFORE (flat):                          AFTER (organized):
feature/input/                          feature/input/
├── InputScreen.kt                      ├── ui/
├── InputUiState.kt                     │   ├── InputScreen.kt
└── InputViewModel.kt                   │   └── components/  (future)
                                        ├── state/
                                        │   ├── InputUiState.kt
                                        │   ├── InputUiEvent.kt
                                        │   └── UserFormEvent.kt
                                        └── InputViewModel.kt
```

**Key changes:**
- Split `InputUiState.kt` into separate files: `InputUiState`, `InputUiEvent`, `UserFormEvent`, `Gender` enum
- Screen composable goes into `ui/` sub-package

---

### `feature:display` — Before vs After

```
BEFORE (flat):                          AFTER (organized):
feature/display/                        feature/display/
├── DisplayViewModel.kt                 ├── ui/
├── UsersScreen.kt                      │   └── UsersScreen.kt
└── UsersUiState.kt                     ├── state/
                                        │   ├── UsersUiState.kt
                                        │   └── UsersUiEvent.kt
                                        └── DisplayViewModel.kt
```

---

### `core:data` — Already organized ✅

No changes needed.

---

## Phase 1 — Critical Fixes

### `DisplayViewModel.kt`
- Store observer `Job`, cancel before re-launching in `refresh()` to prevent coroutine leaks

### `build.gradle.kts` (feature:input & feature:display)
- Remove `implementation(project(":core:data"))` — Clean Architecture violation

### Unused imports cleanup
- `SaveUserUseCase.kt` — remove `UiText` import
- `InputViewModel.kt` — remove `mapList` import
- `UserRepositoryImpl.kt` — remove concrete mapper imports

---

## Phase 2 — Architecture Cleanup

### `BaseViewModel.kt`
- Fix `launchWithLoading` to manage a loading state callback, or remove dead code

---

## Phase 3 — State Management & Error Handling

### `InputScreen.kt`
- Add `AnimatedVisibility` error banner rendering `uiState.errorMessage`

### `InputViewModel.kt`
- In `loadUser()`, notify user when user not found instead of silently stopping

### `DisplayViewModel.kt`
- Add `.catch { }` to flow collection for graceful error recovery

---

## Phase 4 — Performance & UI Polish

### `AppButton.kt`
- Gate infinite animations behind `enabled && Primary && !isLoading`
- Cache `Paint` objects via `remember`

### `AppTextField.kt`
- Cache `Paint` objects via `remember`

### `EmptyStateView.kt`, `ErrorView.kt`
- Replace hardcoded dp with `AppDimens` tokens

### `UserCard.kt`
- Remove unused `onDelete` parameter

### `AppTheme.kt`
- Set `dynamicColor = false` default to enforce gold brand theme

---

## Phase 5 — Testing

### [NEW] `FakeUserRepository.kt`
- Shared test double replacing ~10 duplicate anonymous implementations

### [NEW] `GetUserCountUseCaseTest.kt`

### Update `DisplayViewModelTest.kt`, `InputViewModelTest.kt`
- Add missing test cases (delete, refresh, submit success, loadUser)

### Refactor all UseCase tests to use `FakeUserRepository`

---

## Phase 6 — Verification

```bash
# All unit tests pass:
.\gradlew test --continue

# Clean build with new package structure:
.\gradlew assembleDebug
```

### Manual
- Verify gold theme visible (not overridden by dynamic color)
- Verify pull-to-refresh doesn't cause jank
- Verify error banner appears on save failure in InputScreen
