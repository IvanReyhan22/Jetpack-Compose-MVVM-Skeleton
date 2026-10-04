package id.codemockup.ramu.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.network.BuildConfig
import id.codemockup.ramu.core.network.HermesResponseConverterFactory
import id.codemockup.ramu.core.network.services.HermesServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

object HermesClient {
    fun create(baseUrl: String, key: String): HermesServices {
        // A valid placeholder allows the UI to report configuration failures without crashing DI.
        val validUrl = runCatching { Retrofit.Builder().baseUrl(baseUrl).build() }.isSuccess
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .followRedirects(false)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                if (!validUrl || key.isBlank()) throw IOException("Configure HERMES_BASE_URL and HERMES_API_KEY in app.properties, then rebuild a debug app.")
                chain.proceed(chain.request().newBuilder().header("Authorization", "Bearer $key").build())
            }.build()
        return Retrofit.Builder().baseUrl(if (validUrl) baseUrl else "https://invalid.example/")
            .client(client)
            .addConverterFactory(HermesResponseConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(HermesServices::class.java)
    }
}

@Module @InstallIn(SingletonComponent::class)
object HermesModule {
    @Provides @Named("HermesBaseUrl")
    fun provideHermesBaseUrl(): String = BuildConfig.HERMES_BASE_URL

    @Provides @Singleton
    fun provideHermesServices(): HermesServices = HermesClient.create(BuildConfig.HERMES_BASE_URL, BuildConfig.HERMES_API_KEY)
}
