package id.codemockup.ramu.designsystem.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.components.inputs.AppCheckbox
import id.codemockup.ramu.designsystem.common.enums.AppCheckboxState
import id.codemockup.ramu.designsystem.common.enums.AppCardVariant
import id.codemockup.ramu.designsystem.components.lists.AppBadge
import id.codemockup.ramu.designsystem.common.enums.AppBadgeVariant
import id.codemockup.ramu.designsystem.components.navigation.AppLinearProgress
import id.codemockup.ramu.designsystem.components.buttons.AppButton
import id.codemockup.ramu.designsystem.common.enums.AppButtonVariant
import id.codemockup.ramu.designsystem.components.icons.AppMascot
import id.codemockup.ramu.designsystem.common.enums.AppMascotMood
import id.codemockup.ramu.designsystem.common.enums.AppChipVariant
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors

@Composable
fun AppTaskCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    metadata: String? = null,
    badge: String? = null,
    badgeVariant: AppBadgeVariant = AppBadgeVariant.Error,
    onClick: (() -> Unit)? = null,
) {
    AppCard(modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppCheckbox("", if (checked) AppCheckboxState.Checked else AppCheckboxState.Unchecked, { onCheckedChange(!checked) }, Modifier.width(24.dp))
            AppText(title, Modifier.weight(1f), style = AppTextStyle.TitleSmall)
            if (badge != null) AppBadge(badge, variant = badgeVariant)
        }
        if (metadata != null) AppText(metadata, modifier = Modifier.padding(start = 36.dp, top = 4.dp), style = AppTextStyle.Caption)
    }
}

@Composable
fun AppEventCard(
    dayOfWeek: String,
    day: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
) {
    AppCard(modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            AppCard(variant = AppCardVariant.Hero, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)) {
                AppText(dayOfWeek, style = AppTextStyle.Meta, color = AppColors.secondary.spark)
                AppText(day, style = AppTextStyle.Title)
            }
            Column(Modifier.weight(1f)) {
                AppText(title, style = AppTextStyle.TitleSmall)
                AppText(subtitle, style = AppTextStyle.Caption)
                if (footer != null) { Spacer(Modifier.height(8.dp)); footer() }
            }
        }
    }
}

@Composable
fun AppIdeaCard(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    status: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    AppCard(modifier, onClick = onClick) {
        AppText(title, style = AppTextStyle.TitleSmall)
        AppText(summary, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
        if (status != null || actionLabel != null) {
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                if (status != null) AppBadge(status, variant = AppBadgeVariant.SparkTint)
                if (actionLabel != null && onAction != null) {
                    androidx.compose.material3.TextButton(onClick = onAction) {
                        AppText(actionLabel, style = AppTextStyle.Label, color = AppColors.primary.onyx)
                    }
                }
            }
        }
    }
}

@Composable
fun AppSpaceCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    preview: @Composable (() -> Unit)? = null,
    hasUpdate: Boolean = false,
) {
    AppCard(modifier, onClick = onClick, contentPadding = PaddingValues(12.dp)) {
        preview?.invoke()
        Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                AppText(title, style = AppTextStyle.TitleSmall)
                AppText(subtitle, style = AppTextStyle.Caption)
            }
            if (hasUpdate) Box(Modifier.size(8.dp).background(AppColors.secondary.spark, CircleShape))
        }
    }
}

@Composable
fun AppStatCard(
    label: String,
    value: String,
    detail: String,
    progress: Float,
    modifier: Modifier = Modifier,
    change: String? = null,
    onClick: (() -> Unit)? = null,
    sparkline: List<Float> = emptyList(),
) {
    AppCard(modifier, onClick = onClick) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            AppText(label, Modifier.weight(1f), style = AppTextStyle.Meta)
            if (change != null) AppBadge(change, variant = AppBadgeVariant.Success)
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            AppText(value, style = AppTextStyle.Headline)
            AppText(detail, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
        }
        Spacer(Modifier.height(12.dp))
        if (sparkline.isEmpty()) AppLinearProgress(progress, Modifier.fillMaxWidth()) else {
            Row(Modifier.fillMaxWidth().height(36.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
                sparkline.forEachIndexed { index, bar ->
                    Box(Modifier.weight(1f).fillMaxHeight(bar.coerceIn(0f, 1f)).background(
                        if (index == sparkline.lastIndex) AppColors.secondary.spark else AppColors.primary.graphite,
                        androidx.compose.foundation.shape.RoundedCornerShape(3.dp)))
                }
            }
        }
    }
}

@Composable
fun AppSuggestionCard(
    title: String,
    message: String,
    onReview: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    reviewLabel: String = "Review ↗",
    dismissLabel: String = "Dismiss",
    mascot: @Composable (() -> Unit)? = { AppMascot(AppMascotMood.Thinking, size = 48.dp) },
) {
    AppCard(modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            mascot?.invoke()
            Column(Modifier.weight(1f)) {
                AppText(title, style = AppTextStyle.Label)
                AppText(message, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    androidx.compose.material3.TextButton(onClick = onReview) { AppText(reviewLabel, style = AppTextStyle.Label, color = AppColors.primary.onyx) }
                    androidx.compose.material3.TextButton(onClick = onDismiss) { AppText(dismissLabel, style = AppTextStyle.Label, color = AppColors.neutral.inkSecondary) }
                }
            }
        }
    }
}

@Composable
fun AppMediaCard(
    title: String,
    subtitle: String,
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    AppCard(modifier, onClick = onClick, contentPadding = PaddingValues(0.dp)) {
        image()
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)) {
            AppText(title, style = AppTextStyle.TitleSmall)
            AppText(subtitle, style = AppTextStyle.Caption)
        }
    }
}

@Composable
fun AppEmptyCard(
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    mascot: @Composable (() -> Unit)? = { AppMascot(AppMascotMood.Resting, size = 100.dp) },
) {
    AppCard(modifier, contentPadding = PaddingValues(24.dp)) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            mascot?.invoke()
            Spacer(Modifier.height(12.dp))
            AppText(title, style = AppTextStyle.TitleSmall)
            AppText(message, style = AppTextStyle.BodySmall, color = AppColors.neutral.inkSecondary)
            Spacer(Modifier.height(16.dp))
            AppButton(actionLabel, onAction, variant = AppButtonVariant.Tonal)
        }
    }
}

@Composable
fun AppQuestionCard(
    context: String,
    question: String,
    answers: List<String>,
    selectedAnswer: Int?,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier) {
        AppText("✳ $context", style = AppTextStyle.Caption)
        Spacer(Modifier.height(12.dp))
        AppText(question, style = AppTextStyle.Title)
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            answers.forEachIndexed { index, answer ->
                id.codemockup.ramu.designsystem.components.inputs.AppChip(
                    answer, { onSelectAnswer(index) }, selected = selectedAnswer == index,
                    variant = AppChipVariant.Answer,
                )
            }
        }
    }
}
