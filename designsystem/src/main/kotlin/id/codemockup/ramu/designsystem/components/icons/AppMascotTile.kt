package id.codemockup.ramu.designsystem.components.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppControlTokens
import id.codemockup.ramu.designsystem.theme.AppRadius
import id.codemockup.ramu.designsystem.theme.RamuTheme

@Composable
fun AppMascotTile(
    modifier: Modifier = Modifier,
    size: Dp = AppControlTokens.compact,
    cornerRadius: Dp = AppRadius.control,
    background: Color = AppColors.primary.onyx,
    contentColor: Color = Color.White,
    glyph: String = "✳",
) {
    Surface(
        modifier = modifier.size(size).clearAndSetSemantics { },
        shape = RoundedCornerShape(cornerRadius),
        color = background,
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center,
        ) {
            AppText(
                text = glyph,
                style = AppTextStyle.Label,
                color = contentColor,
            )
        }
    }
}

@Preview(name = "Size 36dp")
@Composable
private fun AppMascotTile36dpPreview() {
    RamuTheme {
        AppMascotTile(size = 36.dp)
    }
}

@Preview(name = "Size 28dp")
@Composable
private fun AppMascotTile28dpPreview() {
    RamuTheme {
        AppMascotTile(size = 28.dp)
    }
}
