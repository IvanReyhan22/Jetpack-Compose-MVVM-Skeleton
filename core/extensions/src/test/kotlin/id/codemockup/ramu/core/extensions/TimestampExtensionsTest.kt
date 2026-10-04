package id.codemockup.ramu.core.extensions

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Test

class TimestampExtensionsTest {
    private val zone = ZoneId.of("Asia/Jakarta")
    private val clock = Clock.fixed(Instant.parse("2026-10-29T16:00:00Z"), zone)

    @Test
    fun formatsTodayYesterdayWeekdayAndOlderDate() {
        assertLabel("2026-10-29T14:32:00Z", "TODAY · 9:32 PM")
        assertLabel("2026-10-28T14:32:00Z", "YESTERDAY · 9:32 PM")
        assertLabel("2026-10-26T14:32:00Z", "Monday · 9:32 PM")
        assertLabel("2026-10-25T14:32:00Z", "25 Oct · 9:32 PM")
        assertLabel("2026-10-23T14:32:00Z", "23 Oct · 9:32 PM")
    }

    @Test
    fun usesLocalDateAndTwelveHourTime() {
        assertLabel("2026-10-28T17:00:00Z", "TODAY · 12:00 AM")
        assertLabel("2026-10-29T05:00:00Z", "TODAY · 12:00 PM")
    }

    @Test
    fun yesterdayTakesPrecedenceAcrossWeekBoundary() {
        val monday = Clock.fixed(Instant.parse("2026-10-26T03:00:00Z"), zone)
        assertEquals(
            "YESTERDAY · 9:32 PM",
            Instant.parse("2026-10-25T14:32:00Z").toEpochMilli().toRelativeDateTimeLabel(monday),
        )
    }

    private fun assertLabel(instant: String, expected: String) {
        assertEquals(expected, Instant.parse(instant).toEpochMilli().toRelativeDateTimeLabel(clock))
    }
}
