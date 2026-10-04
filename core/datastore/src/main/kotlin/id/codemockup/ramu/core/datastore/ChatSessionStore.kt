package id.codemockup.ramu.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import id.codemockup.ramu.core.model.chat.StoredChatSession
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface ChatSessionStore {
    suspend fun read(): StoredChatSession?
    suspend fun save(session: StoredChatSession)
}

class PreferencesChatSessionStore @Inject constructor(private val preferences: DataStore<Preferences>) : ChatSessionStore {
    override suspend fun read(): StoredChatSession? {
        val values = preferences.data.first()
        val url = values[URL] ?: return null
        val id = values[ID] ?: return null
        return StoredChatSession(url, id)
    }
    override suspend fun save(session: StoredChatSession) {
        preferences.edit { it[URL] = session.baseUrl; it[ID] = session.sessionId }
    }
    private companion object {
        val URL = stringPreferencesKey("hermes_base_url")
        val ID = stringPreferencesKey("hermes_session_id")
    }
}
