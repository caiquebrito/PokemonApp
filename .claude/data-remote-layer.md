# Data Remote Layer

## Config

- **Package:** `com.ctb.data_remote`
- **Type:** JVM Library (`jvmToolchain(11)`)
- **Dependencies:** `:data`, `:domain` + Gson, Retrofit

## Structure

```
com.ctb.data_remote/
├── api/               — Retrofit service interfaces
├── repository/        — Remote data source implementations
├── request/           — Outgoing request DTOs (with @SerializedName)
└── response/          — Incoming response DTOs + toDomain() extensions
```

## Rules

1. Pure HTTP/mapping layer. No business logic, no repository contracts.
2. DTOs use `@SerializedName`. Response DTOs provide `toDomain()` extension functions.
3. Extension functions on primitives create request DTOs (e.g., `String.toYourRequest()`).
4. **JVM only — do not place Android code here.**

## Code Patterns

### Retrofit API

```kotlin
// com.ctb.data_remote.api/
import com.ctb.data_remote.request.YourRequest
import com.ctb.data_remote.response.YourResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface YourAPI {
    @POST("your/endpoint")
    suspend fun yourCall(
        @Body request: YourRequest,
    ): YourResponse
}
```

### Request DTO

```kotlin
// com.ctb.data_remote.request/
import com.google.gson.annotations.SerializedName

data class YourRequest(
    @SerializedName("id") val id: String,
)

fun String.toYourRequest() = YourRequest(id = this)
```

### Response DTO → Domain Mapping

```kotlin
// com.ctb.data_remote.response/
import com.ctb.domain.models.YourEntity
import com.google.gson.annotations.SerializedName

data class YourResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
)

fun YourResponse.toDomain() = YourEntity(
    id = id,
    name = name,
)
```

### Remote Data Source Implementation

```kotlin
// com.ctb.data_remote.repository/
import com.ctb.data.YourRemoteDataSource
import com.ctb.data_remote.api.YourAPI
import com.ctb.data_remote.request.toYourRequest
import com.ctb.data_remote.response.toDomain
import com.ctb.domain.models.YourEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class YourRemoteDataSourceImpl(
    val api: YourAPI,
) : YourRemoteDataSource {
    override fun yourOperation(id: String): Flow<YourEntity> =
        flow {
            emit(
                api.yourCall(request = id.toYourRequest()).toDomain(),
            )
        }.catch {
            // handle customized errorBody if needed
            throw it
        }
}
```
