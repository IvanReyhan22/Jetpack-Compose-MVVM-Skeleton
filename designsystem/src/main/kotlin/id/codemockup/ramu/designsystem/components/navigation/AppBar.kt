package id.codemockup.ramu.designsystem.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.common.enums.AppIconName
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonVariant
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonShape
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonSize
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.components.buttons.AppIconButton
import id.codemockup.ramu.designsystem.components.icons.AppIcon
import id.codemockup.ramu.designsystem.components.icons.AppMascotTile
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.AppControlTokens
import id.codemockup.ramu.designsystem.theme.RamuTheme

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    transparent: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (transparent) Modifier else Modifier.drawBehind {
                    drawLine(
                        color = AppColors.neutral.platinum,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = AppControlTokens.border.toPx(),
                    )
                }
            ),
        color = if (transparent) Color.Transparent else AppColors.neutral.canvas,
        contentColor = AppColors.primary.onyx,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpacing.lg.lg64)
                .padding(horizontal = AppSpacing.sm.sm12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm12),
        ) {
            if (onBack != null) AppBarBackButton(onClick = onBack)
            leading?.invoke()
            Column(Modifier.weight(1f)) {
                AppText(title, style = AppTextStyle.TitleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (subtitle != null) {
                    AppText(subtitle, style = AppTextStyle.Caption, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8), content = actions)
        }
    }

}

@Composable
fun AppBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Back",
) {
    AppIconButton(
        onClick = onClick,
        contentDescription = contentDescription,
        modifier = modifier,
        variant = AppIconButtonVariant.Tonal,
        shape = AppIconButtonShape.Circle,
    ) {
        AppIcon(AppIconName.Back, contentDescription = null)
    }
}

@Composable
fun AppBarActionButton(
    icon: AppIconName,
    contentDescription: String,
    onClick: () -> Unit,
    size: AppIconButtonSize = AppIconButtonSize.Default,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AppIconButton(
        onClick = onClick,
        contentDescription = contentDescription,
        modifier = modifier,
        enabled = enabled,
        size=size,
        variant = AppIconButtonVariant.Tonal,
        shape = AppIconButtonShape.Circle,
    ) {
        AppIcon(icon, contentDescription = null)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun AppBarBasicPreview() {
    RamuTheme { AppBar(title = "Title") }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun AppBarFullPreview() {
    RamuTheme {
        AppBar(
            title = "Hermes",
            subtitle = "Ready to chat",
            onBack = {},
            leading = { AppMascotTile(size = AppControlTokens.buttonSmall) },
            actions = { AppBarActionButton(AppIconName.More, "More", onClick = {}) },
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun AppBarTransparentPreview() {
    RamuTheme { AppBar(title = "Title", onBack = {}, transparent = true) }
}

@Preview(showBackground = true, device = Devices.PIXEL_7)
@Composable
private fun AppBarLongTitlePreview() {
    RamuTheme {
        AppBar(
            title = "This is a very long title that should be truncated when it exceeds the available width",
            onBack = {},
        )
    }
}
