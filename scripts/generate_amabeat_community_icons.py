#!/usr/bin/env python3
"""Generate the AmaBeat-owned community icon variants from the canonical logo."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CANONICAL = ROOT / "assets/amabeat-icon.svg"
ICON_DIR = ROOT / "IconPack/svg"
METADATA = ROOT / "IconPack/metadata.json"

STYLES = [
    ("28992", "AmaBeat Pulse", "AmaBeat_Pulse.svg", "#050505", "#FFFFFF", None),
    ("29926", "AmaBeat Aurora", "AmaBeat_Aurora.svg", "#17104A", "#FFFFFF", ("#5B2CFF", "#00D4FF")),
    ("38927", "AmaBeat Neon", "AmaBeat_Neon.svg", "#10051F", "#FFFFFF", ("#FF2BD6", "#6B5CFF")),
    ("29030", "AmaBeat Sunset", "AmaBeat_Sunset.svg", "#3A1020", "#FFF7E6", ("#FF7A18", "#FF2D55")),
    ("82962", "AmaBeat Ocean", "AmaBeat_Ocean.svg", "#062C38", "#E8FFFF", ("#00C2A8", "#147DFF")),
    ("38962", "AmaBeat Mono", "AmaBeat_Mono.svg", "#EDEDED", "#101010", None),
]


def canonical_path() -> str:
    match = re.search(r'<path d="([^"]+)"', CANONICAL.read_text())
    if not match:
        raise RuntimeError(f"No canonical path found in {CANONICAL}")
    return match.group(1)


def svg_for(path: str, background: str, foreground: str, gradient: tuple[str, str] | None) -> str:
    defs = ""
    fill = background
    if gradient:
        defs = (
            '<defs><linearGradient id="amaBeatGradient" x1="0" y1="0" x2="1" y2="1">'
            f'<stop offset="0" stop-color="{gradient[0]}"/><stop offset="1" stop-color="{gradient[1]}"/>'
            '</linearGradient></defs>'
        )
        fill = "url(#amaBeatGradient)"
    return f'''<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1254 1254" width="1254" height="1254">
  <title>AmaBeat-owned community icon</title>
  {defs}
  <rect width="1254" height="1254" rx="220" fill="{fill}"/>
  <g transform="translate(0,1254) scale(0.1,-0.1)" fill="{foreground}">
    <path d="{path}"/>
  </g>
</svg>
'''


def main() -> None:
    path = canonical_path()
    for old in ICON_DIR.glob("*.svg"):
        old.unlink()
    metadata = []
    for icon_id, name, filename, background, foreground, gradient in STYLES:
        (ICON_DIR / filename).write_text(svg_for(path, background, foreground, gradient))
        metadata.append(
            {
                "Id": icon_id,
                "Name": name,
                "Author": "nastechresearch",
                "Source": filename,
                "Link": "https://github.com/nastechresearch/AmaBeat/tree/main/IconPack",
                "GitHubAuthorUrl": "https://github.com/nastechresearch",
            }
        )
    METADATA.write_text(json.dumps(metadata, indent=2) + "\n")
    print(f"Generated {len(STYLES)} AmaBeat-owned community icons")


if __name__ == "__main__":
    main()
