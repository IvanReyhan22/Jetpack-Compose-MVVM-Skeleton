package id.codemockup.ramu.designsystem.components.backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.RamuTheme

private const val DEFAULT_RADIUS_DP = 220f
private const val DEFAULT_HEIGHT_DP = 300f

/**
 * Applies a radial gradient glow effect at the top of the modifier.
 * The gradient is drawn behind the content using drawBehind.
 */
fun Modifier.appTopGlow(
    color: Color = AppColors.secondary.spark,
    alpha: Float = 0.55f,
    radius: Dp = DEFAULT_RADIUS_DP.dp,
    height: Dp = DEFAULT_HEIGHT_DP.dp,
): Modifier = drawWithCache {
    val radiusPx = radius.toPx()
    val heightPx = height.toPx()
    val glowColor = color.copy(alpha = alpha)

    onDrawBehind {
        val gradient = Brush.radialGradient(
            colors = listOf(glowColor, Color.Transparent),
            center = Offset(size.width / 2, -0.1f * heightPx),
            radius = radiusPx,
        )
        drawRect(
            gradient,
            topLeft = Offset(0f, 0f),
            size = androidx.compose.ui.geometry.Size(size.width, heightPx)
        )
    }
}

@Preview(name = "Top Glow Gradient", widthDp = 360, heightDp = 400)
@Composable
private fun AppTopGlowPreview() {
    RamuTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(AppColors.neutral.canvas)
                .appTopGlow()
        )
    }
}
