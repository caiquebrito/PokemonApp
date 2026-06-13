# Common Module — Shared Android Utilities

## Config

- **Package:** `com.ctb.common`
- **Type:** Android Library
- **Dependencies:** Lifecycle, Retrofit, GSON, OkHttp, Koin (no project dependencies)

## Structure

```
com.ctb.common/
├── viewmodel/flow/    — ViewModel, State, Effect base classes (MVI)
├── ui/                — ResourceProvider, EffectFlowExtension (collectAsEffect)
├── rest/              — OkHttpClientFactory, ApiServiceFactory
├── exception/         — UnknownException
├── Common.kt          — Common declarations
└── CommonModule.kt    — Koin module
```

## Rules

1. Android-only shared utilities. Can depend on Android framework.
2. Provides MVI base classes (`ViewModel`, `State`, `Effect`), REST setup, and resource abstraction.
3. No presentation logic, no business logic, no data implementations.
4. `collectAsEffect` composable lives here — always use `when` to handle effects.
