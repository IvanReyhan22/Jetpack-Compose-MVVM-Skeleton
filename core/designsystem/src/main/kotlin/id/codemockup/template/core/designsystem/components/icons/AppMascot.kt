package id.codemockup.template.core.designsystem.components.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.common.enums.AppMascotMood

/** Small static cat mark for Hermes voice. Parent chooses mood and size. */
@Composable
fun AppMascot(
    mood: AppMascotMood,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    contentDescription: String? = "Hermes ${mood.name.lowercase()}",
) {
    Canvas(modifier.size(size).then(if (contentDescription != null) Modifier.semantics { this.contentDescription = contentDescription } else Modifier)) {
        val unit = this.size.minDimension / 100f
        val dark = AppColors.primary.onyx
        val ears = Path().apply {
            moveTo(16f * unit, 40f * unit)
            lineTo(20f * unit, 10f * unit)
            lineTo(40f * unit, 25f * unit)
            lineTo(60f * unit, 25f * unit)
            lineTo(80f * unit, 10f * unit)
            lineTo(84f * unit, 40f * unit)
            close()
        }
        drawPath(ears, dark)
        drawOval(dark, topLeft = Offset(12f * unit, 24f * unit), size = Size(76f * unit, 68f * unit))
        drawOval(AppColors.neutral.surface, topLeft = Offset(20f * unit, 32f * unit), size = Size(60f * unit, 52f * unit))
        when (mood) {
            AppMascotMood.Resting -> {
                drawLine(dark, Offset(33f * unit, 58f * unit), Offset(43f * unit, 58f * unit), 3f * unit, StrokeCap.Round)
                drawLine(dark, Offset(57f * unit, 58f * unit), Offset(67f * unit, 58f * unit), 3f * unit, StrokeCap.Round)
            }
            AppMascotMood.Happy -> {
                drawArc(dark, 0f, 180f, false, Offset(32f * unit, 53f * unit), Size(12f * unit, 8f * unit), style = Stroke(3f * unit))
                drawArc(dark, 0f, 180f, false, Offset(56f * unit, 53f * unit), Size(12f * unit, 8f * unit), style = Stroke(3f * unit))
            }
            else -> {
                drawCircle(dark, 2.5f * unit, Offset(38f * unit, 58f * unit))
                drawCircle(dark, 2.5f * unit, Offset(62f * unit, 58f * unit))
            }
        }
        drawCircle(dark, 2f * unit, Offset(50f * unit, 67f * unit))
        if (mood == AppMascotMood.Thinking) {
            drawCircle(AppColors.secondary.spark, 7f * unit, Offset(77f * unit, 20f * unit))
        }
        if (mood == AppMascotMood.Happy) {
            drawArc(dark, 0f, 180f, false, Offset(44f * unit, 66f * unit), Size(12f * unit, 8f * unit), style = Stroke(2f * unit))
        }
    }
}
