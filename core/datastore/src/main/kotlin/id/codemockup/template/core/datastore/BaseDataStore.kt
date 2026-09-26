package id.codemockup.template.core.datastore


import id.codemockup.template.core.model.session.UserSession
import kotlinx.coroutines.flow.Flow

interface BaseDataStore {
    val session: Flow<UserSession?>
    suspend fun saveSession(session: UserSession)
    suspend fun clearSession()
    suspend fun clearSessionIfTokenMatches(expectedToken: String): Boolean
}
