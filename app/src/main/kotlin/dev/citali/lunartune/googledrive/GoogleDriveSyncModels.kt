/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.googledrive

import dev.citali.amabeat.backup.ScheduledBackupFrequency

data class GoogleDriveSyncSettings(
    val enabled: Boolean = false,
    val frequency: ScheduledBackupFrequency = ScheduledBackupFrequency.WEEKLY,
    val customDateEpochDay: Long? = null,
    val remoteFolderUri: String? = null,
    val remoteFolderName: String? = null,
    val overwriteExisting: Boolean = false,
    val lastSyncEpochMs: Long? = null,
    val lastSyncFailed: Boolean = false,
)
