package id.codemockup.template.diagnostics

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.template.core.common.NetworkErrorManager
import id.codemockup.template.core.common.SessionManager
import id.codemockup.template.core.datastore.BaseDataStore
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
