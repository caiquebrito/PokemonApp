# Data Layer

## Config

- **Package:** `com.ctb.data`
- **Type:** JVM Library (`jvmToolchain(11)`)
- **Dependencies:** `:domain`, `:commonKotlin` + Kotlin stdlib, Coroutines

## Structure

```
com.ctb.data/
├── YourRepositoryImpl.kt    — Repository implementations
└── YourRemoteDataSource.kt  — Data source interfaces (implemented in :data-remote)
```

## Rules

1. Bridges data sources to domain. Implements `:domain` repository interfaces.
2. Defines data source interfaces that `:data-remote` implements.
3. **No Android dependencies, no Retrofit directly** (that's `:data-remote`).
4. Keep repositories thin — delegate to data sources, don't add business logic.

## Code Patterns

### Remote Data Source Interface

```kotlin
// com.ctb.data/
import com.ctb.domain.models.YourEntity
import kotlinx.coroutines.flow.Flow

interface YourRemoteDataSource {
    fun yourOperation(id: String): Flow<YourEntity>
}
```

### Repository Implementation

```kotlin
// com.ctb.data/
import com.ctb.domain.repositories.YourRepository
import com.ctb.domain.models.YourEntity
import kotlinx.coroutines.flow.Flow

class YourRepositoryImpl(
    private val remoteDataSource: YourRemoteDataSource
) : YourRepository {
    override fun yourOperation(id: String): Flow<YourEntity> =
        remoteDataSource.yourOperation(id)
}
```
