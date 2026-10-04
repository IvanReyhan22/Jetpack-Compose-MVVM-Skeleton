package id.codemockup.ramu.feature.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.common.enums.AppIconName
import id.codemockup.ramu.designsystem.common.enums.AppIconButtonSize
import id.codemockup.ramu.designsystem.components.navigation.AppBarActionButton
import id.codemockup.ramu.designsystem.components.overlays.AppMenuItem
import id.codemockup.ramu.designsystem.components.overlays.AppOverflowMenu
import id.codemockup.ramu.designsystem.components.icons.AppIcon
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.RamuTheme

@Composable
fun ChatMenuButton(
    modifier: Modifier = Modifier,
    onNewSession: () -> Unit = {},
    onDeleteSession: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    val items = remember(onNewSession, onDeleteSession) {
        listOf(
            AppMenuItem(
                label = "New Session",
                onClick = onNewSession,
                icon = { AppIcon(AppIconName.Plus, contentDescription = null) },
            ),
            AppMenuItem(
                label = "Delete Session",
                onClick = onDeleteSession,
                destructive = true,
                icon = { AppIcon(AppIconName.Trash, contentDescription = null, color = AppColors.negative.solid) },
            ),
        )
    }

    Box(modifier) {
        AppBarActionButton(
            icon = AppIconName.More,
            contentDescription = "More options",
            onClick = { expanded = true },
            size = AppIconButtonSize.Compact,
        )
        AppOverflowMenu(
            expanded = expanded,
            onDismiss = { expanded = false },
            items = items,
        )
    }
}

@Preview
@Composable
private fun ChatMenuButtonClosedPreview() {
    RamuTheme {
        ChatMenuButton()
    }
}
