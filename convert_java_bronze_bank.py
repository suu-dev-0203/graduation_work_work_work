#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Convert the original Java Bronze question-bank source files to one JSON file.

Usage:
  python convert_java_bronze_bank.py <original-repository-root> <output-json-path>

This script does not modify the source repository. It reads the four
OriginalQuestionBank*.java files and writes a validated java-bronze.json.
"""

from __future__ import annotations

import json
import re
import sys
from collections import Counter
from pathlib import Path
from typing import List, Tuple

EXPECTED_COUNTS = {1: 20, 2: 35, 3: 35, 4: 35, 5: 35, 6: 35, 7: 30, 8: 35}
BANK_RELATIVE_DIR = Path("src/main/java/com/example/graduation_java_game")
BANK_FILE_NAMES = [
    "OriginalQuestionBank.java",
    "OriginalQuestionBankChapters2To4.java",
    "OriginalQuestionBankChapters5To6.java",
    "OriginalQuestionBankChapters7To8.java",
]


def java_string_value(token: str) -> str:
    """Decode one ordinary Java double-quoted string literal."""
    if len(token) < 2 or token[0] != '"' or token[-1] != '"':
        raise ValueError(f"文字列リテラルではありません: {token[:50]!r}")
    s = token[1:-1]
    out: List[str] = []
    i = 0
    escapes = {
        "b": "\b", "t": "\t", "n": "\n", "f": "\f", "r": "\r",
        '"': '"', "'": "'", "\\": "\\", "s": " ",
    }
    while i < len(s):
        ch = s[i]
        if ch != "\\":
            out.append(ch)
            i += 1
            continue
        i += 1
        if i >= len(s):
            raise ValueError("文字列末尾に不完全なエスケープがあります")
        esc = s[i]
        if esc in escapes:
            out.append(escapes[esc])
            i += 1
        elif esc == "u":
            # Java allows one or more 'u' before the four hexadecimal digits.
            while i < len(s) and s[i] == "u":
                i += 1
            digits = s[i:i + 4]
            if len(digits) != 4 or not re.fullmatch(r"[0-9a-fA-F]{4}", digits):
                raise ValueError(f"不正なUnicodeエスケープ: \\u{digits}")
            out.append(chr(int(digits, 16)))
            i += 4
        elif esc in "01234567":
            # Java octal escapes have one to three octal digits.
            j = i
            max_digits = 3 if esc in "0123" else 2
            while j < len(s) and j - i < max_digits and s[j] in "01234567":
                j += 1
            out.append(chr(int(s[i:j], 8)))
            i = j
        else:
            raise ValueError(f"未対応のJavaエスケープ: \\{esc}")
    return "".join(out)


def scan_balanced_call(source: str, open_paren: int) -> Tuple[str, int]:
    """Return text inside a parenthesized call and the index after its ')'."""
    if source[open_paren] != "(":
        raise ValueError("開き括弧の位置が不正です")
    depth = 0
    i = open_paren
    state = "normal"  # normal, string, char, line_comment, block_comment
    while i < len(source):
        ch = source[i]
        nxt = source[i + 1] if i + 1 < len(source) else ""
        if state == "string":
            if ch == "\\":
                i += 2
                continue
            if ch == '"':
                state = "normal"
            i += 1
            continue
        if state == "char":
            if ch == "\\":
                i += 2
                continue
            if ch == "'":
                state = "normal"
            i += 1
            continue
        if state == "line_comment":
            if ch == "\n":
                state = "normal"
            i += 1
            continue
        if state == "block_comment":
            if ch == "*" and nxt == "/":
                state = "normal"
                i += 2
            else:
                i += 1
            continue

        if ch == '"':
            state = "string"
            i += 1
        elif ch == "'":
            state = "char"
            i += 1
        elif ch == "/" and nxt == "/":
            state = "line_comment"
            i += 2
        elif ch == "/" and nxt == "*":
            state = "block_comment"
            i += 2
        elif ch == "(":
            depth += 1
            i += 1
        elif ch == ")":
            depth -= 1
            i += 1
            if depth == 0:
                return source[open_paren + 1:i - 1], i
        else:
            i += 1
    raise ValueError("Javaコード内の括弧が閉じられていません")


def split_top_level_args(content: str) -> List[str]:
    """Split a Java call's arguments at commas outside nested expressions."""
    args: List[str] = []
    start = 0
    paren = bracket = brace = 0
    i = 0
    state = "normal"
    while i < len(content):
        ch = content[i]
        nxt = content[i + 1] if i + 1 < len(content) else ""
        if state == "string":
            if ch == "\\":
                i += 2
                continue
            if ch == '"':
                state = "normal"
            i += 1
            continue
        if state == "char":
            if ch == "\\":
                i += 2
                continue
            if ch == "'":
                state = "normal"
            i += 1
            continue
        if state == "line_comment":
            if ch == "\n":
                state = "normal"
            i += 1
            continue
        if state == "block_comment":
            if ch == "*" and nxt == "/":
                state = "normal"
                i += 2
            else:
                i += 1
            continue
        if ch == '"':
            state = "string"
        elif ch == "'":
            state = "char"
        elif ch == "/" and nxt == "/":
            state = "line_comment"
            i += 2
            continue
        elif ch == "/" and nxt == "*":
            state = "block_comment"
            i += 2
            continue
        elif ch == "(":
            paren += 1
        elif ch == ")":
            paren -= 1
        elif ch == "[":
            bracket += 1
        elif ch == "]":
            bracket -= 1
        elif ch == "{":
            brace += 1
        elif ch == "}":
            brace -= 1
        elif ch == "," and paren == 0 and bracket == 0 and brace == 0:
            args.append(content[start:i].strip())
            start = i + 1
        i += 1
    last = content[start:].strip()
    if last or args:
        args.append(last)
    return args


