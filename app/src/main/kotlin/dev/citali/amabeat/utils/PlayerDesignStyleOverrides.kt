/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.citali.amabeat.constants.PlayerDesignStyle
import dev.citali.amabeat.constants.PlayerDesignStyleOverridesKey

fun parsePlayerDesignStyleOverrides(raw: String): Map<String, PlayerDesignStyle> {
    if (raw.isBlank()) return emptyMap()
    return raw
        .lineSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .mapNotNull { line ->
            val separator = line.lastIndexOf('\t')
            if (separator <= 0 || separator == line.lastIndex) return@mapNotNull null
            val id = line.substring(0, separator)
            val style =
                runCatching {
                    PlayerDesignStyle.valueOf(line.substring(separator + 1))
                }.getOrNull() ?: return@mapNotNull null
            id to style
        }.toMap()
}

fun serializePlayerDesignStyleOverrides(overrides: Map<String, PlayerDesignStyle>): String =
    overrides.entries
        .sortedBy { it.key }
        .joinToString(separator = "\n") { "${it.key}\t${it.value.name}" }

fun resolvePlayerDesignStyle(
    songId: String?,
    overridesRaw: String,
    defaultStyle: PlayerDesignStyle,
): PlayerDesignStyle {
    if (songId.isNullOrBlank()) return defaultStyle
    return parsePlayerDesignStyleOverrides(overridesRaw)[songId] ?: defaultStyle
}

fun setPlayerDesignStyleOverride(
    overridesRaw: String,
    songId: String,
    style: PlayerDesignStyle?,
): String {
    if (songId.isBlank()) return overridesRaw
    val next = parsePlayerDesignStyleOverrides(overridesRaw).toMutableMap()
    if (style == null) {
        next.remove(songId)
    } else {
        next[songId] = style
    }
    return serializePlayerDesignStyleOverrides(next)
}

@Composable
fun rememberResolvedPlayerDesignStyle(
    songId: String?,
    defaultStyle: PlayerDesignStyle = PlayerDesignStyle.V4,
): PlayerDesignStyle {
    val (overridesRaw) = rememberPreference(PlayerDesignStyleOverridesKey, "")
    return remember(songId, overridesRaw, defaultStyle) {
        resolvePlayerDesignStyle(songId, overridesRaw, defaultStyle)
    }
}
