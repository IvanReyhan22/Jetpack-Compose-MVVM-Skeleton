package id.codemockup.ramu.designsystem.components.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.common.enums.AppIconName

@Composable
fun AppIcon(
    name: AppIconName,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = AppColors.primary.onyx,
) {
    val vector = remember(name) { iconVector(name) }
    Icon(vector, contentDescription, modifier.size(size), tint = color)
}

internal fun iconVector(name: AppIconName): ImageVector {
    val path = when (name) {
        AppIconName.Today -> "M7,3 H17 A4,4 0,0 1,21 7 V17 A4,4 0,0 1,17 21 H7 A4,4 0,0 1,3 17 V7 A4,4 0,0 1,7 3 Z M8,12 L11,15 L16,9"
        AppIconName.Idea -> "M9,18 H15 M10,21 H14 M8.5,14.5 A7,7 0,1 1,15.5 14.5 C14.6,15.2 14,16 14,17.5 H10 C10,16 9.4,15.2 8.5,14.5 Z"
        AppIconName.Schedule -> "M6,5 H18 A3,3 0,0 1,21 8 V18 A3,3 0,0 1,18 21 H6 A3,3 0,0 1,3 18 V8 A3,3 0,0 1,6 5 Z M7,3 V7 M17,3 V7 M3,10 H21 M8,14 H11 M14,14 H16 M8,17 H11"
        AppIconName.Spaces -> "M5,3 H9 A2,2 0,0 1,11 5 V9 A2,2 0,0 1,9 11 H5 A2,2 0,0 1,3 9 V5 A2,2 0,0 1,5 3 Z M15,3 H19 A2,2 0,0 1,21 5 V9 A2,2 0,0 1,19 11 H15 A2,2 0,0 1,13 9 V5 A2,2 0,0 1,15 3 Z M5,13 H9 A2,2 0,0 1,11 15 V19 A2,2 0,0 1,9 21 H5 A2,2 0,0 1,3 19 V15 A2,2 0,0 1,5 13 Z M15,13 H19 A2,2 0,0 1,21 15 V19 A2,2 0,0 1,19 21 H15 A2,2 0,0 1,13 19 V15 A2,2 0,0 1,15 13 Z"
        AppIconName.Mic -> "M12,3 A3,3 0,0 1,15 6 V11 A3,3 0,0 1,9 11 V6 A3,3 0,0 1,12 3 Z M5,11 A7,7 0,0 0,19 11 M12,18 V21"
        AppIconName.Search -> "M11,4 A7,7 0,1 1,11 18 A7,7 0,1 1,11 4 Z M20,20 L16,16"
        AppIconName.Plus -> "M12,5 V19 M5,12 H19"
        AppIconName.Check -> "M5,12 L10,17 L19,7"
        AppIconName.Close -> "M6,6 L18,18 M18,6 L6,18"
        AppIconName.Open -> "M7,17 L17,7 M8,7 H17 V16"
        AppIconName.Back -> "M15,18 L9,12 L15,6"
        AppIconName.Chevron -> "M6,9 L12,15 L18,9"
        AppIconName.More -> "M5,12 H5.01 M12,12 H12.01 M19,12 H19.01"
        AppIconName.Send -> "M12,19 V5 M6,11 L12,5 L18,11"
        AppIconName.Bell -> "M6,16 V11 A6,6 0,0 1,18 11 V16 L20,18 H4 Z M10,21 H14"
        AppIconName.Edit -> "M4,20 H8 L19,9 L15,5 L4,16 Z M13,7 L17,11"
        AppIconName.Clock -> "M12,3 A9,9 0,1 1,12 21 A9,9 0,1 1,12 3 Z M12,7 V12 L15,14"
        AppIconName.Filter -> "M4,6 H20 M7,12 H17 M10,18 H14"
        AppIconName.Trash -> "M4,7 H20 M9,7 V4 H15 V7 M6,7 L7,20 H17 L18,7"
        AppIconName.Link -> "M10,14 A4,4 0,0 0,16 14 L19,11 A4,4 0,0 0,13 5 L12,6 M14,10 A4,4 0,0 0,8 10 L5,13 A4,4 0,0 0,11 19 L12,18"
        AppIconName.Refresh -> "M23,4 V10 H17 M20.49,15 A9,9 0,1 1,18.37,5.64 L23,10"
    }
    return ImageVector.Builder(defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .apply {
            addPath(
                pathData = PathParser().parsePathString(path).toNodes(),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = if (name == AppIconName.More) 3f else 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            )
        }.build()
}
