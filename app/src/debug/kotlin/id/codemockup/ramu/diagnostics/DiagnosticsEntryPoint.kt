package id.codemockup.ramu.diagnostics

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.common.NetworkErrorManager
import id.codemockup.ramu.core.common.SessionManager
import id.codemockup.ramu.core.datastore.BaseDataStore
import okhttp3.OkHttpClient

/** Debug-only access to real application wiring for instrumentation verification. */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface DiagnosticsEntryPoint {
    fun dataStore(): BaseDataStore
    fun networkErrors(): NetworkErrorManager
    fun sessions(): SessionManager
    fun httpClient(): OkHttpClient
}
