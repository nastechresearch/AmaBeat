/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.models

import dev.citali.amabeat.db.entities.Song

enum class ImportSource {
    LOCAL,
    YOUTUBE,
    UNRESOLVED,
}

data class ImportedSongResult(
    val originalSong: Song,
    val resolvedId: String?,
    val resolvedSong: Song?,
    val source: ImportSource,
) {
    val isResolved: Boolean
        get() = source != ImportSource.UNRESOLVED && resolvedId != null && resolvedSong != null
}
