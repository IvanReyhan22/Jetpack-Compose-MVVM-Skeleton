package id.codemockup.template.feature.login

import id.codemockup.template.core.datastore.BaseDataStore
import id.codemockup.template.core.model.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException

internal class FakeDataStore : BaseDataStore {
    override val session = MutableStateFlow<UserSession?>(null)
    var failWrite = false
    override suspend fun saveSession(session: UserSession) {
        if (failWrite) throw IOException("Disk unavailable")
        this.session.value = session
    }
    override suspend fun clearSession() {
        if (failWrite) throw IOException("Disk unavailable")
        session.value = null
    }
}
