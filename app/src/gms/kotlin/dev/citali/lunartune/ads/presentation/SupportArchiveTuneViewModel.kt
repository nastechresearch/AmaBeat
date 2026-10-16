/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.ads.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import dev.citali.amabeat.ads.domain.OpenSupportPageUseCase
import dev.citali.amabeat.ads.domain.SupportPageOpenResult
import javax.inject.Inject

internal sealed interface SupportAmaBeatScreenState {
    @Immutable
    data object Loading : SupportAmaBeatScreenState

    @Immutable
    data object Success : SupportAmaBeatScreenState

    @Immutable
    data object Empty : SupportAmaBeatScreenState

    @Immutable
    data class Error(
        val reason: SupportAmaBeatError,
    ) : SupportAmaBeatScreenState
}

internal enum class SupportAmaBeatError {
    PageUnavailable,
}

internal enum class SupportAmaBeatUiEvent {
    OpenFailed,
}

@HiltViewModel
internal class SupportAmaBeatViewModel
    @Inject
    constructor(
        private val openSupportPage: OpenSupportPageUseCase,
    ) : ViewModel() {
        private val _screenState =
            MutableStateFlow<SupportAmaBeatScreenState>(SupportAmaBeatScreenState.Success)
        val screenState: StateFlow<SupportAmaBeatScreenState> = _screenState.asStateFlow()

        private val eventChannel = Channel<SupportAmaBeatUiEvent>(Channel.BUFFERED)
        val events = eventChannel.receiveAsFlow()

        fun onSupportAmaBeatClick() {
            if (_screenState.value is SupportAmaBeatScreenState.Loading) return
            _screenState.value = SupportAmaBeatScreenState.Loading
            when (openSupportPage()) {
                SupportPageOpenResult.Opened -> {
                    _screenState.value = SupportAmaBeatScreenState.Success
                }

                SupportPageOpenResult.Unavailable -> {
                    _screenState.value =
                        SupportAmaBeatScreenState.Error(SupportAmaBeatError.PageUnavailable)
                    eventChannel.trySend(SupportAmaBeatUiEvent.OpenFailed)
                }
            }
        }
    }
