package id.codemockup.ramu.core.datastore.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.datastore.BaseDataStore
import id.codemockup.ramu.core.datastore.PreferencesDataStore
import javax.inject.Singleton

private val Context.sessionPreferences by preferencesDataStore(name = "session")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides @Singleton
    fun provideChatSessionStore(store: id.codemockup.ramu.core.datastore.PreferencesChatSessionStore): id.codemockup.ramu.core.datastore.ChatSessionStore = store

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): DataStore<Preferences> =
        context.sessionPreferences

    @Provides
    @Singleton
    fun provideBaseDataStore(store: PreferencesDataStore): BaseDataStore = store
}
