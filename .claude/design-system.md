# Design System

## Config

- **Package:** `com.ctb.design`
- **Type:** Android Library (Compose)
- **Dependencies:** Compose UI, Compose Foundation, Material3, Navigation Compose (no project dependencies)

## Structure

```
com.ctb.design.compose/
├── theme/
│   ├── Color.kt
│   ├── Type.kt
│   ├── Spacing.kt
│   ├── BorderRadius.kt
│   ├── Theme.kt
│   └── ThemePreview.kt
├── component/
│   └── PokemonNavHost.kt    — Shared NavHost with slide animations
└── Extensions.kt
```

## Rules

1. Pure design tokens and reusable UI components. No ViewModels, no business logic.
2. No state management beyond theme previews.
3. Navigation host lives here (not in `:presentation`) because it's a reusable component.
4. All composable components should be themeable via `MaterialTheme`.
