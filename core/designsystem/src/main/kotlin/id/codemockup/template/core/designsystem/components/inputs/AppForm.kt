package id.codemockup.template.core.designsystem.components.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.codemockup.template.core.designsystem.common.enums.AppTextStyle
import id.codemockup.template.core.designsystem.common.enums.AppAlertVariant
import id.codemockup.template.core.designsystem.components.feedback.AppInlineAlert
import id.codemockup.template.core.designsystem.components.navigation.AppSegmentProgress
import id.codemockup.template.core.designsystem.components.text.AppText
import id.codemockup.template.core.designsystem.theme.AppColors
import id.codemockup.template.core.designsystem.theme.AppRadius

@Composable
fun AppFormSection(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtle: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(if (subtle) AppRadius.card else AppRadius.feature),
        color = if (subtle) AppColors.neutral.canvas else AppColors.neutral.surface,
    ) {
        Column(Modifier.padding(if (subtle) 16.dp else 24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column {
                if (eyebrow != null) AppText(eyebrow, style = AppTextStyle.Meta)
                AppText(title, style = AppTextStyle.SectionTitle)
            }
            content()
        }
    }
}

@Composable
fun AppFormStepHeader(
    step: Int,
    totalSteps: Int,
    title: String,
    modifier: Modifier = Modifier,
) {
    require(totalSteps > 0)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AppSegmentProgress(totalSteps, step, Modifier.fillMaxWidth())
        AppText("Step $step of $totalSteps", style = AppTextStyle.Meta)
        AppText(title, style = AppTextStyle.Title)
    }
}

@Composable
fun AppFormErrorSummary(
    fields: List<String>,
    modifier: Modifier = Modifier,
    title: String = "${fields.size} fields need attention",
) {
    if (fields.isNotEmpty()) {
        AppInlineAlert(title, fields.joinToString(" · "), modifier = modifier, variant = AppAlertVariant.Error)
    }
}
