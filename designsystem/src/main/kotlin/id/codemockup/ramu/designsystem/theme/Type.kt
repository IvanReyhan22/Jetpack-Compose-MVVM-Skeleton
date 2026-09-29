package id.codemockup.ramu.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import id.codemockup.ramu.designsystem.R

@OptIn(ExperimentalTextApi::class)
val OnestFontFamily = FontFamily(
    (100..900 step 100).map { weight ->
        Font(
            resId = R.font.onest,
            weight = FontWeight(weight),
            variationSettings = FontVariation.Settings(FontVariation.weight(weight))
        )
    }
)

object AppTypography {
    private fun style(size: Int, height: Int, weight: Int, tracking: Float = 0f) = TextStyle(
        fontFamily = OnestFontFamily,
        fontSize = size.sp,
        lineHeight = height.sp,
        fontWeight = FontWeight(weight),
        letterSpacing = tracking.sp,
    )

    val display = style(40, 44, 700, -0.8f)
    val headline = style(32, 38, 700, -0.6f)
    val sectionTitle = style(24, 30, 700, -0.4f)
    val title = style(20, 26, 600, -0.2f)
    val titleSmall = style(16, 24, 600)
    val body = style(16, 24, 400)
    val bodySmall = style(14, 20, 400)
    val label = style(14, 20, 600)
    val caption = style(12, 16, 400)
    val meta = style(12, 16, 700).copy(letterSpacing = 0.04.em)
}

val Typography = with(AppTypography) {
    Typography(
        displayLarge = display, displayMedium = display, displaySmall = display,
        headlineLarge = headline, headlineMedium = headline, headlineSmall = sectionTitle,
        titleLarge = title, titleMedium = titleSmall, titleSmall = titleSmall,
        bodyLarge = body, bodyMedium = bodySmall, bodySmall = caption,
        labelLarge = label, labelMedium = label, labelSmall = meta,
    )
}
