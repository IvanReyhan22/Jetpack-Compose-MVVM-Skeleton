package id.codemockup.ramu.feature.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.common.enums.AppIconName
import id.codemockup.ramu.designsystem.components.buttons.AppIconButton
import id.codemockup.ramu.designsystem.components.icons.AppIcon
import id.codemockup.ramu.designsystem.components.inputs.AppComposer
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.RamuTheme

@Composable
fun ChatComposerBar(
    draft: String,
    canSend: Boolean,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppComposer(
        value = draft,
        onValueChange = onDraftChange,
        placeholder = "Message Hermes",
        modifier = modifier
            .fillMaxWidth()
            .padding(AppSpacing.sm.sm12)
            .testTag("chat_composer"),
        action = {
            AppIconButton(
                onClick = onSend,
                contentDescription = "Send",
                enabled = canSend,
            ) {
                AppIcon(AppIconName.Send, contentDescription = null)
            }
        },
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ChatComposerBarEmptyPreview() {
    RamuTheme { ChatComposerBar("", canSend = false, onDraftChange = {}, onSend = {}) }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ChatComposerBarFilledPreview() {
    RamuTheme { ChatComposerBar("Monday and Wednesday", canSend = true, onDraftChange = {}, onSend = {}) }
}
