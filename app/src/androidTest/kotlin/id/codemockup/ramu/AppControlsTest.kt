package id.codemockup.ramu

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import id.codemockup.ramu.designsystem.components.buttons.*
import id.codemockup.ramu.designsystem.components.inputs.*
import id.codemockup.ramu.designsystem.theme.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppControlsTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun buttonsBlockClicksWhileLoadingOrDisabled() {
        var clicks = 0
        compose.setContent {
            RamuTheme {
                Column {
                    AppButton("Ready", { clicks++ })
                    AppButton("Busy", { clicks++ }, loading = true)
                    AppButton("Disabled", { clicks++ }, enabled = false)
                }
            }
        }
        compose.onNodeWithText("Ready").performClick()
        compose.onNodeWithText("Busy").assertIsNotEnabled().performClick()
        compose.onNodeWithText("Disabled").assertIsNotEnabled().performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test fun fieldsKeepInputAndExposeValidation() {
        compose.setContent {
            var value by remember { mutableStateOf("Draft") }
            RamuTheme {
                Column {
                    AppTextField(value, { value = it }, label = "Name", error = "Name already exists.")
                    AppTextField("Locked", {}, label = "Read only", readOnly = true)
                    AppTextField("Disabled value", {}, label = "Disabled", enabled = false)
                    AppTextField("", {}, contentDescription = "Search", filled = true, shape = AppRadius.pill)
                }
            }
        }
        compose.onNodeWithContentDescription("Name").performTextReplacement("New name")
        compose.onNodeWithContentDescription("Name").assertTextEquals("New name")
        compose.onNodeWithText("Name already exists.").assertIsDisplayed()
        compose.onNodeWithContentDescription("Read only").assertTextEquals("Locked")
            .assert(SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsActions.SetText))
        compose.onNodeWithContentDescription("Disabled").assertIsNotEnabled()
        compose.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test fun iconsAndCollapsedFabKeepAccessibleLabels() {
        var clicks = 0
        compose.setContent {
            RamuTheme {
                Column {
                    AppIconButton({ clicks++ }, "Notifications") { androidx.compose.material3.Text("!") }
                    AppFloatingActionButton({ clicks++ }, "Capture", enabled = false) { androidx.compose.material3.Text("+") }
                    AppExtendedFloatingActionButton("Add item", { clicks++ }, expanded = false) { androidx.compose.material3.Text("+") }
                }
            }
        }
        compose.onNodeWithContentDescription("Notifications").performClick()
        compose.onNodeWithContentDescription("Capture").assertIsNotEnabled().performClick()
        compose.onNodeWithContentDescription("Add item").performClick()
        compose.runOnIdle { assertEquals(2, clicks) }
    }
}
