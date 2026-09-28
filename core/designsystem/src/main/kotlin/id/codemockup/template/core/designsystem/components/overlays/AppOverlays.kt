package id.codemockup.template.core.designsystem.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.common.enums.AppButtonVariant
import id.codemockup.template.core.designsystem.common.enums.AppProposalChange
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.components.buttons.AppButton
import id.codemockup.template.core.designsystem.components.text.AppText
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppRadius
import id.codemockup.template.core.designsystem.components.inputs.AppChip
import id.codemockup.template.core.designsystem.components.inputs.AppTextField

@Composable
fun AppConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissLabel: String = "Cancel",
    destructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius.feature),
        containerColor = AppColors.neutral.surface,
        title = { AppText(title, style = AppTextStyle.Title) },
        text = { AppText(message, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary) },
        confirmButton = {
            AppButton(confirmLabel, onConfirm, variant = if (destructive) AppButtonVariant.Destructive else AppButtonVariant.Primary)
        },
        dismissButton = { AppButton(dismissLabel, onDismiss, variant = AppButtonVariant.Tonal) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppColors.neutral.canvas,
        shape = RoundedCornerShape(topStart = AppRadius.feature, topEnd = AppRadius.feature),
        scrimColor = AppColors.neutral.scrim,
    ) {
        Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            AppText(title, style = AppTextStyle.Title, modifier = Modifier.padding(bottom = 12.dp))
            content()
        }
    }
}

data class AppMenuItem(
    val label: String,
    val onClick: () -> Unit,
    val destructive: Boolean = false,
    val icon: (@Composable () -> Unit)? = null,
)

@Composable
fun AppOverflowMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    items: List<AppMenuItem>,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier.background(AppColors.neutral.surface),
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { AppText(item.label, style = AppTextStyle.BodySmall, color = if (item.destructive) AppColors.negative.solid else AppColors.primary.onyx) },
                onClick = { onDismiss(); item.onClick() },
                leadingIcon = item.icon,
                modifier = Modifier.heightIn(min = 44.dp),
            )
        }
    }
}

@Composable
fun AppTooltipBubble(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showArrow: Boolean = true,
) {
    Column(modifier) {
        Box(
            Modifier.background(AppColors.primary.onyx, RoundedCornerShape(AppRadius.small))
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            AppText(text, style = AppTextStyle.Caption, color = Color.White)
        }
        if (showArrow) Canvas(Modifier.padding(start = 20.dp).size(10.dp, 6.dp)) {
            val triangle = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(triangle, AppColors.primary.onyx)
        }
    }
}

data class AppProposalItem(val label: String, val change: AppProposalChange)

@Composable
fun AppProposalDiffRow(
    item: AppProposalItem,
    modifier: Modifier = Modifier,
) {
    val symbol = when (item.change) {
        AppProposalChange.Add -> "+"
        AppProposalChange.Modify -> "~"
        AppProposalChange.Remove -> "−"
        AppProposalChange.Unchanged -> "="
    }
    val color = when (item.change) {
        AppProposalChange.Add -> AppColors.success.solid
        AppProposalChange.Modify -> AppColors.warning.solid
        AppProposalChange.Remove -> AppColors.negative.solid
        AppProposalChange.Unchanged -> AppColors.neutral.inkSecondary
    }
    val tint = when (item.change) {
        AppProposalChange.Add -> AppColors.success.tint
        AppProposalChange.Modify -> AppColors.warning.tint
        AppProposalChange.Remove -> AppColors.negative.tint
        AppProposalChange.Unchanged -> AppColors.neutral.surfaceSubtle
    }
    Row(modifier.heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(24.dp).background(tint, RoundedCornerShape(7.dp)), contentAlignment = Alignment.Center) {
            AppText(symbol, style = AppTextStyle.Label, color = color)
        }
        AppText(item.label, style = AppTextStyle.BodySmall,
            color = if (item.change == AppProposalChange.Remove) AppColors.neutral.inkSecondary else AppColors.primary.onyx,
            textStyle = if (item.change == AppProposalChange.Remove)
                id.codemockup.template.core.designsystem.theme.AppTypography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
            else id.codemockup.template.core.designsystem.theme.AppTypography.bodySmall,
        )
    }
}

@Composable
fun AppProposalSheet(
    title: String,
    items: List<AppProposalItem>,
    onAccept: () -> Unit,
    onKeep: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    acceptLabel: String = "Use this layout ↗",
    keepLabel: String = "Keep current view",
    eyebrow: String? = null,
) {
    AppModalSheet(title, onDismiss, modifier) {
        if (eyebrow != null) AppText(eyebrow, style = AppTextStyle.Meta)
        Surface(shape = RoundedCornerShape(AppRadius.card), color = AppColors.neutral.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.neutral.platinum)) {
            Column(Modifier.padding(horizontal = 14.dp)) {
                items.forEachIndexed { index, item ->
                    AppProposalDiffRow(item, Modifier.fillMaxWidth())
                    if (index < items.lastIndex) HorizontalDivider(color = AppColors.neutral.platinum)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        AppButton(acceptLabel, onAccept, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        AppButton(keepLabel, onKeep, modifier = Modifier.fillMaxWidth(), variant = AppButtonVariant.Secondary)
    }
}

@Composable
fun AppCaptureSheet(
    value: String,
    onValueChange: (String) -> Unit,
    types: List<String>,
    selectedType: Int,
    onSelectType: (Int) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Capture",
    saveLabel: String = "Save",
    placeholder: String = "Capture a thought…",
    voiceAction: @Composable (() -> Unit)? = null,
) {
    AppModalSheet(title, onDismiss, modifier) {
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            contentDescription = title,
            singleLine = false,
            minLines = 3,
        )
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            types.forEachIndexed { index, type ->
                AppChip(type, { onSelectType(index) }, selected = selectedType == index)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            voiceAction?.invoke()
            AppButton(saveLabel, onSave, modifier = Modifier.weight(1f))
        }
    }
}
