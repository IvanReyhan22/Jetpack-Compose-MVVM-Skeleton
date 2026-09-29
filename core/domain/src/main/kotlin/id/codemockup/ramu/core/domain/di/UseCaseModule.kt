package id.codemockup.ramu.core.domain.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.codemockup.ramu.core.domain.usecase.auth.AuthUseCase
import id.codemockup.ramu.core.domain.usecase.auth.LoginUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideAuthUseCase(loginUseCase: LoginUseCase) = AuthUseCase(loginUseCase)
}
