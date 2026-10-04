package id.codemockup.ramu.designsystem.components.feedback

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppMotion
import id.codemockup.ramu.designsystem.theme.AppSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private const val MAX_DRAG_MULTIPLIER = 1.5f

@Composable
fun AppPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val verticalPaddingPx = with(density) { AppSpacing.md.md24.toPx() }
    val scope = rememberCoroutineScope()

    val offset = remember { Animatable(0f) }
    // Natural panel content height + 2 * md24; 0f until the panel content is measured.
    val thresholdPx = remember { mutableFloatStateOf(0f) }
    val currentOnRefresh = rememberUpdatedState(onRefresh)
    val currentIsRefreshing = rememberUpdatedState(isRefreshing)

    val offsetFraction = remember {
        derivedStateOf {
            val threshold = thresholdPx.floatValue
            if (threshold > 0f) (offset.value / threshold).coerceIn(0f, MAX_DRAG_MULTIPLIER) else 0f
        }
    }
    val releaseReady = remember { derivedStateOf { offsetFraction.value >= 1f } }

    val pullLabel = when {
        isRefreshing -> "Refreshing…"
        releaseReady.value -> "Release To Refresh"
        else -> "Pull to refresh"
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            private var isReleasing = false

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (currentIsRefreshing.value || isReleasing) return Offset.Zero

                if (offset.value > 0 && available.y < 0) {
                    val consume = (available.y * 0.5f).coerceAtLeast(-offset.value)
                    scope.launch {
                        offset.snapTo((offset.value + consume).coerceAtLeast(0f))
                    }
                    return Offset(0f, consume)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (currentIsRefreshing.value || isReleasing) return Offset.Zero

                val threshold = thresholdPx.floatValue
                if (threshold > 0f && source == NestedScrollSource.UserInput && available.y > 0) {
                    val consume = (available.y * 0.5f)
                    val newOffset = (offset.value + consume).coerceAtMost(threshold * MAX_DRAG_MULTIPLIER)
                    scope.launch {
                        offset.snapTo(newOffset)
                    }
                    return Offset(0f, consume)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (currentIsRefreshing.value || isReleasing) return Velocity.Zero

                return if (offset.value > 0) {
                    isReleasing = true
                    try {
                        release()
                    } finally {
                        isReleasing = false
                    }
                    available
                } else {
                    Velocity.Zero
                }
            }

            private suspend fun release() {
                val threshold = thresholdPx.floatValue
                if (threshold > 0f && offset.value >= threshold) {
                    offset.animateTo(threshold, AppMotion.tween())
                    currentOnRefresh.value()
                    delay(500.milliseconds)
                    snapshotFlow { currentIsRefreshing.value }.first { !it }
                    offset.animateTo(0f, AppMotion.tween())
                } else {
                    offset.animateTo(0f, AppMotion.tween())
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnection),
    ) {
        AppPullToRefreshPanel(
            progress = { offsetFraction.value.coerceAtMost(1f) },
            label = pullLabel,
            isRefreshing = isRefreshing,
            onContentHeightChanged = { thresholdPx.floatValue = it + 2 * verticalPaddingPx },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .layout { measurable, constraints ->
                    val height = constraints.constrainHeight(offset.value.roundToInt().coerceAtLeast(0))
                    val placeable = measurable.measure(constraints.copy(minHeight = height, maxHeight = height))
                    layout(placeable.width, height) { placeable.place(0, 0) }
                },
        )

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = offset.value
                }
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}

/**
 * Pull indicator. Height is dictated by the caller (the drag gap); content keeps its natural
 * size, is centered, and is clipped rather than squashed when the gap is small.
 */
@Composable
private fun AppPullToRefreshPanel(
    progress: () -> Float,
    label: String,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    onContentHeightChanged: (Int) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true)
                .onSizeChanged { onContentHeightChanged(it.height) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm8),
        ) {
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.height(AppSpacing.lg.lg48),
                    color = AppColors.primary.onyx,
                    trackColor = AppColors.neutral.platinum,
                    strokeWidth = AppSpacing.sm.sm4,
                    gapSize = AppSpacing.sm.sm4
                )
            } else {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.height(AppSpacing.lg.lg48),
                    trackColor = AppColors.neutral.platinum,
                    color = AppColors.primary.onyx,
                    strokeWidth = AppSpacing.sm.sm4,
                    gapSize = AppSpacing.sm.sm4
                )
            }
            AppText(
                text = label,
                style = AppTextStyle.Caption,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPullToRefreshPanelIdlePreview() {
    AppPullToRefreshPanel(
        progress = { 0f },
        label = "Pull to refresh",
        isRefreshing = false,
        modifier = Modifier.height(120.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPullToRefreshPanelPullingPreview() {
    AppPullToRefreshPanel(
        progress = { 0.5f },
        label = "Pull to refresh",
        isRefreshing = false,
        modifier = Modifier.height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPullToRefreshPanelReleaseReadyPreview() {
    AppPullToRefreshPanel(
        progress = { 1f },
        label = "Release To Refresh",
        isRefreshing = false,
        modifier = Modifier.height(120.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPullToRefreshPanelRefreshingPreview() {
    AppPullToRefreshPanel(
        progress = { 1f },
        label = "Refreshing…",
        isRefreshing = true,
        modifier = Modifier.height(120.dp),
    )
}
