package id.codemockup.ramu.designsystem.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.common.enums.AppCardVariant
import id.codemockup.ramu.designsystem.components.text.AppText
import androidx.compose.ui.Alignment

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    variant: AppCardVariant = AppCardVariant.Base,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(
        if (variant in listOf(
                AppCardVariant.Graphite,
                AppCardVariant.Hero
            )
        ) 24.dp else 16.dp
    ),
    containerColor: Color? = null,
    foregroundColor: Color? = null,
    shape: Shape? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val background = when (variant) {
        AppCardVariant.Subtle -> AppColors.neutral.surfaceSubtle
        AppCardVariant.Graphite -> AppColors.primary.graphite
        AppCardVariant.SparkTint -> AppColors.secondary.sparkTint
        AppCardVariant.Hero -> AppColors.primary.onyx
        AppCardVariant.Placeholder -> Color.Transparent
        else -> AppColors.neutral.surface
    }
    val border = when (variant) {
        AppCardVariant.Base -> BorderStroke(1.dp, AppColors.neutral.platinum)
        AppCardVariant.Selected -> BorderStroke(2.dp, AppColors.primary.onyx)
        else -> null
    }
    val resolvedShape = shape ?: RoundedCornerShape(
        if (variant in listOf(
                AppCardVariant.Graphite,
                AppCardVariant.SparkTint,
                AppCardVariant.Hero
            )
        ) AppRadius.feature else AppRadius.card
    )
    val dashed = if (variant == AppCardVariant.Placeholder) Modifier.drawWithContent {
        drawContent()
        val stroke = 1.dp.toPx()
        drawRoundRect(
            color = AppColors.neutral.ash,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(AppRadius.card.toPx()),
            style = Stroke(
                stroke,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()))
            ),
        )
    } else Modifier
    Surface(
        modifier = (if (onClick != null) modifier.clickable(onClick = onClick) else modifier).then(
            dashed
        ),
        shape = resolvedShape,
        color = containerColor ?: background,
        contentColor = foregroundColor ?: if (variant in listOf(
                AppCardVariant.Graphite,
                AppCardVariant.Hero
            )
        ) Color.White else AppColors.primary.onyx,
        border = border,
    ) {
        Box {
            if (variant == AppCardVariant.Hero) {
                Canvas(Modifier.matchParentSize()) {
                    drawRect(
                        Brush.radialGradient(
                            colors = listOf(
                                AppColors.secondary.spark.copy(alpha = .32f),
                                Color.Transparent
                            ),
                            center = Offset(size.width, 0f), radius = 180.dp.toPx(),
                        )
                    )
                    val step = 14.dp.toPx()
                    var x = 0f
                    while (x < size.width) {
                        var y = 0f
                        while (y < size.height) {
                            drawCircle(
                                AppColors.neutral.darkTrack.copy(alpha = .7f),
                                1.dp.toPx(),
                                Offset(x, y)
                            )
                            y += step
                        }
                        x += step
                    }
                }
            }
            Column(
                Modifier
                    .padding(contentPadding)
                    .padding(end = if (variant == AppCardVariant.Selected) 24.dp else 0.dp),
                content = content
            )
            if (variant == AppCardVariant.Selected) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(22.dp)
                        .background(AppColors.primary.onyx, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AppText("✓", style = AppTextStyle.Caption, color = Color.White)
                }
            }
        }
    }
}
