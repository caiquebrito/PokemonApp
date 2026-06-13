# Tests

## Config

Test utilities are spread across multiple modules. Koin graph verification lives in `:main`.

## Domain Layer Tests

- **Location:** `domain/src/test/kotlin/`
- **Tools:** JUnit, MockK, Coroutines `TestRule`/`TestDispatcher`
- **Pattern:** Test use cases by mocking repository interfaces, verifying flows

## Presentation Layer Tests

- **Location:** `presentation/src/test/java/`
- **Tools:** JUnit, MockK, Coroutines test tools
- **Pattern:** Test ViewModels by mocking use cases, verifying state/effect emissions

## Koin DI Graph Verification

- **Location:** `main/src/test/java/`
- **Tools:** Koin Test (`koin-test`), `KoinExperimentalAPI.verify()`
- **Pattern:** `CheckQuickStartModules` class starts Koin with all modules + mocks and calls `modules.verify()` to ensure every dependency can be resolved

## Rules

1. Domain tests mock repository interfaces, never implementations.
2. Presentation tests mock use cases, never view models or repositories.
3. Koin graph test uses `startKoin { modules(mockModule, *loadFeature.toTypedArray()) }` to verify completeness.
4. All layers use `CoroutinesTestRule` / `UnconfinedTestDispatcher` for deterministic coroutine testing.
5. Use MockK for mocking, JUnit for assertions, Turbine for Flow testing (presentation layer).

## Test Utilities

### Domain — `domain/src/test/kotlin/`

- CoroutinesTestRule from `:commonKotlin`
- TestExtension and TestFlowCollector from `:commonKotlin.test`

### Presentation — `presentation/src/test/java/`

- Coroutines test tools for ViewModel testing
- Turbine for Flow emissions
- MockK for use cases