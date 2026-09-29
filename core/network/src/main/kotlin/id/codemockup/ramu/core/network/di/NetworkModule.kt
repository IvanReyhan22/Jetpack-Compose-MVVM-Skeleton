package id.codemockup.ramu.core.network.di


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import id.codemockup.ramu.core.network.DiagnosticsInterceptor
import id.codemockup.ramu.core.network.ResponseInterceptor
import id.codemockup.ramu.core.network.SentryBreadcrumbInterceptor
import java.util.concurrent.TimeUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.network.BuildConfig
import id.codemockup.ramu.core.network.demo.DemoAuthServices
import id.codemockup.ramu.core.network.services.AuthServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        responseInterceptor: ResponseInterceptor,
    ): OkHttpClient {
        val chucker = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context, showNotification = true, retentionPeriod = RetentionManager.Period.ONE_HOUR))
            .maxContentLength(250_000L)
            .redactHeaders("Authorization", "Cookie", "Set-Cookie")
            .build()
        return OkHttpClient.Builder()
            .addInterceptor(responseInterceptor)
            .addInterceptor(DiagnosticsInterceptor(chucker))
            .addInterceptor(SentryBreadcrumbInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Both flavors intentionally run offline until this binding is replaced.
    @Provides
    @Singleton
    fun provideAuthServices(demo: DemoAuthServices): AuthServices = demo
}
