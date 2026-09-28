package id.codemockup.template.core.designsystem.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.components.text.AppText
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppRadius

/** Trigger for a parent owned select, date, or time picker. */
@Composable
fun AppPickerField(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier) {
        AppText(label, style = AppTextStyle.Label, modifier = Modifier.padding(bottom = 8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            shape = RoundedCornerShape(AppRadius.control),
            color = if (enabled) AppColors.neutral.surface else AppColors.neutral.surfaceDisabled,
            border = BorderStroke(1.dp, AppColors.neutral.ash),
        ) {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                leadingIcon?.invoke()
                AppText(value, modifier = Modifier.weight(1f), style = AppTextStyle.Body,
                    color = if (enabled) AppColors.primary.onyx else AppColors.neutral.inkDisabled)
                trailingIcon?.invoke()
            }
        }
    }
}

@Composable
fun AppEffortSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    valueLabel: String = "${(value * 100).toInt()}%",
) {
    Column(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AppText(label, style = AppTextStyle.Label)
            AppText(valueLabel, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
        }
        Slider(
            value = value.coerceIn(valueRange.start, valueRange.endInclusive),
            onValueChange = onValueChange,
            valueRange = valueRange,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = AppColors.primary.graphite,
                activeTrackColor = AppColors.primary.graphite,
                inactiveTrackColor = AppColors.neutral.platinum,
            ),
        )
    }
}

@Composable
fun AppRating(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Int = 5,
    enabled: Boolean = true,
) {
    require(maxValue > 0)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(maxValue) { index ->
            val selected = index < value
            Surface(
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    .clickable(enabled = enabled, role = Role.Button) { onValueChange(index + 1) }
                    .semantics { contentDescription = "Rate ${index + 1} of $maxValue" },
                shape = RoundedCornerShape(AppRadius.small),
                color = if (selected) AppColors.primary.onyx else AppColors.neutral.surfaceSubtle,
            ) {
                Box(contentAlignment = Alignment.Center) {}
            }
        }
    }
}
