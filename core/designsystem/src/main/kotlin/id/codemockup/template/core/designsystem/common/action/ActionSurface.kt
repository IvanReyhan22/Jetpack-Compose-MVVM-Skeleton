package id.codemockup.template.core.designsystem.common.action

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import id.codemockup.template.core.designsystem.common.enums.AppButtonVariant
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppControlTokens
import id.codemockup.template.core.designsystem.theme.AppMotion

@Composable
internal fun ActionSurface(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    variant: AppButtonVariant,
    shape: Shape,
    description: String?,
    outlineUsesAsh: Boolean = false,
    content: @Composable () -> Unit,
) {
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val focused by interactions.collectIsFocusedAsState()
    val active = enabled && (pressed || focused)
    val colors = actionColors(variant, !enabled, active)
    val background by animateColorAsState(colors.background, AppMotion.tween(AppMotion.fast), label = "action background")
    val foreground by animateColorAsState(colors.content, AppMotion.tween(AppMotion.fast), label = "action content")
    val border = if (outlineUsesAsh && enabled) AppColors.neutral.ash else colors.border
    Surface(onClick = onClick, modifier = modifier.minimumInteractiveComponentSize().semantics {
        role = Role.Button
        if (description != null) contentDescription = description
    }, enabled = enabled, shape = shape, color = background, contentColor = foreground,
        border = if (border == Color.Transparent) null else BorderStroke(AppControlTokens.border, border),
        interactionSource = interactions, content = content)
}
