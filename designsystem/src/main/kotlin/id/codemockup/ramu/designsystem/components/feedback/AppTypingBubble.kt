package id.codemockup.ramu.designsystem.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.R
import id.codemockup.ramu.designsystem.components.icons.AppMascotTile
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.RamuTheme

private const val CANVAS_SCALE = 4.8f
private const val CANVAS_ASPECT = 1080f / 1920f
private const val DOTS_Y_OFFSET_FRACTION = 41f / 1080f

@Composable
fun AppTypingBubble(
    modifier: Modifier = Modifier,
    label: String = "Hermes is thinking…",
    avatar: @Composable (() -> Unit)? = null,
    animationWidth: Dp = 56.dp,
    animationHeight: Dp = 24.dp,
) {
    Row(
        modifier.semantics(mergeDescendants = true) { contentDescription = label },
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8),
        verticalAlignment = Alignment.Bottom,
    ) {
        avatar?.invoke()
        Surface(
            color = AppColors.neutral.surface,
            shape = RoundedCornerShape(
                topStart = AppRadius.card,
                topEnd = AppRadius.card,
                bottomEnd = AppRadius.card,
                bottomStart = AppSpacing.sm.sm4,
            ),
            border = BorderStroke(1.dp, AppColors.neutral.platinum),
        ) {
            Box(
                Modifier
                    .padding(horizontal = AppSpacing.md.md16, vertical = AppSpacing.sm.sm12)
                    .size(animationWidth, animationHeight)
                    .clipToBounds(),
                contentAlignment = Alignment.Center,
            ) {
                val canvasWidth = animationWidth * CANVAS_SCALE
                AppLottie(
                    rawRes = R.raw.loading_indicator,
                    size = Dp.Unspecified,
                    modifier = Modifier
                        .requiredSize(canvasWidth, canvasWidth * CANVAS_ASPECT)
                        .offset(y = -(canvasWidth * CANVAS_ASPECT * DOTS_Y_OFFSET_FRACTION)),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTypingBubblePreview() {
    RamuTheme {
        AppTypingBubble(
            modifier = Modifier.padding(AppSpacing.md.md16),
            avatar = { AppMascotTile(size = AppSpacing.lg.lg32, cornerRadius = AppRadius.small) },
        )
    }
}
