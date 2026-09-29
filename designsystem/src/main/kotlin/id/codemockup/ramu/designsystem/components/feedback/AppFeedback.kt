package id.codemockup.ramu.designsystem.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.common.enums.AppAlertVariant
import id.codemockup.ramu.designsystem.common.enums.AppMessageAuthor
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius

@Composable
fun AppInlineAlert(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    variant: AppAlertVariant = AppAlertVariant.Hermes,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val tint = when (variant) {
        AppAlertVariant.Hermes -> AppColors.secondary.sparkTint
        AppAlertVariant.Success -> AppColors.success.tint
        AppAlertVariant.Warning -> AppColors.warning.tint
        AppAlertVariant.Error -> AppColors.negative.tint
    }
    val ink = when (variant) {
        AppAlertVariant.Hermes -> AppColors.secondary.onSparkTint
        AppAlertVariant.Success -> AppColors.success.solid
        AppAlertVariant.Warning -> AppColors.warning.solid
        AppAlertVariant.Error -> AppColors.negative.solid
    }
    val symbol = when (variant) {
        AppAlertVariant.Hermes -> "✳"
        AppAlertVariant.Success -> "✓"
        AppAlertVariant.Warning -> "!"
        AppAlertVariant.Error -> "×"
    }
    Row(modifier.background(tint, RoundedCornerShape(AppRadius.card)).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(Modifier.size(28.dp).background(if (variant == AppAlertVariant.Hermes) AppColors.secondary.spark else Color.White, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            AppText(symbol, style = AppTextStyle.Label, color = ink)
        }
        Column(Modifier.weight(1f)) {
            AppText(title, style = AppTextStyle.Label, color = ink)
            AppText(message, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.height(4.dp))
                AppText(actionLabel, modifier = Modifier.clickable(onClick = onAction).padding(vertical = 8.dp), style = AppTextStyle.Label, color = ink)
            }
        }
    }
}

@Composable
fun AppSnackbarContent(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    dark: Boolean = true,
) {
    val background = if (dark) AppColors.primary.onyx else AppColors.neutral.surface
    val ink = if (dark) Color.White else AppColors.primary.onyx
    Surface(modifier = modifier, shape = RoundedCornerShape(AppRadius.control), color = background) {
        Row(Modifier.heightIn(min = 52.dp).padding(start = 16.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            icon?.invoke()
            AppText(message, modifier = Modifier.weight(1f), style = AppTextStyle.BodySmall, color = ink)
            if (actionLabel != null && onAction != null) {
                AppText(actionLabel, modifier = Modifier.clickable(onClick = onAction).padding(12.dp), style = AppTextStyle.Label,
                    color = if (dark) AppColors.secondary.spark else AppColors.primary.onyx)
            }
        }
    }
}

@Composable
fun AppLoadingIndicator(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = AppColors.primary.onyx,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CircularProgressIndicator(Modifier.size(20.dp), color = color, strokeWidth = 2.dp)
        AppText(label, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
    }
}

@Composable
fun AppSkeletonLine(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 12.dp,
) {
    Box(modifier.height(height).background(AppColors.neutral.surfaceSubtle, RoundedCornerShape(6.dp)))
}

@Composable
fun AppMessageBubble(
    text: String,
    author: AppMessageAuthor,
    modifier: Modifier = Modifier,
    avatar: @Composable (() -> Unit)? = null,
) {
    val user = author == AppMessageAuthor.User
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
        if (!user) avatar?.invoke()
        Surface(
            color = if (user) AppColors.primary.onyx else AppColors.neutral.surface,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = if (user) 4.dp else 16.dp,
                bottomStart = if (user) 16.dp else 4.dp,
            ),
            border = if (user) null else androidx.compose.foundation.BorderStroke(1.dp, AppColors.neutral.platinum),
        ) {
            AppText(text, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = AppTextStyle.BodySmall, color = if (user) Color.White else AppColors.primary.onyx)
        }
    }
}

@Composable
fun AppMessageAttachment(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    actionLabel: String? = null,
) {
    Surface(
        modifier = modifier.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(AppRadius.card),
        color = AppColors.neutral.canvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.neutral.platinum),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            icon?.invoke()
            Column(Modifier.weight(1f)) {
                AppText(title, style = AppTextStyle.Label)
                AppText(subtitle, style = AppTextStyle.Caption)
            }
            if (actionLabel != null) AppText(actionLabel, style = AppTextStyle.Label)
        }
    }
}
