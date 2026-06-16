<!-- nodum:start -->
## Knowledge Graph Context — Nodum

**Load this before each response.** Stack: **unknown** | Files: **77** | Functions: **103** | Last sync: **2026-06-15 23:03**

Analyze code with this project's structure in mind. Reference the knowledge graph when answering questions about code organization, dependencies, or implementation patterns.
<!-- nodum:end -->

<!-- Everything below this line is hand-maintained. The nodum sync only rewrites the block above. -->

## How to work in this repo (read this, not everything)

To keep context small, **do not pre-read the docs or grep the tree wide.** Pull only what the
current task needs, from one of two sources:

### 1. Layer docs — `.claude/*.md`
`.claude/README.md` is the architecture index (module graph + global rules). Open a single layer
doc on demand instead of loading them all:

| Task touches… | Read |
|---|---|
| Module graph, global rules, adding a feature | `.claude/README.md` |
| Compose screens, ViewModels, MVI, routes | `.claude/presentation-layer.md` |
| Entities, repository interfaces, use cases | `.claude/domain-layer.md` |
| Repository impls, data sources | `.claude/data-layer.md` |
| Retrofit APIs, DTOs, remote impls | `.claude/data-remote-layer.md` |
| Koin DI wiring (`QuickStartModule`) | `.claude/di-wiring.md` |
| Theme tokens, shared composables | `.claude/design-system.md` |
| MVI base classes, REST helpers (Android) | `.claude/common.md` |
| `Result`, `FlowUseCase`, test helpers (pure Kotlin) | `.claude/common-kotlin.md` |
| Test patterns & coverage | `.claude/tests.md` |

### 2. Nodum graph — MCP `mcp__nodum__*`
For "where is X / what depends on X / what does X call" questions, query the graph instead of
grepping. It's cheaper than reading files:

- `search_graph` — locate a node by name/keyword.
- `get_node` — details for one file/function.
- `get_dependencies` / `get_dependents` — what a node uses / what uses it.
- `get_graph`, `expand_cluster`, `project_status` — overview & clusters.
- `sync_project` — re-index after structural changes; this refreshes the nodum block above.

**Run `sync_project` after adding/removing/renaming files or modules** so the graph (and the
counts in the block above) stay accurate.

## Stack (one line, so the block above can stay "unknown")
Kotlin · Android (Compose, MVI) · Coroutines · Koin · Retrofit · multi-module Clean Architecture.
Quality tooling (detekt, spotless, jacoco) lives under `tools/`.
