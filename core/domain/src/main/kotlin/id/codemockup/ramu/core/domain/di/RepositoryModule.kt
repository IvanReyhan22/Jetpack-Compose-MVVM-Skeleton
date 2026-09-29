package id.codemockup.ramu.core.domain.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.domain.repository.auth.AuthDataSource
import id.codemockup.ramu.core.domain.repository.auth.AuthRepository
import id.codemockup.ramu.core.network.services.AuthServices
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(services: AuthServices): AuthRepository = AuthDataSource(services)
}
