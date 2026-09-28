package id.codemockup.template.core.designsystem.components.inputs

import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.components.text.AppText
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import id.codemockup.template.core.designsystem.theme.*

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    error: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    helperText: String? = null,
    success: Boolean = false,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    suffix: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    characterLimit: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = RoundedCornerShape(AppRadius.control),
    filled: Boolean = false,
    contentDescription: String? = null,
) {
    require(!label.isNullOrBlank() || !contentDescription.isNullOrBlank()) {
        "AppTextField requires a label or contentDescription"
    }
    require(characterLimit == null || characterLimit > 0) { "characterLimit must be positive" }
    val interactions = remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        when {
            !enabled -> AppColors.neutral.platinum
            error != null -> AppColors.negative.solid
            focused -> AppColors.primary.graphite
            success -> AppColors.success.solid
            filled -> androidx.compose.ui.graphics.Color.Transparent
            else -> AppColors.neutral.ash
        }, AppMotion.tween(AppMotion.fast), label = "field border"
    )
    val background = when {
        !enabled -> AppColors.neutral.surfaceDisabled
        filled -> AppColors.neutral.surfaceSubtle
        else -> AppColors.neutral.surface
    }
    val textColor = if (enabled) AppColors.primary.onyx else AppColors.neutral.inkDisabled
    val secondary = if (enabled) AppColors.neutral.inkSecondary else AppColors.neutral.inkDisabled
    val thickness =
        if (enabled && (focused || error != null)) AppControlTokens.activeBorder else AppControlTokens.border
    Column(modifier) {
        if (label != null) {
            AppText(
                label, style = AppTextStyle.Label, color = textColor,
                modifier = Modifier
                    .padding(bottom = AppSpacing.sm.sm8)
                    .clearAndSetSemantics {})
        }
        BasicTextField(
            value = value, onValueChange = onValueChange, enabled = enabled, readOnly = readOnly,
            textStyle = AppTypography.body.copy(color = textColor),
            keyboardOptions = keyboardOptions, keyboardActions = keyboardActions,
            visualTransformation = visualTransformation, singleLine = singleLine,
            minLines = minLines, maxLines = maxLines, interactionSource = interactions,
            cursorBrush = SolidColor(AppColors.primary.onyx),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    this.contentDescription = contentDescription ?: label.orEmpty()
                    if (!enabled) disabled()
                    if (error != null) error(error)
                },
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .clip(shape)
                        .background(background)
                        .border(thickness, borderColor, shape)
                        .heightIn(min = if (!singleLine) AppControlTokens.multilineField else if (filled) AppControlTokens.touchTarget else AppControlTokens.field)
                        .padding(horizontal = AppSpacing.md.md16, vertical = AppSpacing.sm.sm12),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
                ) {
                    CompositionLocalProvider(LocalContentColor provides secondary) {
                        leadingIcon?.invoke()
                        Box(Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) AppText(
                                placeholder,
                                color = secondary,
                                style = AppTextStyle.Body,
                                modifier = Modifier.clearAndSetSemantics {})
                            innerTextField()
                        }
                        if (suffix != null) AppText(
                            suffix,
                            style = AppTextStyle.BodySmall,
                            color = secondary
                        )
                        if (trailingIcon != null) trailingIcon()
                        else if (success && error == null) AppText(
                            "✓", color = if (enabled) AppColors.success.solid else secondary,
                            modifier = Modifier.semantics { this.contentDescription = "Validated" })
                    }
                }
            },
        )
        if (error != null || helperText != null || characterLimit != null) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.sm.sm4),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8)
            ) {
                val message = error ?: helperText
                if (message != null) AppText(
                    message,
                    Modifier
                        .weight(1f)
                        .semantics {
                            if (error != null) liveRegion = LiveRegionMode.Polite
                        },
                    style = AppTextStyle.Caption,
                    textStyle = if (error != null) AppTypography.caption.copy(fontWeight = FontWeight.SemiBold) else AppTypography.caption,
                    color = if (error != null && enabled) AppColors.negative.solid else secondary
                )
                else Spacer(Modifier.weight(1f))
                if (characterLimit != null) AppText(
                    "${value.length} / $characterLimit",
                    style = AppTextStyle.Caption,
                    color = secondary
                )
            }
        }
    }
}
