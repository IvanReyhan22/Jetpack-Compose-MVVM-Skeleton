package id.codemockup.ramu.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AppRadius {
    val controlSmall = 10.dp
    val controlLarge = 14.dp
    val floating = 18.dp
    val small = 8.dp
    val control = 12.dp
    val card = 16.dp
    val feature = 24.dp
    val pill = RoundedCornerShape(percent = 50)
}

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(AppRadius.small),
    small = RoundedCornerShape(AppRadius.control),
    medium = RoundedCornerShape(AppRadius.card),
    large = RoundedCornerShape(AppRadius.feature),
    extraLarge = RoundedCornerShape(AppRadius.feature),
)