def extract_call_args(source: str, regex: re.Pattern[str], label: str) -> List[List[str]]:
    calls: List[List[str]] = []
    for match in regex.finditer(source):
        open_paren = source.find("(", match.start())
        inner, _ = scan_balanced_call(source, open_paren)
        calls.append(split_top_level_args(inner))
    return calls


def string_expression(expr: str) -> str | None:
    expr = expr.strip()
    if expr == "null":
        return None
    tokens = re.findall(r'"(?:\\.|[^"\\])*"', expr, re.DOTALL)
    if not tokens:
        raise ValueError(f"文字列が見つかりません: {expr[:80]!r}")
    return "".join(java_string_value(token) for token in tokens)


def list_expression(expr: str, expected_method: str) -> List[str]:
    match = re.search(r"\b" + re.escape(expected_method) + r"\s*\(", expr)
    if not match:
        raise ValueError(f"{expected_method}(...) が見つかりません: {expr[:80]!r}")
    open_paren = expr.find("(", match.start())
    inner, _ = scan_balanced_call(expr, open_paren)
    items = split_top_level_args(inner)
    return [string_expression(item) for item in items]  # type: ignore[list-item]


def java_hash_code(value: str) -> int:
    """Java String.hashCode(), using UTF-16 code units."""
    raw = value.encode("utf-16-be", errors="surrogatepass")
    h = 0
    for index in range(0, len(raw), 2):
        unit = (raw[index] << 8) | raw[index + 1]
        h = (31 * h + unit) & 0xFFFFFFFF
    if h >= 0x80000000:
        h -= 0x100000000
    return h


def rotate_like_source(values: List[str], offset: int) -> List[str]:
    # Matches the source method: values[(index - offset + size) % size].
    size = len(values)
    return [values[(index - offset + size) % size] for index in range(size)]


def parse_questions(source_path: Path) -> List[dict]:
    source = source_path.read_text(encoding="utf-8")
    q_pattern = re.compile(r'\bq\s*\(\s*"CH[1-8]-\d{2}"')
    calls = extract_call_args(source, q_pattern, "q")
    questions: List[dict] = []
    for args in calls:
        if len(args) != 12:
            raise ValueError(
                f"{source_path.name}: q(...)の引数が12個ではありません（{len(args)}個）"
            )
        source_id = string_expression(args[0])
        checklist = string_expression(args[1])
        theme = string_expression(args[2])
        difficulty = args[3].split(".")[-1].strip()
        prompt = string_expression(args[4])
        code_block = string_expression(args[5])
        choices = list_expression(args[6], "choices")
        raw_answer = int(args[7].strip())
        explanation = string_expression(args[8])
        reasons = list_expression(args[9], "reasons")
        review_hint = string_expression(args[10])
        time_limit = int(args[11].strip())

        if not source_id or not re.fullmatch(r"CH[1-8]-\d{2}", source_id):
            raise ValueError(f"問題IDが不正です: {source_id!r}")
        if len(choices) != 5 or len(reasons) != 5:
            raise ValueError(f"{source_id}: 選択肢・選択肢解説は5件必要です")
        if raw_answer < 0 or raw_answer >= len(choices):
            raise ValueError(f"{source_id}: 正解番号が選択肢範囲外です")

        chapter = int(source_id[2])
        displayed_answer = java_hash_code(source_id) % len(choices)
        rotation = (displayed_answer - raw_answer) % len(choices)
        displayed_choices = rotate_like_source(choices, rotation)
        displayed_reasons = rotate_like_source(reasons, rotation)

        # OriginalQuestionBank.q() prefixes its questionText with [ID] and theme.
        # For JSON, keep those as separate fields and use only the actual prompt as questionText.
        detailed_lines = [
            f"対応する出題範囲: {checklist}",
            f"学習テーマ: {theme}",
            "",
            f"正解: {chr(ord('A') + displayed_answer)}",
            explanation or "",
            "",
            "各選択肢の解説:",
        ]
        detailed_lines.extend(
            f"{chr(ord('A') + index)}. {reason}"
            for index, reason in enumerate(displayed_reasons)
        )
        detailed_lines.extend(["", f"間違えた場合の復習: {review_hint or ''}"])
        detailed_explanation = "\n".join(detailed_lines)

        questions.append({
            "questionId": f"BRONZE-{source_id}",
            "sourceQuestionId": source_id,
            "genre": "BRONZE",
            "chapter": chapter,
            "isMock": chapter == 8,
            "checklist": checklist,
            "theme": theme,
            "difficulty": difficulty,
            "questionText": prompt,
            "codeBlock": code_block,
            "choices": displayed_choices,
            "answer": displayed_answer,
            "explanation": explanation,
            "choiceExplanations": displayed_reasons,
            "reviewHint": review_hint,
            "detailedExplanation": detailed_explanation,
            "timeLimitSeconds": time_limit,
        })
    return questions


