/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package moe.rukamori.amabeat.innertube.models.response

import kotlinx.serialization.Serializable
import moe.rukamori.amabeat.innertube.models.Continuation
import moe.rukamori.amabeat.innertube.models.ContinuationItemRenderer
import moe.rukamori.amabeat.innertube.models.MusicResponsiveListItemRenderer
import moe.rukamori.amabeat.innertube.models.Tabs

@Serializable
data class SearchResponse(
    val contents: Contents?,
    val continuationContents: ContinuationContents?,
) {
    @Serializable
    data class Contents(
        val tabbedSearchResultsRenderer: Tabs?,
    )

    @Serializable
    data class ContinuationContents(
        val musicShelfContinuation: MusicShelfContinuation,
    ) {
        @Serializable
        data class MusicShelfContinuation(
            val contents: List<Content>,
            val continuations: List<Continuation>?,
        ) {
            @Serializable
            data class Content(
                val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer? = null,
                val continuationItemRenderer: ContinuationItemRenderer? = null,
            )
        }
    }
}
