package id.codemockup.template.core.designsystem.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.components.text.AppText
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppTypography

@Composable
fun AppComposer(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Ask or tell Hermes…",
    enabled: Boolean = true,
    maxLines: Int = 4,
    action: @Composable (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = AppColors.neutral.surface,
        border = BorderStroke(1.dp, AppColors.neutral.ash),
    ) {
        Row(Modifier.padding(start = 16.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f).heightIn(min = 40.dp).padding(vertical = 8.dp),
                enabled = enabled,
                maxLines = maxLines,
                textStyle = AppTypography.body.copy(color = AppColors.primary.onyx),
                cursorBrush = SolidColor(AppColors.primary.onyx),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) AppText(placeholder, style = AppTextStyle.Body, color = AppColors.neutral.inkSecondary)
                        inner()
                    }
                },
            )
            action?.invoke()
        }
    }
}

@Composable
fun AppInlineEditableTitle(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier.heightIn(min = 48.dp).drawWithContent {
            drawContent()
            drawLine(
                AppColors.neutral.ash,
                start = androidx.compose.ui.geometry.Offset(0f, size.height - 1.dp.toPx()),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height - 1.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx())),
            )
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            singleLine = true,
            textStyle = AppTypography.sectionTitle.copy(color = AppColors.primary.onyx),
            cursorBrush = SolidColor(AppColors.primary.onyx),
        )
        trailingIcon?.invoke()
    }
}

@Composable
fun AppPromptField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Describe what you want to make…",
    enabled: Boolean = true,
    suggestions: @Composable RowScope.() -> Unit = {},
    action: @Composable (() -> Unit)? = null,
) {
    Surface(modifier = modifier, color = AppColors.primary.graphite, shape = RoundedCornerShape(24.dp)) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppText("✳ Hermes prompt", style = AppTextStyle.Meta, color = AppColors.neutral.darkSoft)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AppColors.neutral.darkField,
                border = BorderStroke(1.dp, AppColors.neutral.darkTrack),
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 88.dp).padding(16.dp),
                    enabled = enabled,
                    maxLines = 5,
                    textStyle = AppTypography.body.copy(color = Color.White),
                    cursorBrush = SolidColor(AppColors.secondary.spark),
                    decorationBox = { inner ->
                        Box {
                            if (value.isEmpty()) AppText(placeholder, style = AppTextStyle.Body, color = AppColors.neutral.darkMuted)
                            inner()
                        }
                    },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                suggestions()
                Spacer(Modifier.weight(1f))
                action?.invoke()
            }
        }
    }
}