def parse_chapters(source_files: List[Path]) -> List[dict]:
    seed_pattern = re.compile(r"\bnew\s+(?:OriginalQuestionBank\.)?ChapterSeed\s*\(")
    chapters_by_number: dict[int, dict] = {}
    for path in source_files:
        source = path.read_text(encoding="utf-8")
        for args in extract_call_args(source, seed_pattern, "ChapterSeed"):
            if len(args) < 2:
                continue
            try:
                title = string_expression(args[0])
                description = string_expression(args[1])
            except ValueError:
                continue
            if title is None or description is None:
                continue
            match = re.search(r"第\s*(\d+)\s*章", title)
            if not match:
                continue
            number = int(match.group(1))
            chapters_by_number[number] = {
                "chapter": number,
                "title": title,
                "description": description,
                "isMock": number == 8,
            }
    return [chapters_by_number[number] for number in sorted(chapters_by_number)]


def main() -> int:
    if len(sys.argv) != 3:
        print(
            "使い方: python convert_java_bronze_bank.py <元リポジトリのルート> <出力JSONのパス>",
            file=sys.stderr,
        )
        return 2

    source_root = Path(sys.argv[1]).expanduser().resolve()
    output_path = Path(sys.argv[2]).expanduser().resolve()
    bank_dir = source_root / BANK_RELATIVE_DIR
    source_files = [bank_dir / name for name in BANK_FILE_NAMES]

    missing = [str(path) for path in source_files if not path.is_file()]
    if missing:
        print("問題バンクのJavaファイルが見つかりません:", file=sys.stderr)
        for path in missing:
            print(f"  - {path}", file=sys.stderr)
        return 1

    if output_path.exists():
        print(
            f"出力先が既に存在するため、上書きせず停止しました: {output_path}",
            file=sys.stderr,
        )
        print("既存ファイルを残したい場合は、出力先を別の名前にしてください。", file=sys.stderr)
        return 1

    try:
        chapters = parse_chapters(source_files)
        all_questions: List[dict] = []
        for source_file in source_files:
            all_questions.extend(parse_questions(source_file))

        counts = Counter(question["chapter"] for question in all_questions)
        ids = [question["sourceQuestionId"] for question in all_questions]
        duplicates = sorted(value for value, count in Counter(ids).items() if count > 1)

        if duplicates:
            raise ValueError(f"重複した問題IDがあります: {', '.join(duplicates)}")
        if len(chapters) != 8:
            raise ValueError(f"章情報が8件ではありません（{len(chapters)}件）")
        if len(all_questions) != 260:
            raise ValueError(f"問題数が260問ではありません（{len(all_questions)}問）")
        for chapter, expected in EXPECTED_COUNTS.items():
            actual = counts.get(chapter, 0)
            if actual != expected:
                raise ValueError(
                    f"第{chapter}章の問題数が想定と異なります: {actual}問（想定 {expected}問）"
                )

        all_questions.sort(key=lambda item: (item["chapter"], item["sourceQuestionId"]))
        data = {
            "schemaVersion": 1,
            "genre": "BRONZE",
            "totalQuestions": len(all_questions),
            "chapters": chapters,
            "questions": all_questions,
        }

        output_path.parent.mkdir(parents=True, exist_ok=True)
        output_path.write_text(
            json.dumps(data, ensure_ascii=False, indent=2) + "\n",
            encoding="utf-8",
        )

        print(f"JSONを作成しました: {output_path}")
        print("章ごとの問題数:")
        for chapter in sorted(EXPECTED_COUNTS):
            print(f"  第{chapter}章: {counts.get(chapter, 0)}問")
        print(f"合計: {len(all_questions)}問")
        print("choices と choiceExplanations は、元Javaコードの表示順に合わせて変換しました。")
        return 0

    except (OSError, ValueError, IndexError) as exc:
        print(f"変換に失敗しました: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
