package id.codemockup.ramu.designsystem.components.buttons

import id.codemockup.ramu.designsystem.common.enums.AppButtonVariant
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonVariant
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonShape
import id.codemockup.ramu.designsystem.components.text.AppText

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.common.action.ActionSurface
import id.codemockup.ramu.designsystem.theme.*


@Composable
fun AppIconButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: AppIconButtonVariant = AppIconButtonVariant.Tonal,
    shape: AppIconButtonShape = AppIconButtonShape.Circle,
    showBadge: Boolean = false,
    icon: @Composable () -> Unit,
) {
    val buttonVariant = when (variant) {
        AppIconButtonVariant.Filled -> AppButtonVariant.Primary
        AppIconButtonVariant.Spark -> AppButtonVariant.Spark
        AppIconButtonVariant.Tonal -> AppButtonVariant.Tonal
        AppIconButtonVariant.Outline -> AppButtonVariant.Secondary
        AppIconButtonVariant.Ghost -> AppButtonVariant.Text
    }
    ActionSurface(onClick, modifier, enabled, buttonVariant,
        if (shape == AppIconButtonShape.Circle) CircleShape else RoundedCornerShape(AppRadius.control),
        contentDescription, outlineUsesAsh = variant == AppIconButtonVariant.Outline) {
        Box(Modifier.size(if (shape == AppIconButtonShape.Circle) AppControlTokens.touchTarget else AppControlTokens.compact),
            contentAlignment = Alignment.Center) {
            Box(Modifier.size(AppControlTokens.icon), contentAlignment = Alignment.Center) { icon() }
            if (showBadge) Box(Modifier.align(Alignment.TopEnd).padding(AppSpacing.sm.sm8)
                .size(AppControlTokens.badge).background(AppColors.negative.solid, CircleShape))
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun AppIconButtonPreview() {
    RamuTheme {
        Column(
            Modifier.padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
        ) {
            AppIconButtonVariant.entries.forEach { variant ->
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)) {
                    AppIconButton({}, "Add", variant = variant, showBadge = true) { AppText("+") }
                    AppIconButton({}, "More", variant = variant, shape = AppIconButtonShape.Square) { AppText("…") }
                    AppIconButton({}, "Disabled", variant = variant, enabled = false) { AppText("+") }
                }
            }
        }
    }
}
