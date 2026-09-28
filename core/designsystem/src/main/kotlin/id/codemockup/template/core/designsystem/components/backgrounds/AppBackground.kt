package id.codemockup.template.core.designsystem.components.backgrounds

import id.codemockup.template.core.designsystem.common.enums.AppBackgroundVariant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.theme.AppColors


@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    variant: AppBackgroundVariant = AppBackgroundVariant.Canvas,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier.appBackground(variant), content = content)
}

fun Modifier.appBackground(
    variant: AppBackgroundVariant = AppBackgroundVariant.Canvas,
): Modifier = drawWithCache {
    val fieldHeight = size.height * 0.5f
    val step = when (variant) {
        AppBackgroundVariant.FieldGlow -> 14.dp
        AppBackgroundVariant.FieldDots -> 20.dp
        AppBackgroundVariant.FieldRuled -> 28.dp
        AppBackgroundVariant.FieldGrid -> 24.dp
        AppBackgroundVariant.Canvas -> 20.dp
    }.toPx()
    val dots = variant == AppBackgroundVariant.FieldGlow || variant == AppBackgroundVariant.FieldDots
    val radius = if (variant == AppBackgroundVariant.FieldDots) 1.3.dp.toPx() else 1.dp.toPx()
    val pattern = Path().apply {
        if (variant != AppBackgroundVariant.Canvas) {
            if (dots) {
                var y = step / 2f
                while (y < fieldHeight + radius) {
                    var x = step / 2f
                    while (x < size.width + radius) {
                        addOval(Rect(x - radius, y - radius, x + radius, y + radius))
                        x += step
                    }
                    y += step
                }
            } else {
                var y = 0f
                while (y < fieldHeight) {
                    moveTo(0f, y)
                    lineTo(size.width, y)
                    y += step
                }
                if (variant == AppBackgroundVariant.FieldGrid) {
                    var x = 0f
                    while (x < size.width) {
                        moveTo(x, 0f)
                        lineTo(x, fieldHeight)
                        x += step
                    }
                }
            }
        }
    }
    val patternColor = when (variant) {
        AppBackgroundVariant.FieldGlow -> AppColors.neutral.glowDot
        AppBackgroundVariant.FieldDots -> AppColors.neutral.fieldDot
        else -> AppColors.neutral.fieldLine
    }
    val glow = Brush.radialGradient(
        colors = listOf(AppColors.secondary.spark.copy(alpha = 0.9f), AppColors.secondary.spark.copy(alpha = 0f)),
        center = Offset(size.width * 0.85f, size.height * 0.12f),
        radius = 90.dp.toPx(),
    )
    val fade = Brush.verticalGradient(
        colors = listOf(AppColors.neutral.canvas.copy(alpha = 0f), AppColors.neutral.canvas),
        endY = fieldHeight.coerceAtLeast(1f),
    )
    val stroke = Stroke(width = 1.dp.toPx())
    onDrawBehind {
        drawRect(AppColors.neutral.canvas)
        if (variant != AppBackgroundVariant.Canvas && fieldHeight > 0f) {
            clipRect(bottom = fieldHeight) {
                if (dots) drawPath(pattern, patternColor)
                else drawPath(pattern, patternColor, style = stroke)
                if (variant == AppBackgroundVariant.FieldGlow) drawRect(glow)
                drawRect(fade, size = Size(size.width, fieldHeight))
            }
        }
    }
}
