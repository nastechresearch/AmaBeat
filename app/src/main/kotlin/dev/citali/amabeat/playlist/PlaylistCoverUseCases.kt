/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.playlist

import android.net.Uri
import dev.citali.amabeat.repository.PlaylistCoverRepository
import javax.inject.Inject

class UpdatePlaylistCoverUseCase
    @Inject
    constructor(
        private val repository: PlaylistCoverRepository,
    ) {
        suspend operator fun invoke(
            playlistId: String,
            uri: Uri,
        ) {
            val playlist = requireNotNull(repository.getPlaylist(playlistId))
            if (playlist.browseId == null) {
                repository.setLocalCover(playlist, uri)
            } else {
                repository.setRemoteCover(playlist, uri)
            }
        }
    }

class RemovePlaylistCoverUseCase
    @Inject
    constructor(
        private val repository: PlaylistCoverRepository,
    ) {
        suspend operator fun invoke(playlistId: String) {
            val playlist = requireNotNull(repository.getPlaylist(playlistId))
            if (playlist.browseId == null) {
                repository.removeLocalCover(playlist)
            } else {
                repository.removeRemoteCover(playlist)
            }
        }
    }
