package id.codemockup.ramu.designsystem.components.lists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.common.enums.AppBadgeVariant
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius

@Composable
fun AppBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: AppBadgeVariant = AppBadgeVariant.Neutral,
    backgroundColor: Color? = null,
    contentColor: Color? = null,
) {
    val background = when (variant) {
        AppBadgeVariant.Dark -> AppColors.primary.onyx
        AppBadgeVariant.Spark -> AppColors.secondary.spark
        AppBadgeVariant.SparkTint -> AppColors.secondary.sparkTint
        AppBadgeVariant.Success -> AppColors.success.tint
        AppBadgeVariant.Warning -> AppColors.warning.tint
        AppBadgeVariant.Error -> AppColors.negative.tint
        AppBadgeVariant.Outline -> AppColors.neutral.surface
        else -> AppColors.neutral.surfaceSubtle
    }
    val ink = when (variant) {
        AppBadgeVariant.Dark -> Color.White
        AppBadgeVariant.SparkTint -> AppColors.secondary.onSparkTint
        AppBadgeVariant.Success -> AppColors.success.solid
        AppBadgeVariant.Warning -> AppColors.warning.solid
        AppBadgeVariant.Error -> AppColors.negative.solid
        else -> AppColors.primary.onyx
    }
    Surface(
        modifier = modifier,
        shape = AppRadius.pill,
        color = backgroundColor ?: background,
        border = if (variant == AppBadgeVariant.Outline) BorderStroke(1.dp, AppColors.neutral.ash) else null,
    ) {
        AppText(text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = AppTextStyle.Caption, color = contentColor ?: ink)
    }
}

@Composable
fun AppCounter(
    count: Int,
    modifier: Modifier = Modifier,
    maxCount: Int = 9,
    color: Color = AppColors.primary.onyx,
) {
    val text = if (count > maxCount) "$maxCount+" else count.toString()
    Surface(modifier = modifier, shape = CircleShape, color = color) {
        Box(Modifier.defaultMinSize(minWidth = 22.dp, minHeight = 22.dp).padding(horizontal = 5.dp), contentAlignment = Alignment.Center) {
            AppText(text, style = AppTextStyle.Caption, color = Color.White)
        }
    }
}

@Composable
fun AppAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    background: Color = AppColors.neutral.ash,
    contentColor: Color = AppColors.primary.onyx,
) {
    Surface(modifier = modifier.size(size), shape = CircleShape, color = background) {
        Box(contentAlignment = Alignment.Center) {
            AppText(initials, style = AppTextStyle.Label, color = contentColor, maxLines = 1)
        }
    }
}

@Composable
fun AppListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = (if (onClick != null) modifier.clickable(onClick = onClick) else modifier)
            .heightIn(min = 64.dp).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        leading?.invoke()
        Column(Modifier.weight(1f)) {
            AppText(title, style = AppTextStyle.TitleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) AppText(subtitle, style = AppTextStyle.Caption, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        trailing?.invoke()
    }
}

@Composable
fun AppStatusDot(
    modifier: Modifier = Modifier,
    color: Color = AppColors.secondary.spark,
    size: Dp = 8.dp,
) {
    Box(modifier.size(size).background(color, CircleShape))
}

@Composable
fun AppAvatarStack(
    initials: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    maxVisible: Int = 3,
) {
    require(maxVisible > 0)
    val visible = initials.size.coerceAtMost(maxVisible)
    val total = visible + if (initials.size > maxVisible) 1 else 0
    Box(modifier.width(if (total == 0) 0.dp else size + (size - 8.dp) * (total - 1)).height(size)) {
        initials.take(maxVisible).forEachIndexed { index, value ->
            AppAvatar(value, Modifier.offset(x = (size - 8.dp) * index), size = size,
                background = if (index % 2 == 0) AppColors.neutral.ash else AppColors.neutral.inkSecondary,
                contentColor = if (index % 2 == 0) AppColors.primary.onyx else Color.White)
        }
        if (initials.size > maxVisible) {
            AppAvatar("+${initials.size - maxVisible}", Modifier.offset(x = (size - 8.dp) * maxVisible), size = size,
                background = AppColors.neutral.surfaceSubtle)
        }
    }
}
