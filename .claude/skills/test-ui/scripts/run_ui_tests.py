#!/usr/bin/env python3
"""Run the UI test cases in test/ui-test-plan.md against the compiled program.

Usage:
    run_ui_tests.py [plan_file]

Compiles every .java file under src/main/java, then for each test case in
the plan feeds its Input block to GLaDOS on stdin and compares the full
console output to its Expected output block. Stops at the first failure.
"""

import re
import subprocess
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[4]
SRC_DIR = REPO_ROOT / "src" / "main" / "java"
BUILD_DIR = REPO_ROOT / "_temp" / "ui-test-classes"
MAIN_CLASS = "GLaDOS"

TEST_HEADER_RE = re.compile(r"^## (.+)$", re.MULTILINE)
AIM_RE = re.compile(r"\*\*Aim:\*\*\s*(.+?)\n\n", re.DOTALL)
CODE_BLOCK_RE = re.compile(r"```text\n(.*?)```", re.DOTALL)


def normalize(text):
    """Ignore Windows/Unix line-ending differences and a trailing blank line."""
    return text.replace("\r\n", "\n").rstrip("\n")


def parse_plan(plan_text):
    """Split the plan into test cases: (name, aim, input_text, expected_text)."""
    sections = TEST_HEADER_RE.split(plan_text)[1:]  # drop text before first "## "
    tests = []
    for i in range(0, len(sections), 2):
        name = sections[i].strip()
        body = sections[i + 1]

        aim_match = AIM_RE.search(body)
        aim = " ".join(aim_match.group(1).split()) if aim_match else "(no aim given)"

        blocks = CODE_BLOCK_RE.findall(body)
        if len(blocks) != 2:
            print(f"error: test '{name}' does not have exactly one Input and "
                  f"one Expected output ```text``` block", file=sys.stderr)
            sys.exit(2)

        input_text, expected_text = blocks
        tests.append((name, aim, input_text, expected_text))
    return tests


def compile_sources():
    BUILD_DIR.mkdir(parents=True, exist_ok=True)
    java_files = [str(p) for p in SRC_DIR.glob("*.java")]
    result = subprocess.run(
        ["javac", "-d", str(BUILD_DIR), *java_files],
        capture_output=True, text=True,
    )
    if result.returncode != 0:
        print("Compilation failed:\n" + result.stderr, file=sys.stderr)
        sys.exit(1)


def run_program(input_text):
    result = subprocess.run(
        ["java", "-cp", str(BUILD_DIR), MAIN_CLASS],
        input=input_text, capture_output=True, text=True,
    )
    return result.stdout


def main():
    plan_path = Path(sys.argv[1]) if len(sys.argv) > 1 else REPO_ROOT / "test" / "ui-test-plan.md"
    if not plan_path.is_file():
        print(f"error: no such file: {plan_path}", file=sys.stderr)
        return 2

    tests = parse_plan(plan_path.read_text(encoding="utf-8"))
    if not tests:
        print(f"error: no test cases found in {plan_path}", file=sys.stderr)
        return 2

    print(f"Compiling {SRC_DIR} ...")
    compile_sources()

    for name, aim, input_text, expected_text in tests:
        print(f"\n=== {name} ===")
        print(f"Aim: {aim}")
        print("--- console input ---")
        print(input_text.rstrip("\n"))

        actual_text = run_program(input_text)
        print("--- console output ---")
        print(actual_text.rstrip("\n"))

        if normalize(actual_text) == normalize(expected_text):
            print(f"PASS: {name}")
        else:
            print(f"\nFAIL: {name}")
            print("--- expected output ---")
            print(expected_text.rstrip("\n"))
            print("--- actual output ---")
            print(actual_text.rstrip("\n"))
            print(f"\nTest session terminated at first failure: {name}")
            return 1

    print(f"\nAll {len(tests)} test case(s) passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
