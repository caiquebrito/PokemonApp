#!/usr/bin/env python3
"""Run a Gradle task only on the modules touched by the current diff.

Usage:
    python3 tools/ci/changed_modules.py --base origin/main --task spotlessCheck

The script:
  1. Reads the Gradle module list from settings.gradle.kts.
  2. Diffs the current HEAD against the merge-base with --base to find
     changed files.
  3. Maps each changed file to its owning Gradle module.
  4. If a change falls outside any module (root build files, version
     catalog, tools/build-logic, settings.gradle.kts, ...), every module
     is considered affected, since those files can change how all modules
     build.
  5. Runs `./gradlew :module:<task> ...` for the affected modules only.

If nothing is affected, it exits 0 without invoking Gradle.
"""

import argparse
import re
import subprocess
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[2]
SETTINGS_FILE = REPO_ROOT / "settings.gradle.kts"
INCLUDE_RE = re.compile(r'include\(\s*"([^"]+)"\s*\)')

# Paths outside any Gradle module that can still affect how every module
# builds. A change here makes every module "affected".
GLOBAL_PATHS = (
    "build.gradle.kts",
    "settings.gradle.kts",
    "gradle.properties",
    "gradle/",
    "tools/build-logic/",
)


def discover_modules() -> list[str]:
    """Return Gradle project paths, e.g. [":app", ":domain", ...]."""
    text = SETTINGS_FILE.read_text()
    return INCLUDE_RE.findall(text)


def module_dir(gradle_path: str) -> str:
    """Convert a Gradle project path (":feature:foo") to a repo-relative dir."""
    return gradle_path.lstrip(":").replace(":", "/")


def changed_files(base_ref: str, head_ref: str) -> list[str]:
    merge_base = subprocess.run(
        ["git", "merge-base", base_ref, head_ref],
        cwd=REPO_ROOT,
        capture_output=True,
        text=True,
        check=True,
    ).stdout.strip()

    result = subprocess.run(
        ["git", "diff", "--name-only", merge_base, head_ref],
        cwd=REPO_ROOT,
        capture_output=True,
        text=True,
        check=True,
    )
    return [line for line in result.stdout.splitlines() if line]


def is_global_change(file_path: str) -> bool:
    return any(file_path == p or file_path.startswith(p) for p in GLOBAL_PATHS)


def changed_modules(base_ref: str, head_ref: str) -> list[str]:
    modules = discover_modules()

    # Match the deepest module path first so nested modules (":feature:foo")
    # aren't shadowed by a shorter parent path (":feature").
    module_paths = sorted(
        ((module_dir(m), m) for m in modules),
        key=lambda pair: len(pair[0]),
        reverse=True,
    )

    files = changed_files(base_ref, head_ref)
    if not files:
        return []

    touched: set[str] = set()
    for file_path in files:
        if is_global_change(file_path):
            return list(modules)

        for dir_path, gradle_path in module_paths:
            if file_path == dir_path or file_path.startswith(dir_path + "/"):
                touched.add(gradle_path)
                break

    # Preserve settings.gradle.kts ordering for readable output.
    return [m for m in modules if m in touched]


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--base",
        default="origin/main",
        help="Base ref to diff against (default: origin/main)",
    )
    parser.add_argument("--head", default="HEAD", help="Head ref (default: HEAD)")
    parser.add_argument(
        "--task",
        default="spotlessCheck",
        help="Gradle task to run per affected module (default: spotlessCheck)",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Print the Gradle command without running it",
    )
    args = parser.parse_args()

    modules = changed_modules(args.base, args.head)

    if not modules:
        print("No Gradle module changes detected — skipping.")
        return 0

    print(f"Changed modules: {', '.join(modules)}")

    tasks = [f"{module}:{args.task}" for module in modules]
    cmd = ["./gradlew", *tasks]

    print(f"Running: {' '.join(cmd)}")
    if args.dry_run:
        return 0

    return subprocess.call(cmd, cwd=REPO_ROOT)


if __name__ == "__main__":
    sys.exit(main())
