package com.example.mssa.di.module

import brigitte.AuthorizationInterceptor
import brigitte.di.dagger.module.OkhttpModule
import com.example.mssa.model.remote.GithubSearchService
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2018. 12. 5. <p/>
 */

@Module(includes = [OkhttpModule::class])
class NetworkModule {
    companion object {
        val LOG_CLASS = NetworkModule::class.java

        const val GITHUB_API_URL     = "https://api.github.com/"
    }

    @Provides
    @Singleton
    fun provideGithubSearchService(retrofitBuilder: Retrofit.Builder): GithubSearchService =
        retrofitBuilder.baseUrl(GITHUB_API_URL).build()
            .create(GithubSearchService::class.java)

    @Provides
    @Singleton
    fun provideLogger(): Logger =
        LoggerFactory.getLogger(LOG_CLASS)

    @Provides
    @Singleton
    fun provideLogLevel() =
        HttpLoggingInterceptor.Level.BODY

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor? = null
}
