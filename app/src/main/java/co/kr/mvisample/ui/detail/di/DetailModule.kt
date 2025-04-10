package co.kr.mvisample.ui.detail.di

import co.kr.mvisample.mvi.ActionProcessor
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailEvent
import co.kr.mvisample.ui.detail.model.DetailUiState
import co.kr.mvisample.ui.detail.actionprocessor.LogoutActionProcessor
import co.kr.mvisample.ui.detail.actionprocessor.SignInActionProcessor
import co.kr.mvisample.ui.detail.actionprocessor.SignUpActionProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SignInActionAnnotation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SignUpActionAnnotation

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class LogoutActionAnnotation

@Module
@InstallIn(ActivityRetainedComponent::class)
class DetailModule {
    @Provides
    @SignInActionAnnotation
    fun provideSignInActionProcessor(): ActionProcessor<DetailAction, DetailUiState, DetailEvent> = SignInActionProcessor()

    @Provides
    @SignUpActionAnnotation
    fun provideSignUpActionProcessor(): ActionProcessor<DetailAction, DetailUiState, DetailEvent> = SignUpActionProcessor()

    @Provides
    @LogoutActionAnnotation
    fun provideLogoutActionProcessor(): ActionProcessor<DetailAction, DetailUiState, DetailEvent> = LogoutActionProcessor()
}