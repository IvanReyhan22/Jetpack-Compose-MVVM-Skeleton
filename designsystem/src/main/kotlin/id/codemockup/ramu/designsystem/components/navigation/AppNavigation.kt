package id.codemockup.ramu.designsystem.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.common.enums.AppTabsVariant
import id.codemockup.ramu.designsystem.common.enums.AppConnectionState
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius

data class AppTab(val label: String, val count: Int? = null, val hasUpdate: Boolean = false)
@Composable
fun AppConnectionPill(
    state: AppConnectionState,
    modifier: Modifier = Modifier,
    connectedLabel: String = "Connected",
    syncingLabel: String = "Syncing",
    offlineLabel: String = "Offline · saved locally",
) {
    val offline = state == AppConnectionState.Offline
    Surface(
        modifier = modifier,
        shape = AppRadius.pill,
        color = if (offline) AppColors.warning.tint else AppColors.neutral.surface,
        border = if (offline) null else androidx.compose.foundation.BorderStroke(1.dp, AppColors.neutral.platinum),
    ) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(Modifier.size(6.dp).background(if (offline) AppColors.warning.solid else AppColors.secondary.spark, CircleShape))
            AppText(when (state) {
                AppConnectionState.Connected -> connectedLabel
                AppConnectionState.Syncing -> syncingLabel
                AppConnectionState.Offline -> offlineLabel
            }, style = AppTextStyle.Caption, color = if (offline) AppColors.warning.solid else AppColors.primary.onyx)
        }
    }
}

@Composable
fun AppTabs(
    tabs: List<AppTab>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    variant: AppTabsVariant = AppTabsVariant.Underline,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(if (variant == AppTabsVariant.Pills) 8.dp else 24.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = index == selectedIndex
            val background = if (variant == AppTabsVariant.Pills) {
                if (selected) AppColors.primary.onyx else AppColors.neutral.surfaceSubtle
            } else Color.Transparent
            Column(
                Modifier.clip(if (variant == AppTabsVariant.Pills) AppRadius.pill else RoundedCornerShape(0.dp))
                    .background(background)
                    .clickable(role = Role.Tab) { onSelect(index) }
                    .heightIn(min = 48.dp)
                    .padding(horizontal = if (variant == AppTabsVariant.Pills) 14.dp else 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AppText(tab.label, style = AppTextStyle.Label,
                        color = if (variant == AppTabsVariant.Pills && selected) Color.White else if (selected) AppColors.primary.onyx else AppColors.neutral.inkSecondary)
                    if (tab.count != null) AppText(tab.count.toString(), style = AppTextStyle.Caption,
                        color = if (variant == AppTabsVariant.Pills && selected) Color.White else AppColors.neutral.inkSecondary)
                    if (tab.hasUpdate) Box(Modifier.size(8.dp).background(AppColors.secondary.spark, CircleShape))
                }
                if (variant == AppTabsVariant.Underline) {
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.width(16.dp).height(2.dp).background(if (selected) AppColors.primary.onyx else Color.Transparent))
                }
            }
        }
    }
}

@Composable
fun AppLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    color: Color = AppColors.primary.graphite,
    trackColor: Color = AppColors.neutral.platinum,
) {
    Box(modifier.height(height).clip(AppRadius.pill).background(trackColor)) {
        Box(Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxHeight().background(color))
    }
}

@Composable
fun AppSegmentProgress(
    total: Int,
    completed: Int,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
) {
    require(total > 0)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(total) { index ->
            Box(Modifier.weight(1f).height(height).clip(AppRadius.pill)
                .background(if (index < completed) AppColors.primary.onyx else AppColors.neutral.platinum))
        }
    }
}

@Composable
fun AppPageDots(
    count: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onSelect: ((Int) -> Unit)? = null,
) {
    require(count > 0)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(count) { index ->
            Box(
                (if (onSelect != null) Modifier.clickable { onSelect(index) }.size(32.dp) else Modifier.size(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(8.dp).background(if (index == selectedIndex) AppColors.primary.onyx else AppColors.neutral.ash, CircleShape))
            }
        }
    }
}

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Surface(modifier = modifier, color = AppColors.neutral.canvas) {
        Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(44.dp), contentAlignment = Alignment.Center) { navigationIcon?.invoke() }
            AppText(title, modifier = Modifier.weight(1f), style = AppTextStyle.TitleSmall)
            Row(content = actions)
        }
    }
}

data class AppBottomDestination(
    val label: String,
    val icon: @Composable () -> Unit,
    val hasUpdate: Boolean = false,
)

@Composable
fun AppBottomBar(
    destinations: List<AppBottomDestination>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    captureIcon: @Composable () -> Unit,
    onCapture: () -> Unit,
    modifier: Modifier = Modifier,
    flat: Boolean = false,
) {
    require(destinations.size == 4)
    Box(modifier.fillMaxWidth().height(if (flat) 72.dp else 104.dp)) {
        Row(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(if (flat) 72.dp else 88.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(AppColors.primary.onyx).padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.forEachIndexed { index, destination ->
                if (index == 2) Spacer(Modifier.weight(1f))
                Column(
                    Modifier.weight(1f).fillMaxHeight().clickable(role = Role.Tab) { onSelect(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box {
                        destination.icon()
                        if (destination.hasUpdate) Box(Modifier.align(Alignment.TopEnd).offset(x = 6.dp, y = (-2).dp)
                            .size(8.dp).background(AppColors.secondary.spark, CircleShape))
                    }
                    AppText(destination.label, style = AppTextStyle.Caption,
                        color = if (index == selectedIndex) Color.White else AppColors.neutral.darkMuted)
                    if (!flat) Box(Modifier.padding(top = 3.dp).width(16.dp).height(3.dp).background(
                        if (index == selectedIndex) AppColors.secondary.spark else Color.Transparent, AppRadius.pill))
                }
            }
        }
        if (!flat) Box(Modifier.align(Alignment.TopCenter).size(80.dp).background(AppColors.neutral.canvas, CircleShape))
        Box(
            Modifier.align(if (flat) Alignment.Center else Alignment.TopCenter)
                .size(if (flat) 48.dp else 64.dp).background(AppColors.secondary.spark, if (flat) RoundedCornerShape(AppRadius.card) else CircleShape)
                .clickable(role = Role.Button, onClick = onCapture),
            contentAlignment = Alignment.Center,
        ) { captureIcon() }
    }
}

@Composable
fun AppRingProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    strokeWidth: Dp = 7.dp,
    color: Color = AppColors.primary.graphite,
    content: @Composable BoxScope.() -> Unit = {
        AppText("${(progress.coerceIn(0f, 1f) * 100).toInt()}%", style = AppTextStyle.Label)
    },
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            inset(stroke / 2f) {
                drawArc(AppColors.neutral.platinum, startAngle = -90f, sweepAngle = 360f, useCenter = false,
                    style = Stroke(stroke, cap = StrokeCap.Round))
                drawArc(color, startAngle = -90f, sweepAngle = 360f * progress.coerceIn(0f, 1f), useCenter = false,
                    style = Stroke(stroke, cap = StrokeCap.Round))
            }
        }
        content()
    }
}
