package id.codemockup.ramu.designsystem.components.icons

import id.codemockup.ramu.designsystem.common.enums.AppIconName
import org.junit.Assert.assertEquals
import org.junit.Test

class AppIconTest {
    @Test
    fun everyReferenceIconBuildsFromItsPath() {
        AppIconName.entries.forEach { name ->
            val vector = iconVector(name)
            assertEquals(name.toString(), 24f, vector.viewportWidth)
            assertEquals(name.toString(), 24f, vector.viewportHeight)
        }
    }
}
