# Domain Layer

## Config

- **Package:** `com.ctb.domain`
- **Type:** JVM Library (`jvmToolchain(11)`)
- **Dependencies:** `:commonKotlin` + Kotlin stdlib, Coroutines

## Structure

```
com.ctb.domain/
├── repositories/     — Repository interface contracts
├── models/           — Domain entities (pure data classes)
└── usecase/          — Use case implementations (extend FlowUseCase)
```

## Rules

1. **Zero external dependencies**: no Retrofit, no Android, no Compose, no frameworks.
2. Defines *what* the app does. No implementation details.
3. Repository **interfaces** live here; implementations in `:data`.
4. Use cases extend `FlowUseCase` from `:commonKotlin`.
5. Each use case receives a repository interface + `CoroutineDispatcher`.
6. Use case params should be inner data classes (e.g., `ParamShortenerUrl`).
7. Domain models are pure data classes — no behavior, no side effects.

## Code Patterns

### Domain Entity

```kotlin
// com.ctb.domain.models/
data class YourEntity(
    val id: String,
    val name: String,
)
```

### Repository Interface

```kotlin
// com.ctb.domain.repositories/
import com.ctb.domain.models.YourEntity
import kotlinx.coroutines.flow.Flow

interface YourRepository {
    fun yourOperation(param: String): Flow<YourEntity>
}
```

### Use Case Implementation

```kotlin
// com.ctb.domain.usecase/
import com.ctb.commonkotlin.usecases.FlowUseCase
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.repositories.YourRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class YourUseCase(
    private val repository: YourRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<YourUseCase.Params, YourEntity>(dispatcher) {

    override suspend fun performAction(param: Params?): Flow<Result<YourEntity>> =
        param?.let { params ->
            repository.yourOperation(params.id).map { entity ->
                Result.Success(entity)
            }
        } ?: throw IllegalArgumentException()

    data class Params(
        val id: String,
    )
}
```
