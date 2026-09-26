package id.codemockup.template.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import id.codemockup.template.core.model.session.User
import id.codemockup.template.core.model.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class PreferencesDataStoreTest {
    @get:Rule val temporaryFolder = TemporaryFolder()

    @Test fun savesRestoresAndClearsSessionOnDisk() = runTest {
        val file = temporaryFolder.root.resolve("session.preferences_pb")
        val dispatcher = StandardTestDispatcher(testScheduler)
        val firstJob = SupervisorJob()
        val firstStore = PreferencesDataStore(PreferenceDataStoreFactory.create(
            scope = CoroutineScope(firstJob + dispatcher), produceFile = { file },
        ))
        val session = UserSession("token", User("id", "demo@example.com"))
        try {
            assertNull(firstStore.session.first())
            firstStore.saveSession(session)
            assertEquals(session, firstStore.session.first())
        } finally { firstJob.cancelAndJoin() }

        val secondJob = SupervisorJob()
        val restored = PreferencesDataStore(PreferenceDataStoreFactory.create(
            scope = CoroutineScope(secondJob + dispatcher), produceFile = { file },
        ))
        try {
            assertEquals(session, restored.session.first())
            restored.clearSession()
            assertNull(restored.session.first())
        } finally { secondJob.cancelAndJoin() }
    }
}
