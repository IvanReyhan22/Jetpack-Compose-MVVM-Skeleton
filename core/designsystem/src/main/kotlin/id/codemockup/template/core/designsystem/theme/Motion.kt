package id.codemockup.template.core.designsystem.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

object AppMotion {
    const val fast = 140
    const val base = 220
    const val sheet = 320
    const val compose = 480
    val standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    fun <T> tween(durationMillis: Int = base): TweenSpec<T> =
        tween(durationMillis = durationMillis, easing = standard)
}
