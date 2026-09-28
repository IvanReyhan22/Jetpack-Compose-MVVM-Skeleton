package id.codemockup.template.core.designsystem.components.lists

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppRadius

/** Parent owns reveal state. Swipe right calls complete; swipe left reveals actions. */
@Composable
fun AppSwipeRow(
    revealed: Boolean,
    onRevealChange: (Boolean) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val threshold = with(LocalDensity.current) { 64.dp.toPx() }
    val actionWidth = with(LocalDensity.current) { 128.dp.roundToPx() }
    val offset by animateIntOffsetAsState(if (revealed) IntOffset(-actionWidth, 0) else IntOffset.Zero, label = "swipe row")
    Box(modifier.clip(RoundedCornerShape(AppRadius.card)).heightIn(min = 64.dp)) {
        Row(Modifier.align(Alignment.CenterEnd).width(128.dp).fillMaxHeight(), content = actions)
        Box(
            Modifier.fillMaxWidth().offset { offset }.background(AppColors.neutral.surface)
                .pointerInput(revealed) {
                    var drag = 0f
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, amount -> drag += amount; change.consume() },
                        onDragEnd = {
                            when {
                                drag > threshold -> if (revealed) onRevealChange(false) else onComplete()
                                drag < -threshold -> onRevealChange(true)
                            }
                            drag = 0f
                        },
                    )
                },
        ) { content() }
    }
}
