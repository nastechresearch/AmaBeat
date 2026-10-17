/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dev.citali.amabeat.constants.HideExplicitKey
import dev.citali.amabeat.constants.HideVideoKey
import dev.citali.amabeat.db.MusicDatabase
import dev.citali.amabeat.extensions.filterBlockedArtists
import moe.rukamori.amabeat.innertube.YouTube
import moe.rukamori.amabeat.innertube.models.BrowseEndpoint
import moe.rukamori.amabeat.innertube.models.filterExplicit
import moe.rukamori.amabeat.innertube.models.filterVideo
import moe.rukamori.amabeat.innertube.pages.ArtistItemsPageLayout
import dev.citali.amabeat.models.ItemsPage
import dev.citali.amabeat.utils.dataStore
import dev.citali.amabeat.utils.get
import dev.citali.amabeat.utils.reportException
import javax.inject.Inject

@HiltViewModel
class ArtistItemsViewModel
    @Inject
    constructor(
        @ApplicationContext val context: Context,
        private val database: MusicDatabase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val browseId = savedStateHandle.get<String>("browseId")!!
        private val params =
            savedStateHandle
                .get<String>("params")
                ?.takeUnless { it.isBlank() || it == "null" }

        val title = MutableStateFlow("")
        val itemsPage = MutableStateFlow<ItemsPage?>(null)
        val itemsLayout = MutableStateFlow(ArtistItemsPageLayout.LIST)

        init {
            viewModelScope.launch {
                YouTube
                    .artistItems(
                        BrowseEndpoint(
                            browseId = browseId,
                            params = params,
                        ),
                    ).onSuccess { artistItemsPage ->
                        title.value = artistItemsPage.title
                        itemsLayout.value = artistItemsPage.layout
                        itemsPage.value =
                            ItemsPage(
                                items =
                                    artistItemsPage.items
                                        .distinctBy { it.id }
                                        .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                                        .filterVideo(context.dataStore.get(HideVideoKey, false))
                                        .filterBlockedArtists(database.getBlockedArtistIds().toSet()),
                                continuation = artistItemsPage.continuation,
                            )
                    }.onFailure {
                        reportException(it)
                    }
            }
        }

        fun loadMore() {
            viewModelScope.launch {
                val oldItemsPage = itemsPage.value ?: return@launch
                val continuation = oldItemsPage.continuation ?: return@launch
                YouTube
                    .artistItemsContinuation(continuation)
                    .onSuccess { artistItemsContinuationPage ->
                        itemsPage.update {
                            ItemsPage(
                                items =
                                    (oldItemsPage.items + artistItemsContinuationPage.items)
                                        .distinctBy { it.id }
                                        .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                                        .filterVideo(context.dataStore.get(HideVideoKey, false))
                                        .filterBlockedArtists(database.getBlockedArtistIds().toSet()),
                                continuation = artistItemsContinuationPage.continuation,
                            )
                        }
                    }.onFailure {
                        reportException(it)
                    }
            }
        }
    }
