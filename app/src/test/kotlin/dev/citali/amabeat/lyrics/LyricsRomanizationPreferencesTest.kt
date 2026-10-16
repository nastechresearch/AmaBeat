/*
 * AmaBeat (2026)
 * © nastechresearch — github.com/nastechresearch
 * GPL-3.0 License | Contributors: see git history
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package dev.citali.amabeat.lyrics

import dev.citali.amabeat.lyrics.LyricsUtils.shouldRomanizeLyricsLine
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LyricsRomanizationPreferencesTest {
    @Test
    fun japaneseRomanizationUsesItsLanguagePreference() {
        val preferences =
            LyricsRomanizationPreferences(
                romanizeJapanese = true,
                romanizeKorean = false,
                romanizeChinese = false,
                romanizeHindi = false,
                romanizeOther = false,
            )

        assertTrue(shouldRomanizeLyricsLine("日本語の歌詞", preferences))
    }

    @Test
    fun disabledJapaneseRomanizationDoesNotScheduleLocalWork() {
        val preferences =
            LyricsRomanizationPreferences(
                romanizeJapanese = false,
                romanizeKorean = false,
                romanizeChinese = false,
                romanizeHindi = false,
                romanizeOther = false,
            )

        assertFalse(shouldRomanizeLyricsLine("日本語の歌詞", preferences))
    }
}
