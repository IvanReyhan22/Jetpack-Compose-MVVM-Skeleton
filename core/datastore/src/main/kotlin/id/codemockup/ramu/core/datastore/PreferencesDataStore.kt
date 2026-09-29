package id.codemockup.ramu.core.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import id.codemockup.ramu.core.model.session.User
import id.codemockup.ramu.core.model.session.UserSession
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(
    private val preferences: DataStore<Preferences>,
) : BaseDataStore {
    override val session = preferences.data.map { values ->
        val token = values[TOKEN]
        val id = values[USER_ID]
        val email = values[EMAIL]
        if (token.isNullOrBlank() || id.isNullOrBlank() || email.isNullOrBlank()) null
        else UserSession(token, User(id, email))
    }

    override suspend fun saveSession(session: UserSession) {
        preferences.edit {
            it[TOKEN] = session.token
            it[USER_ID] = session.user.id
            it[EMAIL] = session.user.email
        }
    }

    override suspend fun clearSession() {
        preferences.edit {
            it.remove(TOKEN)
            it.remove(USER_ID)
            it.remove(EMAIL)
        }
    }

    override suspend fun clearSessionIfTokenMatches(expectedToken: String): Boolean {
        var cleared = false
        preferences.edit {
            if (it[TOKEN] == expectedToken) {
                it.remove(TOKEN)
                it.remove(USER_ID)
                it.remove(EMAIL)
                cleared = true
            }
        }
        return cleared
    }

    private companion object {
        val TOKEN = stringPreferencesKey("session_token")
        val USER_ID = stringPreferencesKey("session_user_id")
        val EMAIL = stringPreferencesKey("session_email")
    }
}
