package id.codemockup.template.core.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import id.codemockup.template.core.designsystem.theme.AppColors
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

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: AppTextStyle = AppTextStyle.Body,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    val resolvedColor = when {
        color != Color.Unspecified -> color
        style == AppTextStyle.Caption || style == AppTextStyle.Meta -> AppColors.neutral.inkSecondary
        else -> Color.Unspecified
    }
    Text(
        text = if (style == AppTextStyle.Meta) text.uppercase(LocalConfiguration.current.locales[0]) else text,
        modifier = modifier,
        style = style.textStyle,
        color = resolvedColor,
        textAlign = textAlign,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
    )
}
