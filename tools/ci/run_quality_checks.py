#!/usr/bin/env python3
"""Run spotlessCheck/detekt per changed module, reporting each (module, task)
pair as its own GitHub commit status.

This is the per-module companion to changed_modules.py: instead of a single
aggregate "spotless" / "detekt" check on the PR, every affected module gets
its own status, e.g.:

    spotlessCheck (:domain)        - success
    spotlessCheck (:presentation)  - failure
    detekt (:domain)               - success
    detekt (:presentation)         - success

All of this runs inside a single Bitrise build/workflow — no extra builds are
triggered. GitHub's Commit Status API (not Checks) is used directly, since
Checks don't support custom names per build.

Required env vars (set GITHUB_STATUS_TOKEN as a Bitrise secret, the rest are
provided automatically by Bitrise):
  GITHUB_STATUS_TOKEN  - GitHub PAT with `repo:status` scope
  GIT_REPOSITORY_URL   - repo URL, used to derive the "owner/repo" slug
  BITRISE_GIT_COMMIT   - commit SHA to report the statuses against
  BITRISE_BUILD_URL    - linked from each status as its target_url (optional)

If GITHUB_STATUS_TOKEN is unset, the script still runs every task locally
(useful for local testing) but skips reporting statuses.

Usage:
  python3 tools/ci/run_quality_checks.py --base origin/main
  python3 tools/ci/run_quality_checks.py --base origin/main --tasks spotlessCheck
"""

import argparse
import json
import os
import re
import subprocess
import sys
import urllib.request

from changed_modules import REPO_ROOT, changed_modules

GITHUB_API = "https://api.github.com"


def repo_slug() -> str:
    url = os.environ["GIT_REPOSITORY_URL"]
    match = re.search(r"github\.com[:/](.+?)(\.git)?$", url)
    if not match:
        raise ValueError(f"Could not parse owner/repo from GIT_REPOSITORY_URL={url!r}")
    return match.group(1)


def post_status(slug: str, sha: str, token: str, context: str, state: str, description: str) -> None:
    url = f"{GITHUB_API}/repos/{slug}/statuses/{sha}"
    payload = {"state": state, "context": context, "description": description[:140]}
    target_url = os.environ.get("BITRISE_BUILD_URL")
    if target_url:
        payload["target_url"] = target_url

    request = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        method="POST",
        headers={
            "Authorization": f"token {token}",
            "Accept": "application/vnd.github+json",
            "User-Agent": "pokemonapp-ci",
        },
    )
    with urllib.request.urlopen(request) as response:
        response.read()


def run_task(module: str, task: str) -> int:
    cmd = ["./gradlew", f"{module}:{task}"]
    print(f"$ {' '.join(cmd)}")
    return subprocess.call(cmd, cwd=REPO_ROOT)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--base", default="origin/main", help="Base ref to diff against")
    parser.add_argument("--head", default="HEAD", help="Head ref")
    parser.add_argument(
        "--tasks",
        nargs="+",
        default=["spotlessCheck", "detekt"],
        help="Gradle tasks to run per affected module",
    )
    args = parser.parse_args()

    modules = changed_modules(args.base, args.head)
    if not modules:
        print("No Gradle module changes detected — nothing to run.")
        return 0

    print(f"Changed modules: {', '.join(modules)}")

    token = os.environ.get("GITHUB_STATUS_TOKEN")
    sha = os.environ.get("BITRISE_GIT_COMMIT")
    slug = repo_slug() if token else None

    checks = [(module, task) for module in modules for task in args.tasks]

    if token:
        for module, task in checks:
            post_status(slug, sha, token, f"{task} ({module})", "pending", "Running…")
    else:
        print("GITHUB_STATUS_TOKEN not set — running tasks without reporting statuses.")

    overall_exit = 0
    for module, task in checks:
        exit_code = run_task(module, task)

        if token:
            state = "success" if exit_code == 0 else "failure"
            description = "Passed" if exit_code == 0 else "Failed"
            post_status(slug, sha, token, f"{task} ({module})", state, description)

        if exit_code != 0:
            overall_exit = exit_code

    return overall_exit


if __name__ == "__main__":
    sys.exit(main())
