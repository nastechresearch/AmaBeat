/*
 * AmaBeat (2026)
 * © Rukamori — github.com/rukamori
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.amabeat.innertube.pages

import moe.rukamori.amabeat.innertube.models.AlbumItem
import moe.rukamori.amabeat.innertube.models.AlbumReleaseType
import moe.rukamori.amabeat.innertube.models.Artist
import moe.rukamori.amabeat.innertube.models.MusicTwoRowItemRenderer
import moe.rukamori.amabeat.innertube.models.oddElements
import moe.rukamori.amabeat.innertube.models.splitBySeparator

object NewReleaseAlbumPage {
    fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
        val thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getBestThumbnail() ?: return null
        val subtitleRuns = renderer.subtitle?.runs ?: return null
        val subtitleGroups = subtitleRuns.splitBySeparator()

        return AlbumItem(
            browseId = renderer.navigationEndpoint.browseEndpoint?.browseId ?: return null,
            playlistId =
                renderer.thumbnailOverlay
                    ?.musicItemThumbnailOverlayRenderer
                    ?.content
                    ?.musicPlayButtonRenderer
                    ?.playNavigationEndpoint
                    ?.watchPlaylistEndpoint
                    ?.playlistId ?: return null,
            title =
                renderer.title.runs
                    ?.firstOrNull()
                    ?.text ?: return null,
            artists =
                subtitleGroups.getOrNull(1)?.oddElements()?.map {
                    Artist(
                        name = it.text,
                        id = it.navigationEndpoint?.browseEndpoint?.browseId,
                    )
                } ?: return null,
            year =
                subtitleRuns
                    .lastOrNull()
                    ?.text
                    ?.toIntOrNull(),
            releaseType =
                AlbumReleaseType.fromLabel(
                    subtitleGroups.firstOrNull()?.joinToString(separator = "") { it.text },
                ),
            thumbnail = thumbnail.normalizedUrl,
            thumbnailWidth = thumbnail.width,
            thumbnailHeight = thumbnail.height,
            explicit =
                renderer.subtitleBadges?.find {
                    it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                } != null,
        )
    }
}
