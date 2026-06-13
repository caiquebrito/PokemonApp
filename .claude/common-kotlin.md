# CommonKotlin Module — Pure Kotlin Utilities

## Config

- **Package:** `com.ctb.commonkotlin`
- **Type:** JVM Library (`jvmToolchain(11)`)
- **Dependencies:** Kotlin stdlib, Coroutines, JUnit, MockK (no project dependencies)

## Structure

```
com.ctb.commonkotlin/
├── usecases/
│   ├── Result.kt            — Result sealed class (Success / Failure)
│   ├── UnknownException.kt
│   ├── CoroutinesTestRule.kt
│   └── FlowUseCase.kt       — Base class for Flow use cases
└── test/
    ├── TestExtension.kt
    └── TestFlowCollector.kt
```

## Rules

1. Pure Kotlin. **No Android dependencies** — usable by any layer.
2. Provides the `Result` wrapper, `FlowUseCase` base, and test helpers.
3. Use `Result.onSuccess` and `Result.onFailure` for handling use case outcomes.
4. `FlowUseCase` automatically wraps errors into `Result.Failure` via `catch`.
5. Always provide `CoroutineDispatcher` when instantiating a `FlowUseCase`.

## Code Patterns

### FlowUseCase Base Class

```kotlin
// com.ctb.commonkotlin.usecases/
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in TParam, out TResult>(
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(param: TParam? = null) =
        performAction(param)
            .catch { exception -> emit(Result.Failure(exception)) }
            .flowOn(dispatcher)

    // Produce a cold Flow<T>. Keep non-suspending so errors
    // during collection are caught by the upstream `catch`.
    protected abstract suspend fun performAction(param: TParam?): Flow<Result<TResult>>
}
```

### Result Usage

Use `Result.onSuccess` and `Result.onFailure` extension functions from `commonkotlin.usecases` for handling use case outcomes:

```kotlin
yourUseCase(YourUseCase.Params(id)).collect { result ->
    result.onSuccess { entity ->
        // handle success
    }
    result.onFailure { exception ->
        // handle error
    }
}
```
