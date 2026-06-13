# Architecture Overview

## Module Dependency Graph

```
app  ──→  main  ───────────────────┬──→ domain
  │         │                      │    ↑
  │         │                      ├──→ data  ←── data-remote
  │         │                      │
  ├─────→ presentation  ←──────────┘
  │         │
  ├─────→ design
  │
  ├─────→ common ─── all layers
  │
  └─────→ commonKotlin ─── all layers
```

## Global Rules

1. **Clean Architecture**: Domain has zero external dependencies (no Retrofit, no Android, no Compose).
2. **No feature-to-feature imports**: `:presentation` features don't import each other. Cross-feature wiring goes through `:main`.
3. **Repository pattern**: Domain defines interface → `:data` implements → `:data-remote` provides raw data source.
4. **Use case pattern**: Each use case is a class receiving repository + `CoroutineDispatcher`.
5. **MVI presentation**: Every screen follows `State` + `Effect` + `ViewModel` pattern.
6. **Kotlin `when` in `collectAsEffect`**: Always use `when` instead of `if`.
7. **DI verification**: `CheckQuickStartModules` uses `koin-test.verify()` to validate the entire DI graph.

## Quick Reference

| Module | Type | Deps | Description |
|--------|------|------|-------------|
| `:app` | Android App | `:main`, `:common`, `:presentation` | Thin entry point |
| `:main` | Android Lib | all layers | Koin DI wiring |
| `:domain` | JVM | `:commonKotlin` | Entities, repository interfaces, use cases |
| `:data` | JVM | `:domain`, `:commonKotlin` | Repository impls, data source interfaces |
| `:data-remote` | JVM | `:data`, `:domain` | Retrofit APIs, DTOs, remote implementations |
| `:presentation` | Android Lib | `:design`, `:common`, `:domain` | Compose screens, ViewModels, routes |
| `:design` | Android Lib | Compose only | Theme tokens, shared composables |
| `:common` | Android Lib | Lifecycle, Retrofit, OkHttp | MVI base classes, REST helpers |
| `:commonKotlin` | JVM | Kotlin, Coroutines | `Result`, `FlowUseCase`, test helpers |

## Adding a New Feature

1. Create feature folder in `:presentation` with `Screen`, `Content`, `Route`, `ViewModel`, `State`, `Effect`.
2. Add domain entities, repository interface, and use case in `:domain` if needed.
3. Add repository impl and data source interface in `:data`; API, DTOs, and remote impl in `:data-remote` if needed.
4. Wire in `QuickStartModule.kt`: add Koin modules to `loadFeature`, register ViewModel/use case/repository.
5. Add nav route in `QuickStartNavHost.kt` (`:design`).
6. Verify DI graph — `CheckQuickStartModules` should still pass.

## Layer-Specific Docs

- [Common (Android Utilities)](common.md)
- [CommonKotlin (Pure Kotlin)](common-kotlin.md)
- [Domain Layer](domain-layer.md)
- [Data Layer](data-layer.md)
- [Data Remote Layer](data-remote-layer.md)
- [Presentation Layer](presentation-layer.md)
- [Design System](design-system.md)
- [DI Wiring](di-wiring.md)
- [Tests](tests.md)
