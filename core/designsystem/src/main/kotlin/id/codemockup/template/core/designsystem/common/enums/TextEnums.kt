package id.codemockup.template.core.designsystem.common.enums

import androidx.compose.ui.text.TextStyle
import id.codemockup.template.core.designsystem.theme.AppTypography

enum class AppTextStyle(val textStyle: TextStyle) {
    Display(AppTypography.display),
    Headline(AppTypography.headline),
    SectionTitle(AppTypography.sectionTitle),
    Title(AppTypography.title),
    TitleSmall(AppTypography.titleSmall),
    Body(AppTypography.body),
    BodySmall(AppTypography.bodySmall),
    Label(AppTypography.label),
    Caption(AppTypography.caption),
    Meta(AppTypography.meta),
}
