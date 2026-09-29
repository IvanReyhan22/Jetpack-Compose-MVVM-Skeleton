package id.codemockup.ramu.designsystem.components.previews

import id.codemockup.ramu.designsystem.common.enums.AppButtonSize

import id.codemockup.ramu.designsystem.components.buttons.*
import id.codemockup.ramu.designsystem.components.inputs.*
import id.codemockup.ramu.designsystem.components.text.AppText

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.designsystem.theme.*

@Preview(showBackground = true, widthDp = 420, heightDp = 1200)
@Preview(showBackground = true, widthDp = 420, heightDp = 1800, fontScale = 1.5f)
@Composable
private fun TextFieldsPreview() {
    RamuTheme {
        var value by remember { mutableStateOf("") }
        Column(Modifier.verticalScroll(rememberScrollState()).padding(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md.md16)) {
            AppTextField(value, { value = it }, label = "Name", placeholder = "Give this idea a name", helperText = "You can rename it later.")
            AppTextField("Space name", {}, label = "Error", error = "Enter a name before saving.")
            AppTextField("dana@studio.id", {}, label = "Validated", success = true)
            AppTextField("Created by Hermes", {}, label = "Disabled", enabled = false)
            AppTextField("Selectable text", {}, label = "Read only", readOnly = true)
            AppTextField("", {}, placeholder = "Search ideas, spaces, tasks", contentDescription = "Search",
                shape = AppRadius.pill, filled = true, leadingIcon = { AppText("⌕") })
            AppTextField("45", {}, label = "Duration", suffix = "min")
            AppTextField("Missing a day should not reset everything.", {}, label = "Notes", singleLine = false, characterLimit = 280)
            AppButton("Continue with a longer label", {}, size = AppButtonSize.Large)
        }
    }
}
