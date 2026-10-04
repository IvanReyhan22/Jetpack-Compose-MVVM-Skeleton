package id.codemockup.ramu.feature.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.common.enums.AppMascotMood
import id.codemockup.ramu.designsystem.common.enums.AppTextStyle
import id.codemockup.ramu.designsystem.components.feedback.AppLoadingIndicator
import id.codemockup.ramu.designsystem.components.icons.AppMascot
import id.codemockup.ramu.designsystem.components.text.AppText
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.RamuTheme

@Composable
fun ChatEmptyState(
    modifier: Modifier = Modifier,
    date: String? = null,
    isLoading: Boolean,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(AppSpacing.md.md24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AppMascot(AppMascotMood.Thinking, size = AppSpacing.lg.lg64)
        Spacer(Modifier.height(AppSpacing.md.md16))
        AppText("Ask, plan, or think out loud.", style = AppTextStyle.Title)
        Spacer(Modifier.height(AppSpacing.sm.sm8))
        AppText(
            "Hermes can see your spaces, schedule and ideas.",
            color = AppColors.neutral.inkSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.75f),
        )
        when {
            isLoading -> {
                AppLoadingIndicator(
                    label = "Connecting…",
                    modifier = Modifier.padding(AppSpacing.md.md16),
                )
            }
            !date.isNullOrBlank() -> {
                Spacer(Modifier.height(AppSpacing.md.md24))
                AppText(
                    date,
                    color = AppColors.neutral.inkSecondary,
                    style = AppTextStyle.Label,
                    textFontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.75f),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun ChatEmptyStatePreview() {
    RamuTheme { ChatEmptyState(isLoading = false) }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun ChatEmptyStateDataExistPreview() {
    RamuTheme { ChatEmptyState(isLoading = false, date = "TODAY · 9:32 PM") }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun ChatEmptyStateLoadingPreview() {
    RamuTheme { ChatEmptyState(isLoading = true) }
}
