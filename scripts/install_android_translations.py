#!/usr/bin/env python3
"""Install generated Android string translations while preserving resource structure."""
from __future__ import annotations

import json
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

PLACEHOLDER = re.compile(r"%(?:\\d+\$)?[sd]")


def key_for(element: ET.Element, child: ET.Element | None = None) -> str:
    key = f"strings.xml::{element.attrib['name']}"
    if child is not None:
        key += f"::{child.attrib['quantity']}"
    return key


def text_of(element: ET.Element) -> str:
    return "".join(element.itertext())


def valid_xml_text(value: str) -> bool:
    return all(
        ch in "\t\n\r" or ord(ch) >= 0x20
        for ch in value
    )


def translated_or_source(values: dict[str, str], key: str, source_value: str) -> str:
    value = values.get(key, source_value)
    # A model can accidentally decode escaped Unicode arrows/control sequences. Keep the
    # original source value rather than emitting XML 1.0-invalid characters.
    return value if valid_xml_text(value) else source_value


def main() -> None:
    if len(sys.argv) != 3:
        raise SystemExit("usage: install_android_translations.py translations.json app/src/main/res")
    translations_path = Path(sys.argv[1])
    res_root = Path(sys.argv[2])
    source_path = res_root / "values" / "strings.xml"
    translations = json.loads(translations_path.read_text(encoding="utf-8"))
    source_root = ET.parse(source_path).getroot()

    # These two entries were absent from the model response for isiZulu and Shona.
    manual = {
        "zu": {
            "strings.xml::scheduled_backup_enabled_description": "Izipele zisebenza ngokuzenzakalela kusetshenziswa uhlelo olungezansi.",
            "strings.xml::scheduled_backup_disabled_description": "Izipele ezizenzakalelayo zimisiwe.",
        },
        "sn": {
            "strings.xml::scheduled_backup_enabled_description": "Mabhakapu anomhanya otomatiki achishandisa purogiramu iri pazasi.",
            "strings.xml::scheduled_backup_disabled_description": "Mabhakapu otomatiki ambomiswa.",
        },
    }

    for language in ("lg", "zu", "sn"):
        values = dict(translations.get(language, {}))
        values.update(manual.get(language, {}))
        root = ET.Element("resources")
        expected = 0
        for source in source_root:
            clone = ET.SubElement(root, source.tag, dict(source.attrib))
            if source.tag == "plurals":
                for source_item in source:
                    expected += 1
                    item = ET.SubElement(clone, source_item.tag, dict(source_item.attrib))
                    key = key_for(source, source_item)
                    value = translated_or_source(values, key, text_of(source_item))
                    if sorted(PLACEHOLDER.findall(text_of(source_item))) != sorted(PLACEHOLDER.findall(value)):
                        raise ValueError(f"placeholder mismatch for {language}: {key}")
                    item.text = value
            else:
                expected += 1
                key = key_for(source)
                value = translated_or_source(values, key, text_of(source))
                if sorted(PLACEHOLDER.findall(text_of(source))) != sorted(PLACEHOLDER.findall(value)):
                    raise ValueError(f"placeholder mismatch for {language}: {key}")
                clone.text = value

        ET.indent(root, space="    ")
        output_dir = res_root / f"values-{language}"
        output_dir.mkdir(parents=True, exist_ok=True)
        output_path = output_dir / "strings.xml"
        tree = ET.ElementTree(root)
        tree.write(output_path, encoding="utf-8", xml_declaration=True)
        actual = sum(len(list(e)) if e.tag == "plurals" else 1 for e in root)
        if actual != expected:
            raise ValueError(f"resource count mismatch for {language}: {actual} != {expected}")
        print(f"{language}: wrote {actual} entries to {output_path}")


if __name__ == "__main__":
    main()
