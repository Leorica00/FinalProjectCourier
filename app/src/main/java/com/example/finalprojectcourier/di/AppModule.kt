package com.example.finalprojectcourier.di

import android.content.Context
import com.example.final_project.data.remote.common.EmailSignInResponseHandler
import com.example.final_project.data.remote.common.ResponseHandler
import com.example.final_project.data.remote.service.ChatBotApiService
import com.example.final_project.di.DispatchersModule
import com.example.finalprojectcourier.data.remote.service.DirectionsApiService
import com.example.final_project.presentation.util.EncryptionHelper
import com.example.finalprojectcourier.BuildConfig
import com.example.finalprojectcourier.data.local.datasource.ChatBotAuthTokenDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MockyRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleMapRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FirebaseRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChatBotRetrofit

    @Provides
    @Singleton
    fun provideEmailSignInResponseHandler(@DispatchersModule.IoDispatcher ioDispatcher: CoroutineDispatcher): EmailSignInResponseHandler {
        return EmailSignInResponseHandler(ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideHandleResponse(): ResponseHandler = ResponseHandler()

    @Provides
    @Singleton
    fun provideEncryptionHelper(): EncryptionHelper = EncryptionHelper()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideChatBotAuthTokenDataSource(@ApplicationContext context: Context): ChatBotAuthTokenDataSource {
        return ChatBotAuthTokenDataSource(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            client.addInterceptor(httpLoggingInterceptor)
        }
        return client.build()
    }

    @Provides
    @Singleton
    @MockyRetrofit
    fun provideMockyRetrofitClient(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MOCKY_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @GoogleMapRetrofit
    fun provideGoogleMapRetrofitClient(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @FirebaseRetrofit
    fun provideFirebaseRetrofitClient(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FIREBASE_API_SERVICE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @ChatBotRetrofit
    fun provideChatBotRetrofitClient(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CHATBOT_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideChatBotApiService(@ChatBotRetrofit retrofit: Retrofit): ChatBotApiService {
        return retrofit.create(ChatBotApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDirectionsApiService(@GoogleMapRetrofit retrofit: Retrofit): DirectionsApiService {
        return retrofit.create(DirectionsApiService::class.java)
    }
}