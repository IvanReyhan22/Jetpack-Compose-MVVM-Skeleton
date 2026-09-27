package id.codemockup.template.core.designsystem.theme

import androidx.compose.ui.graphics.Color

object AppColors {
    object primary {
        val onyx = Color(0xFF222526)
        val graphite = Color(0xFF353A3E)
    }

    object neutral {
        val canvas = Color(0xFFF6F6F5)
        val surface = Color(0xFFFFFFFF)
        val surfaceSubtle = Color(0xFFEEEEED)
        val inkSecondary = Color(0xFF62686B)
        val platinum = Color(0xFFE0E0E0)
        val ash = Color(0xFFBFBFBF)
        val darkField = Color(0xFF2B2F32)
        val darkTrack = Color(0xFF4B5156)
        val darkMuted = Color(0xFFAEB3B5)
        val darkSoft = Color(0xFFD6D9DA)
        val glowDot = Color(0xFFD3D3D0)
        val fieldDot = Color(0xFFCFCFCC)
        val fieldLine = Color(0xFFE0E0DD)
        val scrim = Color(0xFF1A1A1A).copy(alpha = 0.45f)
    }

    object secondary {
        val spark = Color(0xFFD7F36A)
        val sparkTint = Color(0xFFEEF8C8)
        val onSparkTint = Color(0xFF4E5A1E)
    }

    object warning {
        val solid = Color(0xFF8B5A2B)
        val tint = Color(0xFFF5EDE4)
    }

    object negative {
        val solid = Color(0xFFA13E36)
        val tint = Color(0xFFF6E7E5)
    }

    object success {
        val solid = Color(0xFF3F6955)
        val tint = Color(0xFFE3EDE7)
    }
}
