# Architecture — QuickStart

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

## Adding a New Feature

1. **Create feature folder in `:presentation`:** `Screen`, `Content`, `Route`, `ViewModel`, `State`, `Effect`.
2. **Add domain contracts in `:domain`** if needed: entities, repository interface, use case.
3. **Add data implementations** in `:data` (repo impl) and `:data-remote` (API, DTOs, remote impl) if needed.
4. **Wire in `QuickStartModule.kt`:** add Koin modules to `loadFeature`, register ViewModel/use case/repository.
5. **Add navigation route** in `QuickStartNavHost.kt` (`:design` module).
6. **Verify DI graph** — `CheckQuickStartModules` should still pass.

## Layer-Specific Docs

| Module | Type | Deps | Description |
|--------|------|------|-------------|
| `:app` | Android App | `:main`, `:common`, `:presentation` | Thin entry point — `QuickStartApp.kt` + `StartActivity.kt`, calls `QuickStart.init()` |
| `:main` | Android Lib | all layers | [Koin DI wiring](.claude/di-wiring.md) |
| `:domain` | JVM | `:commonKotlin` | [Entities, repo interfaces, use cases](.claude/domain-layer.md) |
| `:data` | JVM | `:domain`, `:commonKotlin` | [Repository impls, data source interfaces](.claude/data-layer.md) |
| `:data-remote` | JVM | `:data`, `:domain` | [Retrofit APIs, DTOs, remote impls](.claude/data-remote-layer.md) |
| `:presentation` | Android Lib | `:design`, `:common`, `:domain` | [Compose screens, ViewModels, routes](.claude/presentation-layer.md) |
| `:design` | Android Lib | Compose only | [Theme tokens, shared components](.claude/design-system.md) |
| `:common` | Android Lib | Lifecycle, Retrofit, OkHttp | [MVI base classes, REST helpers](.claude/common.md) |
| `:commonKotlin` | JVM | Kotlin, Coroutines | [Result, FlowUseCase, test helpers](.claude/common-kotlin.md) |

## Cross-Cutting Concerns

- [Testing Strategy](.claude/tests.md)
