package id.codemockup.ramu.designsystem.components.buttons

import id.codemockup.ramu.designsystem.common.enums.AppButtonVariant
import id.codemockup.ramu.designsystem.common.action.ActionSurface
import id.codemockup.ramu.designsystem.common.enums.AppFabColor
import id.codemockup.ramu.designsystem.common.enums.AppFabSize
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.components.text.AppText

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.theme.*


@Composable
fun AppFloatingActionButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: AppFabColor = AppFabColor.Spark,
    size: AppFabSize = AppFabSize.Standard,
    icon: @Composable () -> Unit,
) {
    val dimension = when (size) {
        AppFabSize.Small -> AppControlTokens.compact
        AppFabSize.Standard -> AppControlTokens.floating
        AppFabSize.Capture -> AppControlTokens.capture
    }
    val shape = when (size) {
        AppFabSize.Small -> RoundedCornerShape(AppRadius.control)
        AppFabSize.Standard -> RoundedCornerShape(AppRadius.floating)
        AppFabSize.Capture -> CircleShape
    }
    ActionSurface(onClick, modifier, enabled,
        if (color == AppFabColor.Spark) AppButtonVariant.Spark else AppButtonVariant.Primary,
        shape, contentDescription) {
        Box(Modifier.size(dimension), contentAlignment = Alignment.Center) {
            Box(Modifier.size(if (size == AppFabSize.Small) AppControlTokens.iconSmall else AppControlTokens.icon),
                contentAlignment = Alignment.Center) { icon() }
        }
    }
}

@Composable
fun AppExtendedFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    expanded: Boolean = true,
    color: AppFabColor = AppFabColor.Onyx,
    icon: @Composable () -> Unit,
) {
    ActionSurface(onClick, modifier, enabled,
        if (color == AppFabColor.Spark) AppButtonVariant.Spark else AppButtonVariant.Primary,
        RoundedCornerShape(AppRadius.floating), if (expanded) null else text) {
        Row(Modifier.animateContentSize(AppMotion.tween(AppMotion.fast))
            .defaultMinSize(minWidth = AppControlTokens.floating, minHeight = AppControlTokens.floating)
            .padding(horizontal = if (expanded) AppSpacing.md.md16 else AppSpacing.sm.sm12,
                vertical = AppSpacing.sm.sm12),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Box(Modifier.size(AppControlTokens.icon), contentAlignment = Alignment.Center) { icon() }
            if (expanded) {
                Spacer(Modifier.width(AppSpacing.sm.sm8))
                AppText(text, Modifier.padding(end = AppSpacing.sm.sm4), style = AppTextStyle.Label,
                    textStyle = AppControlTokens.extendedLabel.let {
                        if (color == AppFabColor.Spark) it.copy(fontWeight = FontWeight.Bold) else it
                    })
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun AppFloatingActionButtonPreview() {
    RamuTheme {
        Column(
            Modifier.padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
        ) {
            AppFabColor.entries.forEach { color ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)) {
                    AppFabSize.entries.forEach { size ->
                        AppFloatingActionButton({}, "Add", color = color, size = size) { AppText("+") }
                    }
                    AppFloatingActionButton({}, "Disabled", color = color, enabled = false) { AppText("+") }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun AppExtendedFloatingActionButtonPreview() {
    RamuTheme {
        Column(
            Modifier.padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
        ) {
            AppFabColor.entries.forEach { color ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)) {
                    AppExtendedFloatingActionButton("Log session", {}, color = color) { AppText("+") }
                    AppExtendedFloatingActionButton("Log session", {}, color = color, expanded = false) { AppText("+") }
                }
                AppExtendedFloatingActionButton("Disabled", {}, color = color, enabled = false) { AppText("+") }
            }
        }
    }
}
