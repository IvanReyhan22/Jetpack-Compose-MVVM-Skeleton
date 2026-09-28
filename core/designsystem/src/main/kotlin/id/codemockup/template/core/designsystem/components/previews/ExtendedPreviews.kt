package id.codemockup.template.core.designsystem.components.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.common.enums.*
import id.codemockup.template.core.designsystem.components.cards.*
import id.codemockup.template.core.designsystem.components.feedback.*
import id.codemockup.template.core.designsystem.components.icons.*
import id.codemockup.template.core.designsystem.components.inputs.*
import id.codemockup.template.core.designsystem.components.lists.*
import id.codemockup.template.core.designsystem.components.navigation.*
import id.codemockup.template.core.designsystem.components.text.AppText
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.TemplateTheme

@Preview(showBackground = true, widthDp = 390, heightDp = 1000)
@Composable
private fun SelectionPreview() {
    TemplateTheme {
        var checked by remember { mutableStateOf(false) }
        var segment by remember { mutableIntStateOf(0) }
        var step by remember { mutableIntStateOf(3) }
        var slider by remember { mutableFloatStateOf(.4f) }
        var rating by remember { mutableIntStateOf(3) }
        Column(Modifier.background(AppColors.neutral.canvas).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AppCheckbox("Weekly totals", if (checked) AppCheckboxState.Checked else AppCheckboxState.Unchecked, { checked = !checked })
            AppCheckbox("Partial progress", AppCheckboxState.Indeterminate, {})
            AppRadioOption("Board view", segment == 0, { segment = 0 })
            AppSwitchRow("Show completed tasks", checked, { checked = it }, supportingText = "Keeps done cards on the board")
            AppSegmentedControl(listOf("Board", "List", "Calendar"), segment, { segment = it })
            AppStepper("Sets", step, { step = it }, range = 0..10)
            AppPickerField("Date", "Fri, Oct 2", {})
            AppEffortSlider("Effort", slider, { slider = it })
            AppRating(rating, { rating = it })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppChip("Filter", {}, selected = true)
                AppChip("✳ Suggestion", {}, variant = AppChipVariant.Assist)
            }
            AppOptionCard("Board", "Columns by status", true, {})
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 900)
@Composable
private fun CardsAndFeedbackPreview() {
    TemplateTheme {
        Column(Modifier.background(AppColors.neutral.canvas).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppCard {
                AppText("PROJECT SPACE", style = AppTextStyle.Meta)
                AppText("Gym tracker", style = AppTextStyle.Title)
                AppText("Board · 3 next actions", style = AppTextStyle.BodySmall)
            }
            AppCard(variant = AppCardVariant.Subtle) { AppText("Rest timer rules", style = AppTextStyle.TitleSmall) }
            AppCard(variant = AppCardVariant.Placeholder, onClick = {}) { AppText("Add a block", style = AppTextStyle.TitleSmall) }
            AppCard(variant = AppCardVariant.Selected) { AppText("Reading list", style = AppTextStyle.TitleSmall) }
            AppCard(variant = AppCardVariant.Graphite) { AppText("First up", style = AppTextStyle.Meta); AppText("Define the workout flow", style = AppTextStyle.SectionTitle) }
            AppCard(variant = AppCardVariant.SparkTint) { AppText("Hermes is asking", style = AppTextStyle.Meta); AppText("Streaks or weekly totals?", style = AppTextStyle.Title) }
            AppListRow("Reply to Dana", subtitle = "11:00 · Weekend market", leading = { AppCheckbox("", AppCheckboxState.Unchecked, {}) }, trailing = { AppBadge("Today", variant = AppBadgeVariant.Error) })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppBadge("Done", variant = AppBadgeVariant.Success)
                AppBadge("Due soon", variant = AppBadgeVariant.Warning)
                AppCounter(12)
                AppAvatar("DA")
            }
            AppInlineAlert("Needs attention", "One question is waiting.", variant = AppAlertVariant.Warning)
            AppSnackbarContent("Task moved to Done", actionLabel = "Undo", onAction = {})
            AppLoadingIndicator("Loading workspace…")
            AppSkeletonLine(Modifier.fillMaxWidth(.6f))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 640)
@Composable
private fun NavigationAndIconsPreview() {
    TemplateTheme {
        var selected by remember { mutableIntStateOf(0) }
        val destinations = listOf(AppIconName.Today, AppIconName.Idea, AppIconName.Schedule, AppIconName.Spaces)
            .map { icon -> AppBottomDestination(icon.name, { AppIcon(icon, null, color = AppColors.neutral.surface) }) }
        Column(Modifier.background(AppColors.neutral.canvas).padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            AppTopBar("Gym tracker", navigationIcon = { AppIcon(AppIconName.Back, "Back") })
            AppTabs(listOf(AppTab("Board"), AppTab("Notes"), AppTab("Activity", 4)), selected, { selected = it.coerceAtMost(2) })
            AppTabs(listOf(AppTab("All", 12), AppTab("Captured"), AppTab("Developing")), selected, { selected = it.coerceAtMost(2) }, variant = AppTabsVariant.Pills)
            AppLinearProgress(.25f, Modifier.fillMaxWidth())
            AppSegmentProgress(5, 3, Modifier.fillMaxWidth())
            AppRingProgress(.6f)
            AppPageDots(3, selected)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppMascot(AppMascotMood.Idle)
                AppMascot(AppMascotMood.Thinking)
                AppMascot(AppMascotMood.Happy)
                AppMascot(AppMascotMood.Resting)
            }
            AppBottomBar(destinations, selected.coerceAtMost(3), { selected = it }, { AppIcon(AppIconName.Plus, "Capture") }, {})
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1200)
@Composable
private fun ContentCardsPreview() {
    TemplateTheme {
        var checked by remember { mutableStateOf(false) }
        var selectedAnswer by remember { mutableStateOf<Int?>(null) }
        Column(Modifier.background(AppColors.neutral.canvas).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTaskCard("Send grant budget to Rafi", checked, { checked = it }, metadata = "17:00 · Grant application", badge = "Today")
            AppEventCard("FRI", "2", "Grant application due", "All day · Tracker · 60% ready")
            AppIdeaCard("Reading list that summarizes itself", "Concept is ready. It fits a list with notes.",
                status = "Concept ready", actionLabel = "Make it a space ↗", onAction = {})
            AppSpaceCard("Gym tracker", "Board · 3 next · edited 2h ago", hasUpdate = true,
                preview = { Box(Modifier.fillMaxWidth().height(96.dp).background(AppColors.neutral.surfaceSubtle)) })
            AppStatCard("Sessions this week", "3", " / 4 goal", .75f, change = "+1",
                sparkline = listOf(.4f, .7f, .2f, .9f, .6f, .1f, 1f))
            AppQuestionCard("Hermes is asking · Habit app", "Should progress count streaks or weekly totals?",
                listOf("Streaks", "Weekly totals"), selectedAnswer, { selectedAnswer = it })
            AppSuggestionCard("Hermes suggested a layout change", "Add a sessions log to record sets.", {}, {})
            AppMediaCard("Kemang · 2BR", "Rp 9.5 jt / mo · viewing Sat 10:00",
                image = { Box(Modifier.fillMaxWidth().height(120.dp).background(AppColors.neutral.surfaceSubtle)) })
            AppEmptyCard("All clear for now.", "New ideas and actions will appear here.", "Capture something", {})
        }
    }
}
