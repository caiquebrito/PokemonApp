# Presentation Layer

## Config

- **Package:** `com.ctb.presentation`
- **Type:** Android Library (Compose)
- **Dependencies:** `:design`, `:common`, `:commonKotlin`, `:domain` + Koin (Compose), Navigation Compose, Material3

## Structure per Feature

```
com.ctb.presentation/<feature>/
├── FeatureScreen.kt        — Compose screen composable
├── FeatureContent.kt       — Content/layout composable
├── FeatureRoute.kt         — Navigation route definition
└── viewmodel/
    ├── FeatureViewModel.kt
    ├── FeatureState.kt
    └── FeatureEffect.kt
```

## Rules

1. ViewModels inject **use cases** (never repositories directly).
2. MVI pattern: ViewModel exposes `State` (via `StateFlow`) and `Effect` (via `SharedFlow`).
3. **`collectAsEffect` always uses `when`, never `if`.**
4. No feature imports another feature. Cross-feature communication through `:main`.
5. `MainActivity` hosts the NavHost and wires routes.
6. Use `koinViewModel()` for ViewModel injection in composables.
7. Routes are `@Serializable` objects (type-safe navigation).

## Code Patterns

### MVI State

```kotlin
// com.ctb.presentation/<feature>/viewmodel/
import com.ctb.common.viewmodel.flow.UIState
import com.ctb.domain.models.YourEntity

data class YourState(
    val isLoading: Boolean = false,
    val items: List<YourEntity> = emptyList(),
    val error: String = "",
) : UIState
```

### MVI Effect

```kotlin
// com.ctb.presentation/<feature>/viewmodel/
import com.ctb.common.viewmodel.flow.UIEffect

sealed class YourEffect : UIEffect {
    object BackPressed : YourEffect()

    data class ShowError(
        val message: String,
    ) : YourEffect()
}
```

### MVI ViewModel

```kotlin
// com.ctb.presentation/<feature>/viewmodel/
import androidx.lifecycle.viewModelScope
import com.ctb.common.ui.ResourceProvider
import com.ctb.common.viewmodel.flow.ViewModel
import com.ctb.commonkotlin.usecases.onFailure
import com.ctb.commonkotlin.usecases.onSuccess
import com.ctb.domain.usecase.YourUseCase
import kotlinx.coroutines.launch

class YourViewModel(
    private val yourUseCase: YourUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel<YourState, YourEffect>(YourState()) {

    fun onCreate() {
        // initialize any metrics here, like analytics
    }

    fun performOperation(id: String) {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }

            yourUseCase(YourUseCase.Params(id)).collect { result ->
                result.onSuccess { entity ->
                    setState { it.copy(items = it.items + entity) }
                }

                result.onFailure {
                    sendEffect(
                        YourEffect.ShowError(
                            message =
                                it.message
                                    ?: resourceProvider.getString(resId = R.string.error_message),
                        ),
                    )
                }
            }

            setState { it.copy(isLoading = false) }
        }
    }
}
```

### Feature Route (Navigation)

```kotlin
// com.ctb.presentation/<feature>/
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object YourRoute

fun NavController.openYourFeature() {
    navigate(YourRoute)
}

fun NavGraphBuilder.yourFeature(
    onBack: () -> Unit,
    onItemClicked: (com.ctb.domain.models.YourEntity) -> Unit,
) =
    composable<YourRoute> {
        YourFeatureScreen(
            onBack = onBack,
            onItemClicked = onItemClicked,
        )
    }
```

### Feature Screen (collectAsEffect with `when`)

```kotlin
// com.ctb.presentation/<feature>/
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ctb.common.ui.collectAsEffect
import com.ctb.presentation.<feature>.viewmodel.YourViewModel
import com.ctb.presentation.<feature>.viewmodel.YourEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun YourFeatureScreen(
    viewModel: YourViewModel = koinViewModel(),
    onBack: () -> Unit,
    onItemClicked: (com.ctb.domain.models.YourEntity) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    // IMPORTANT: always use `when`, never `if`, with collectAsEffect
    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            YourEffect.BackPressed -> onBack.invoke()
            is YourEffect.ShowError ->
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
        }
    }

    YourFeatureContent(
        isLoading = state.isLoading,
        items = state.items,
        error = state.error,
        onItemClicked = onItemClicked,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onCreate()
    }
}
```

### Feature Content (Composable)

```kotlin
// com.ctb.presentation/<feature>/
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ctb.design.compose.theme.Spacing
import com.ctb.domain.models.YourEntity

@Composable
fun YourFeatureContent(
    isLoading: Boolean,
    items: List<YourEntity>,
    error: String,
    onItemClicked: (YourEntity) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            if (error.isNotBlank()) {
                Text(text = error)
            }
            LazyColumn {
                items(items = items) { item ->
                    Text(
                        text = item.toString(),
                        modifier = Modifier.fillMaxWidth().padding(Spacing.small),
                    )
                }
                if (isLoading) item { Text("Loading...") }
            }
        }
    }
}
```

### Navigation Host Integration

```kotlin
// com.ctb.presentation/MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                QuickStartNavHost(
                    navController = navController,
                    startDestination = YourRoute,
                ) {
                    yourFeature(
                        onBack = { navController.popBackStack() },
                        onItemClicked = { navController.openDetailFeature(it) },
                    )
                    detailFeature(
                        onBack = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
```
