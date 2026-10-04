package id.codemockup.ramu.designsystem.components.feedback

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import id.codemockup.ramu.designsystem.theme.AppControlTokens

/**
 * Plays a Lottie animation bundled in `res/raw`.
 *
 * Pass `size = Dp.Unspecified` to size it through [modifier]. Decorative by default; wrap in your own semantics when the animation conveys state.
 */
@Composable
fun AppLottie(
    @RawRes rawRes: Int,
    modifier: Modifier = Modifier,
    size: Dp = AppControlTokens.field,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    speed: Float = 1f,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress = animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        isPlaying = isPlaying,
        speed = speed,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress.value },
        modifier = if (size.isSpecified) modifier.size(size) else modifier,
    )
}
