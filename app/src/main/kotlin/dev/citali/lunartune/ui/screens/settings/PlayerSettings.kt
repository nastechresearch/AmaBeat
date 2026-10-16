/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.citali.amabeat.LocalPlayerAwareWindowInsets
import dev.citali.amabeat.R
import dev.citali.amabeat.constants.ArtistSeparatorsKey
import dev.citali.amabeat.constants.AudioNormalizationKey
import dev.citali.amabeat.constants.AudioOffload
import dev.citali.amabeat.constants.AudioQuality
import dev.citali.amabeat.constants.AudioQualityKey
import dev.citali.amabeat.constants.AutoDownloadOnLikeKey
import dev.citali.amabeat.constants.AutoSkipNextOnErrorKey
import dev.citali.amabeat.constants.AutoStartOnBluetoothKey
import dev.citali.amabeat.constants.CrossfadeCurve
import dev.citali.amabeat.constants.CrossfadeCurveKey
import dev.citali.amabeat.constants.CrossfadeDurationKey
import dev.citali.amabeat.constants.CrossfadeEnabledKey
import dev.citali.amabeat.constants.CrossfadeGaplessKey
import dev.citali.amabeat.constants.CrossfadeManualSkipKey
import dev.citali.amabeat.constants.DeviceMutePlaybackRecoveryVolumeKey
import dev.citali.amabeat.constants.ExternalDownloaderEnabledKey
import dev.citali.amabeat.constants.ExternalDownloaderPackageKey
import dev.citali.amabeat.constants.HISTORY_DURATION_DEFAULT
import dev.citali.amabeat.constants.HistoryDuration
import dev.citali.amabeat.constants.LowDataModeKey
import dev.citali.amabeat.constants.PauseOnDeviceMuteKey
import dev.citali.amabeat.constants.PermanentShuffleKey
import dev.citali.amabeat.constants.PersistentQueueKey
import dev.citali.amabeat.constants.PreloadNextSongKey
import dev.citali.amabeat.constants.PreloadQueueSongsCountKey
import dev.citali.amabeat.constants.SeekExtraSeconds
import dev.citali.amabeat.constants.SkipSilenceKey
import dev.citali.amabeat.constants.StopMusicOnTaskClearKey
import dev.citali.amabeat.constants.WakelockKey
import dev.citali.amabeat.ui.component.ArtistSeparatorsDialog
import dev.citali.amabeat.ui.component.CrossfadeCurvePreview
import dev.citali.amabeat.ui.component.CrossfadeSliderPreference
import dev.citali.amabeat.ui.component.EnumListPreference
import dev.citali.amabeat.ui.component.IconButton
import dev.citali.amabeat.ui.component.NumberPickerPreference
import dev.citali.amabeat.ui.component.PreferenceEntry
import dev.citali.amabeat.ui.component.PreferenceGroup
import dev.citali.amabeat.ui.component.SliderPreference
import dev.citali.amabeat.ui.component.SwitchPreference
import dev.citali.amabeat.ui.component.TextFieldDialog
import dev.citali.amabeat.ui.utils.backToMain
import dev.citali.amabeat.utils.rememberEnumPreference
import dev.citali.amabeat.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettings(navController: NavController) {
    val (audioQuality, onAudioQualityChange) =
        rememberEnumPreference(
            AudioQualityKey,
            defaultValue = AudioQuality.AUTO,
        )
    val (lowDataMode, onLowDataModeChange) =
        rememberPreference(
            LowDataModeKey,
            defaultValue = true,
        )
    val (preloadNextSong, onPreloadNextSongChange) =
        rememberPreference(
            PreloadNextSongKey,
            defaultValue = false,
        )
    val (preloadQueueSongsCount, onPreloadQueueSongsCountChange) =
        rememberPreference(
            PreloadQueueSongsCountKey,
            defaultValue = 1,
        )
    val (persistentQueue, onPersistentQueueChange) =
        rememberPreference(
            PersistentQueueKey,
            defaultValue = true,
        )
    val (permanentShuffle, onPermanentShuffleChange) =
        rememberPreference(
            PermanentShuffleKey,
            defaultValue = false,
        )
    val (skipSilence, onSkipSilenceChange) =
        rememberPreference(
            SkipSilenceKey,
            defaultValue = false,
        )
    val (audioNormalization, onAudioNormalizationChange) =
        rememberPreference(
            AudioNormalizationKey,
            defaultValue = true,
        )
    val (audioOffload, onAudioOffloadChange) =
        rememberPreference(
            AudioOffload,
            defaultValue = false,
        )

    val (seekExtraSeconds, onSeekExtraSeconds) =
        rememberPreference(
            SeekExtraSeconds,
            defaultValue = false,
        )

    val (autoDownloadOnLike, onAutoDownloadOnLikeChange) =
        rememberPreference(
            AutoDownloadOnLikeKey,
            defaultValue = false,
        )
    val (autoSkipNextOnError, onAutoSkipNextOnErrorChange) =
        rememberPreference(
            AutoSkipNextOnErrorKey,
            defaultValue = false,
        )
    val (pauseOnDeviceMute, onPauseOnDeviceMuteChange) =
        rememberPreference(
            PauseOnDeviceMuteKey,
            defaultValue = false,
        )
    val (
        deviceMutePlaybackRecoveryVolume,
        onDeviceMutePlaybackRecoveryVolumeChange,
    ) =
        rememberPreference(
            DeviceMutePlaybackRecoveryVolumeKey,
            defaultValue = 0,
        )
    val (autoStartOnBluetooth, onAutoStartOnBluetoothChange) =
        rememberPreference(
            AutoStartOnBluetoothKey,
            defaultValue = false,
        )
    val (stopMusicOnTaskClear, onStopMusicOnTaskClearChange) =
        rememberPreference(
            StopMusicOnTaskClearKey,
            defaultValue = false,
        )
    val (historyDuration, onHistoryDurationChange) =
        rememberPreference(
            HistoryDuration,
            defaultValue = HISTORY_DURATION_DEFAULT,
        )

    val (crossfadeEnabled, onCrossfadeEnabledChange) =
        rememberPreference(
            CrossfadeEnabledKey,
            defaultValue = false,
        )
    val (crossfadeDurationSeconds, onCrossfadeDurationSecondsChange) =
        rememberPreference(
            CrossfadeDurationKey,
            defaultValue = 5f,
        )
    val (crossfadeGapless, onCrossfadeGaplessChange) =
        rememberPreference(
            CrossfadeGaplessKey,
            defaultValue = true,
        )
    val (crossfadeManualSkip, onCrossfadeManualSkipChange) =
        rememberPreference(
            CrossfadeManualSkipKey,
            defaultValue = false,
        )
    val (crossfadeCurve, onCrossfadeCurveChange) =
        rememberEnumPreference(
            CrossfadeCurveKey,
            defaultValue = CrossfadeCurve.EQUAL_POWER,
        )

    val (artistSeparators, onArtistSeparatorsChange) =
        rememberPreference(
            ArtistSeparatorsKey,
            defaultValue = ",;/&",
        )
    val (externalDownloaderEnabled, onExternalDownloaderEnabledChange) =
        rememberPreference(
            ExternalDownloaderEnabledKey,
            defaultValue = false,
        )
    val (externalDownloaderPackage, onExternalDownloaderPackageChange) =
        rememberPreference(
            ExternalDownloaderPackageKey,
            defaultValue = "",
        )

    val (wakelockEnabled, onWakelockChange) =
        rememberPreference(
            WakelockKey,
            defaultValue = false,
        )

    var showArtistSeparatorsDialog by remember { mutableStateOf(false) }
    var showExternalDownloaderPackageDialog by remember { mutableStateOf(false) }

    if (showArtistSeparatorsDialog) {
        ArtistSeparatorsDialog(
            currentSeparators = artistSeparators,
            onDismiss = { showArtistSeparatorsDialog = false },
            onSave = { newSeparators ->
                onArtistSeparatorsChange(newSeparators)
                showArtistSeparatorsDialog = false
            },
        )
    }

    if (showExternalDownloaderPackageDialog) {
        TextFieldDialog(
            initialTextFieldValue =
                androidx.compose.ui.text.input
                    .TextFieldValue(externalDownloaderPackage),
            onDone = { pkg ->
                onExternalDownloaderPackageChange(pkg.trim())
                showExternalDownloaderPackageDialog = false
            },
            onDismiss = { showExternalDownloaderPackageDialog = false },
            singleLine = true,
            maxLines = 1,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.player_and_audio)) },
                navigationIcon = {
                    IconButton(
                        onClick = navController::navigateUp,
                        onLongClick = navController::backToMain,
                    ) {
                        Icon(
                            painterResource(R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding()

        Column(
            Modifier
                .padding(top = topPadding)
                .windowInsetsPadding(LocalPlayerAwareWindowInsets.current.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
                .verticalScroll(rememberScrollState())
                .padding(bottom = SettingsDimensions.ScreenBottomPadding),
        ) {
            PreferenceGroup(title = stringResource(R.string.player)) {
                item {
                    EnumListPreference(
                        title = { Text(stringResource(R.string.audio_quality)) },
                        icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
                        selectedValue = audioQuality,
                        onValueSelected = onAudioQualityChange,
                        valueText = {
                            when (it) {
                                AudioQuality.HIGHEST -> stringResource(R.string.audio_quality_max)
                                AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                                AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                                AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                            }
                        },
                    )
                }

                item {
                    PreferenceEntry(
                        title = { Text(stringResource(R.string.player_stream_client)) },
                        description = stringResource(R.string.player_stream_client_desc),
                        icon = { Icon(painterResource(R.drawable.integration), null) },
                        onClick = { navController.navigate("settings/player/stream_sources") },
                    )
                }

                item {
                    PreferenceEntry(
                        title = { Text(stringResource(R.string.mori_cipher_settings_title)) },
                        description = stringResource(R.string.mori_cipher_settings_description),
                        icon = { Icon(painterResource(R.drawable.security), null) },
                        onClick = { navController.navigate("settings/player/chiper") },
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.low_data_mode_title)) },
                        description = stringResource(R.string.low_data_mode_description),
                        icon = { Icon(painterResource(R.drawable.android_cell), null) },
                        checked = lowDataMode,
                        onCheckedChange = {
                            onLowDataModeChange(it)
                            if (it) onPreloadNextSongChange(false)
                        },
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.preload_next_song_title)) },
                        description = stringResource(R.string.preload_next_song_warning),
                        icon = { Icon(painterResource(R.drawable.error), null) },
                        checked = preloadNextSong,
                        onCheckedChange = onPreloadNextSongChange,
                        isEnabled = !lowDataMode,
                    )
                }

                item(visible = preloadNextSong) {
                    NumberPickerPreference(
                        title = { Text(stringResource(R.string.queue_songs_preload_count)) },
                        icon = { Icon(painterResource(R.drawable.queue_music), null) },
                        value = preloadQueueSongsCount,
                        onValueChange = onPreloadQueueSongsCountChange,
                        minValue = 1,
                        maxValue = 10,
                        valueText = { it.toString() },
                    )
                }

                item {
                    SliderPreference(
                        title = { Text(stringResource(R.string.history_duration)) },
                        icon = { Icon(painterResource(R.drawable.history), null) },
                        value = historyDuration,
                        onValueChange = onHistoryDurationChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.audio_crossfade_title)) },
                        description = stringResource(R.string.audio_crossfade_description),
                        icon = { Icon(painterResource(R.drawable.animation), null) },
                        checked = crossfadeEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                onAudioOffloadChange(false)
                            }
                            onCrossfadeEnabledChange(enabled)
                        },
                    )
                }

                item {
                    CrossfadeSliderPreference(
                        valueSeconds = crossfadeDurationSeconds,
                        onValueChange = onCrossfadeDurationSecondsChange,
                        isEnabled = crossfadeEnabled,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.crossfade_gapless_title)) },
                        description = stringResource(R.string.crossfade_gapless_description),
                        icon = { Icon(painterResource(R.drawable.fast_forward), null) },
                        checked = crossfadeGapless,
                        onCheckedChange = onCrossfadeGaplessChange,
                        isEnabled = crossfadeEnabled,
                    )
                }

                item {
                    EnumListPreference(
                        title = { Text(stringResource(R.string.crossfade_curve_title)) },
                        icon = { Icon(painterResource(R.drawable.animation), null) },
                        selectedValue = crossfadeCurve,
                        onValueSelected = onCrossfadeCurveChange,
                        valueText = {
                            when (it) {
                                CrossfadeCurve.EQUAL_POWER ->
                                    stringResource(R.string.crossfade_curve_entry_equal_power)
                                CrossfadeCurve.EASE_OUT_QUAD ->
                                    stringResource(R.string.crossfade_curve_entry_ease_out_quad)
                                CrossfadeCurve.EASE_OUT_CUBIC ->
                                    stringResource(R.string.crossfade_curve_entry_ease_out_cubic)
                                CrossfadeCurve.SMOOTHSTEP ->
                                    stringResource(R.string.crossfade_curve_entry_smoothstep)
                            }
                        },
                        isEnabled = crossfadeEnabled,
                    )
                }

                item {
                    CrossfadeCurvePreview(
                        curve = crossfadeCurve,
                        durationSeconds = crossfadeDurationSeconds,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.crossfade_manual_skip_title)) },
                        description = stringResource(R.string.crossfade_manual_skip_description),
                        icon = { Icon(painterResource(R.drawable.fast_forward), null) },
                        checked = crossfadeManualSkip,
                        onCheckedChange = onCrossfadeManualSkipChange,
                        isEnabled = crossfadeEnabled,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.skip_silence)) },
                        icon = { Icon(painterResource(R.drawable.fast_forward), null) },
                        checked = skipSilence,
                        onCheckedChange = onSkipSilenceChange,
                        isEnabled = !audioOffload,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.audio_normalization)) },
                        icon = { Icon(painterResource(R.drawable.volume_up), null) },
                        checked = audioNormalization,
                        onCheckedChange = onAudioNormalizationChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.audio_offload)) },
                        description = stringResource(R.string.audio_offload_desc),
                        icon = { Icon(painterResource(R.drawable.speed), null) },
                        checked = audioOffload,
                        onCheckedChange = { enabled ->
                            onAudioOffloadChange(enabled)
                            if (enabled) {
                                onSkipSilenceChange(false)
                                onCrossfadeEnabledChange(false)
                            }
                        },
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.seek_seconds_addup)) },
                        description = stringResource(R.string.seek_seconds_addup_description),
                        icon = { Icon(painterResource(R.drawable.arrow_forward), null) },
                        checked = seekExtraSeconds,
                        onCheckedChange = onSeekExtraSeconds,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.pause_on_device_mute)) },
                        description = stringResource(R.string.pause_on_device_mute_desc),
                        icon = { Icon(painterResource(R.drawable.volume_off), null) },
                        checked = pauseOnDeviceMute,
                        onCheckedChange = onPauseOnDeviceMuteChange,
                    )
                }

                item(visible = pauseOnDeviceMute) {
                    val context = LocalContext.current
                    val disabledLabel = stringResource(R.string.device_mute_recovery_volume_disabled)
                    val recoveryVolumeText =
                        remember(context, disabledLabel) {
                            { value: Int ->
                                if (value == 0) {
                                    disabledLabel
                                } else {
                                    context.getString(R.string.percentage_format, value)
                                }
                            }
                        }
                    NumberPickerPreference(
                        title = { Text(stringResource(R.string.device_mute_recovery_volume)) },
                        icon = { Icon(painterResource(R.drawable.volume_up), null) },
                        value = deviceMutePlaybackRecoveryVolume,
                        onValueChange = onDeviceMutePlaybackRecoveryVolumeChange,
                        minValue = 0,
                        maxValue = 100,
                        valueText = recoveryVolumeText,
                        isEnabled = pauseOnDeviceMute,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.auto_start_on_bluetooth)) },
                        description = stringResource(R.string.auto_start_on_bluetooth_desc),
                        icon = { Icon(painterResource(R.drawable.bluetooth), null) },
                        checked = autoStartOnBluetooth,
                        onCheckedChange = onAutoStartOnBluetoothChange,
                    )
                }
            }

            PreferenceGroup(title = stringResource(R.string.queue)) {
                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.persistent_queue)) },
                        description = stringResource(R.string.persistent_queue_desc),
                        icon = { Icon(painterResource(R.drawable.queue_music), null) },
                        checked = persistentQueue,
                        onCheckedChange = onPersistentQueueChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.permanent_shuffle)) },
                        description = stringResource(R.string.permanent_shuffle_desc),
                        icon = { Icon(painterResource(R.drawable.shuffle), null) },
                        checked = permanentShuffle,
                        onCheckedChange = onPermanentShuffleChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.auto_download_on_like)) },
                        description = stringResource(R.string.auto_download_on_like_desc),
                        icon = { Icon(painterResource(R.drawable.download), null) },
                        checked = autoDownloadOnLike,
                        onCheckedChange = onAutoDownloadOnLikeChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.auto_skip_next_on_error)) },
                        description = stringResource(R.string.auto_skip_next_on_error_desc),
                        icon = { Icon(painterResource(R.drawable.skip_next), null) },
                        checked = autoSkipNextOnError,
                        onCheckedChange = onAutoSkipNextOnErrorChange,
                    )
                }
            }

            PreferenceGroup(title = stringResource(R.string.misc)) {
                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.stop_music_on_task_clear)) },
                        icon = { Icon(painterResource(R.drawable.clear_all), null) },
                        checked = stopMusicOnTaskClear,
                        onCheckedChange = onStopMusicOnTaskClearChange,
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.wakelock)) },
                        description = stringResource(R.string.wakelock_desc),
                        icon = { Icon(painterResource(R.drawable.bolt), null) },
                        checked = wakelockEnabled,
                        onCheckedChange = onWakelockChange,
                    )
                }

                item {
                    PreferenceEntry(
                        title = { Text(stringResource(R.string.artist_separators)) },
                        description = artistSeparators.map { "\"$it\"" }.joinToString("  "),
                        icon = { Icon(painterResource(R.drawable.artist), null) },
                        onClick = { showArtistSeparatorsDialog = true },
                    )
                }

                item {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.external_downloader)) },
                        description = stringResource(R.string.external_downloader_desc),
                        icon = { Icon(painterResource(R.drawable.download), null) },
                        checked = externalDownloaderEnabled,
                        onCheckedChange = onExternalDownloaderEnabledChange,
                    )
                }

                item {
                    PreferenceEntry(
                        title = { Text(stringResource(R.string.external_downloader_package)) },
                        description = externalDownloaderPackage.ifEmpty { stringResource(R.string.external_downloader_package_desc) },
                        icon = { Icon(painterResource(R.drawable.integration), null) },
                        onClick = { showExternalDownloaderPackageDialog = true },
                        isEnabled = externalDownloaderEnabled,
                    )
                }
            }
        }
    }
}
