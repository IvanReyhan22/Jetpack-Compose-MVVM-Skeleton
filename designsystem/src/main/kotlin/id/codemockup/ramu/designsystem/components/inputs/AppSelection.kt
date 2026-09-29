package id.codemockup.ramu.designsystem.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.common.enums.AppCheckboxState
import id.codemockup.ramu.designsystem.common.enums.AppChipVariant
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius

@Composable
fun AppCheckbox(
    label: String,
    state: AppCheckboxState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val checked = state != AppCheckboxState.Unchecked
    val ink = if (enabled) AppColors.primary.onyx else AppColors.neutral.inkDisabled
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .triStateToggleable(
                state = when (state) {
                    AppCheckboxState.Unchecked -> ToggleableState.Off
                    AppCheckboxState.Checked -> ToggleableState.On
                    AppCheckboxState.Indeterminate -> ToggleableState.Indeterminate
                },
                enabled = enabled,
                role = Role.Checkbox,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier.size(22.dp)
                .background(if (checked) ink else AppColors.neutral.surface, RoundedCornerShape(7.dp))
                .border(1.5.dp, if (enabled) AppColors.primary.graphite else AppColors.neutral.platinum, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) AppText(if (state == AppCheckboxState.Checked) "✓" else "−", color = Color.White, style = AppTextStyle.Label)
        }
        AppText(label, modifier = Modifier.weight(1f), style = AppTextStyle.BodySmall, color = ink)
    }
}

@Composable
fun AppRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.heightIn(min = 48.dp).selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(22.dp).border(1.5.dp, if (enabled) AppColors.primary.graphite else AppColors.neutral.platinum, CircleShape), contentAlignment = Alignment.Center) {
            if (selected) Box(Modifier.size(10.dp).background(AppColors.primary.onyx, CircleShape))
        }
        AppText(label, modifier = Modifier.weight(1f), style = AppTextStyle.BodySmall, color = if (enabled) AppColors.primary.onyx else AppColors.neutral.inkDisabled)
    }
}

@Composable
fun AppSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.heightIn(min = 56.dp).toggleable(value = checked, enabled = enabled, role = Role.Switch) { onCheckedChange(it) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(Modifier.weight(1f)) {
            AppText(title, style = AppTextStyle.Label, color = if (enabled) AppColors.primary.onyx else AppColors.neutral.inkDisabled)
            if (supportingText != null) AppText(supportingText, style = AppTextStyle.Caption)
        }
        Box(Modifier.size(44.dp, 28.dp).background(if (checked) AppColors.primary.onyx else AppColors.neutral.ash, CircleShape)) {
            Box(Modifier.align(if (checked) Alignment.CenterEnd else Alignment.CenterStart).padding(horizontal = 4.dp).size(20.dp).background(Color.White, CircleShape))
        }
    }
}

@Composable
fun AppSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    require(options.isNotEmpty())
    Row(modifier.background(AppColors.neutral.surfaceSubtle, RoundedCornerShape(AppRadius.control)).padding(3.dp)) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Surface(
                modifier = Modifier.weight(1f).heightIn(min = 40.dp).clickable(enabled = enabled, role = Role.Tab) { onSelect(index) },
                shape = RoundedCornerShape(9.dp),
                color = if (selected) AppColors.neutral.surface else Color.Transparent,
                border = if (selected) BorderStroke(1.dp, AppColors.neutral.platinum) else null,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AppText(label, style = AppTextStyle.Label, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        color = if (!enabled) AppColors.neutral.inkDisabled else if (selected) AppColors.primary.onyx else AppColors.neutral.inkSecondary)
                }
            }
        }
    }
}

@Composable
fun AppStepper(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..Int.MAX_VALUE,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.heightIn(min = 56.dp).border(1.dp, AppColors.neutral.ash, RoundedCornerShape(AppRadius.control)).padding(start = 16.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(label, Modifier.weight(1f), style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
        StepperAction("−", enabled && value > range.first) { onValueChange(value - 1) }
        AppText(value.toString(), modifier = Modifier.widthIn(min = 40.dp), style = AppTextStyle.TitleSmall)
        StepperAction("+", enabled && value < range.last) { onValueChange(value + 1) }
    }
}

@Composable
private fun StepperAction(symbol: String, enabled: Boolean, onClick: () -> Unit) {
    val dark = symbol == "+"
    Box(
        Modifier.size(44.dp).background(if (dark) AppColors.primary.onyx else AppColors.neutral.surfaceSubtle, RoundedCornerShape(10.dp))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        AppText(symbol, style = AppTextStyle.Title, color = if (dark) Color.White else AppColors.primary.onyx)
    }
}

@Composable
fun AppChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AppChipVariant = AppChipVariant.Filter,
    selected: Boolean = false,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
) {
    val background = when {
        selected -> AppColors.primary.onyx
        variant == AppChipVariant.Assist -> AppColors.secondary.sparkTint
        variant == AppChipVariant.Input -> AppColors.neutral.surfaceSubtle
        else -> AppColors.neutral.surface
    }
    val ink = when {
        selected -> Color.White
        variant == AppChipVariant.Assist -> AppColors.secondary.onSparkTint
        else -> AppColors.primary.onyx
    }
    val height: Dp = if (variant == AppChipVariant.Input) 32.dp else if (variant == AppChipVariant.Assist) 36.dp else 40.dp
    Surface(
        modifier = modifier.heightIn(min = height).clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        color = containerColor ?: background, shape = AppRadius.pill,
        border = if (!selected && (variant == AppChipVariant.Filter || variant == AppChipVariant.Answer)) BorderStroke(1.dp, AppColors.neutral.ash) else null,
    ) {
        Box(Modifier.padding(horizontal = 14.dp), contentAlignment = Alignment.Center) {
            AppText(label, style = AppTextStyle.Label, color = if (enabled) contentColor ?: ink else AppColors.neutral.inkDisabled)
        }
    }
}

@Composable
fun AppOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    preview: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier.border(if (selected) 2.dp else 1.dp, if (selected) AppColors.primary.onyx else AppColors.neutral.ash, RoundedCornerShape(AppRadius.card))
            .clickable(role = Role.RadioButton, onClick = onClick).padding(if (selected) 14.dp else 15.dp),
    ) {
        if (preview != null) preview()
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppText(title, Modifier.weight(1f), style = AppTextStyle.Label)
            if (selected) AppText("✓", style = AppTextStyle.Label)
        }
        AppText(subtitle, style = AppTextStyle.Caption)
    }
}

@Composable
fun AppInputChip(
    label: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    removeContentDescription: String = "Remove $label",
) {
    Surface(modifier = modifier, shape = AppRadius.pill, color = AppColors.neutral.surfaceSubtle) {
        Row(Modifier.heightIn(min = 32.dp).padding(start = 12.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            AppText(label, style = AppTextStyle.Caption, color = AppColors.primary.onyx)
            Box(Modifier.size(32.dp).clickable(role = Role.Button, onClickLabel = removeContentDescription, onClick = onRemove),
                contentAlignment = Alignment.Center) {
                Box(Modifier.size(20.dp).background(AppColors.neutral.platinum, CircleShape), contentAlignment = Alignment.Center) {
                    AppText("×", style = AppTextStyle.Caption, color = AppColors.primary.onyx)
                }
            }
        }
    }
}
