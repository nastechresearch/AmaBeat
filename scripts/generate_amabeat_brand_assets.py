#!/usr/bin/env python3
"""Generate AmaBeat's opaque splash/icon and transparent foreground assets.

The canonical source is the supplied AmaBeat logo in assets/amabeat-icon.png:
black background, white mark, square canvas. Android adaptive-icon resources
use the transparent output as foreground/monochrome and the opaque output as
background or legacy launcher artwork.
"""
from __future__ import annotations

import argparse
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
DEFAULT_SOURCE = ROOT / "assets/amabeat-icon.png"

OPAQUE_OUTPUTS = {
    ROOT / "assets/amabeat-icon.png": (512, 512),
    ROOT / "assets/amabeat-icon-rounded.png": (512, 512),
    ROOT / "app/src/main/res/drawable/about_splash.png": (512, 512),
    ROOT / "app/src/main/res/drawable-nodpi/app_icon_small.png": (512, 512),
    ROOT / "website/assets/icon-hero.png": (512, 512),
    ROOT / "website/assets/icon.png": (192, 192),
    ROOT / "website/favicon.png": (192, 192),
    ROOT / "fastlane/metadata/android/en-US/images/icon.png": (512, 512),
}

TRANSPARENT_OUTPUTS = {
    ROOT / "assets/amabeat-icon-transparent.png": (2048, 2048),
    ROOT / "website/assets/icon-fg.png": (1728, 1728),
}

for density, size in {"mdpi": 108, "hdpi": 162, "xhdpi": 216, "xxhdpi": 324, "xxxhdpi": 432}.items():
    TRANSPARENT_OUTPUTS[ROOT / f"app/src/main/res/mipmap-{density}/ic_launcher_foreground.png"] = (size, size)
    TRANSPARENT_OUTPUTS[ROOT / f"app/src/main/res/mipmap-{density}/ic_launcher_monochrome.png"] = (size, size)
    OPAQUE_OUTPUTS[ROOT / f"app/src/main/res/mipmap-{density}/ic_launcher_background.png"] = (size, size)

for density, size in {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}.items():
    OPAQUE_OUTPUTS[ROOT / f"app/src/main/res/mipmap-{density}/ic_launcher.png"] = (size, size)


def resize(source: Image.Image, size: tuple[int, int]) -> Image.Image:
    return source.resize(size, Image.Resampling.LANCZOS)


def transparent_variant(source: Image.Image, size: tuple[int, int]) -> Image.Image:
    resized = resize(source, size).convert("RGBA")
    # The supplied artwork is white on black. Use its luminance as alpha so
    # antialiased edges remain smooth while the black canvas becomes transparent.
    alpha = resized.convert("L").point(lambda value: value)
    result = Image.new("RGBA", size, (255, 255, 255, 0))
    result.putalpha(alpha)
    return result


def save(image: Image.Image, path: Path) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path, format="PNG", optimize=True)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--source", type=Path, default=DEFAULT_SOURCE)
    args = parser.parse_args()

    source = Image.open(args.source).convert("RGBA")
    for path, size in OPAQUE_OUTPUTS.items():
        save(resize(source, size), path)
    for path, size in TRANSPARENT_OUTPUTS.items():
        save(transparent_variant(source, size), path)

    print(f"Generated {len(OPAQUE_OUTPUTS)} opaque and {len(TRANSPARENT_OUTPUTS)} transparent AmaBeat assets from {args.source}")


if __name__ == "__main__":
    main()
