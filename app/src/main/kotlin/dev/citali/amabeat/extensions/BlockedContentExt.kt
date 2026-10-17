/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.extensions

import moe.rukamori.amabeat.innertube.models.AlbumItem
import moe.rukamori.amabeat.innertube.models.ArtistItem
import moe.rukamori.amabeat.innertube.models.PlaylistItem
import moe.rukamori.amabeat.innertube.models.SongItem
import moe.rukamori.amabeat.innertube.models.YTItem
import moe.rukamori.amabeat.innertube.pages.BrowseResult

fun <T : YTItem> List<T>.filterBlockedArtists(blockedArtistIds: Set<String>): List<T> {
    if (blockedArtistIds.isEmpty()) return this

    return filter { item ->
        when (item) {
            is ArtistItem -> item.id !in blockedArtistIds
            is SongItem -> item.artists.none { it.id in blockedArtistIds }
            is AlbumItem -> item.artists.orEmpty().none { it.id in blockedArtistIds }
            is PlaylistItem -> item.author?.id !in blockedArtistIds
        }
    }
}

fun BrowseResult.filterBlockedArtists(blockedArtistIds: Set<String>): BrowseResult {
    if (blockedArtistIds.isEmpty()) return this

    return copy(
        items =
            items.mapNotNull { section ->
                section.copy(
                    items =
                        section.items
                            .filterBlockedArtists(blockedArtistIds)
                            .ifEmpty { return@mapNotNull null },
                )
            },
    )
}
