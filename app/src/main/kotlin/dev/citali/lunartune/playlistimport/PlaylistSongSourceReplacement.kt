/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.playlistimport

import dev.citali.amabeat.db.entities.PlaylistSongMap

internal fun PlaylistSongMap.replaceSongSource(replacementSongId: String): PlaylistSongMap {
    require(replacementSongId.isNotBlank()) { "Replacement song ID must not be blank" }
    return copy(
        songId = replacementSongId,
        setVideoId = null,
    )
}
