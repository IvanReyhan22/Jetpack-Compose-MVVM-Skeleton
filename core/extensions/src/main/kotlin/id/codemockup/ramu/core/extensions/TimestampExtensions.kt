package id.codemockup.ramu.core.extensions

import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

fun Long.toRelativeDateTimeLabel(
    clock: Clock = Clock.systemDefaultZone(),
): String {
    val timestamp = Instant.ofEpochMilli(this).atZone(clock.zone)
    val date = timestamp.toLocalDate()
    val today = LocalDate.now(clock)
    val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val label = when {
        date == today -> "TODAY"
        date == today.minusDays(1) -> "YESTERDAY"
        !date.isBefore(weekStart) && !date.isAfter(today) ->
            timestamp.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))
        else -> timestamp.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH))
    }
    val time = timestamp.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
    return "$label · $time"
}
