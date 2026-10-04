package id.codemockup.ramu.designsystem.components.text

import id.codemockup.ramu.designsystem.common.enums.AppTextStyle

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import id.codemockup.ramu.designsystem.theme.AppColors


@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: AppTextStyle = AppTextStyle.Body,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    textFontWeight: FontWeight? = null,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    textStyle: TextStyle = style.textStyle,
) {
    val resolvedColor = when {
        color != Color.Unspecified -> color
        style == AppTextStyle.Caption || style == AppTextStyle.Meta -> AppColors.neutral.inkSecondary
        else -> Color.Unspecified
    }
    Text(
        text = if (style == AppTextStyle.Meta) text.uppercase(LocalConfiguration.current.locales[0]) else text,
        modifier = modifier,
        style = textStyle,
        fontWeight = textFontWeight,
        color = resolvedColor,
        textAlign = textAlign,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
    )
}
