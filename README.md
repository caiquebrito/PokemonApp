# QuickStart

**QuickStart** is a ready-to-fork Android project that gives you a clean, opinionated foundation so you can start building (or solving a take-home) in minutes instead of hours.

## Architecture

- Multi-module **Clean Architecture** — `domain` has zero Android/framework dependencies; `data` / `data-remote` implement the repositories; `presentation` holds the UI.
- **MVI** presentation layer (State + Effect + ViewModel) with Jetpack Compose.
- **Koin** dependency injection, with a `verify()`-based test that validates the whole DI graph.

See [ARCHITECTURE.md](ARCHITECTURE.md) for the module dependency graph and layer-by-layer docs.

## Tech stack

Kotlin, Jetpack Compose, Coroutines/Flow, Koin, Retrofit/OkHttp, JUnit + MockK + Turbine.

## Quality, out of the box

- `detekt` static analysis — `./gradlew detekt`
- `Spotless` + `ktlint` formatting — `./gradlew spotlessCheck` / `./gradlew spotlessApply`
- `Jacoco` coverage, per-module and aggregated — `./gradlew jacocoAggregatedReport`

## Included sample

A fully working URL-shortener feature (input validation, remote call, result list, detail screen) demonstrating every layer end to end.

## Getting started

```bash
./gradlew clean assembleDebug test spotlessCheck detekt
```
