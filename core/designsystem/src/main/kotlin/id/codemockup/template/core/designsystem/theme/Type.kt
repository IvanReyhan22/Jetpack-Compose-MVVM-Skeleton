package id.codemockup.template.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import id.codemockup.template.core.designsystem.R

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

private val DefaultTypography = Typography()

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = OnestFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = OnestFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = OnestFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = OnestFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = OnestFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = OnestFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = OnestFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = OnestFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = OnestFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = OnestFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = OnestFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = OnestFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = OnestFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = OnestFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = OnestFontFamily)
)
