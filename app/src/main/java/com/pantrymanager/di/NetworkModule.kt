package com.pantrymanager.di

import com.pantrymanager.data.service.OpenAIApiService
import com.pantrymanager.data.service.OpenAIBrandSearchService

import com.pantrymanager.data.service.SefazService
import com.pantrymanager.data.service.ViaCepService
import com.pantrymanager.data.service.OpenAIApiService
import com.pantrymanager.data.service.OpenAIBrandSearchService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("viacep_okhttp")
    fun provideViaCepOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("viacep_retrofit")
    fun provideViaCepRetrofit(@Named("viacep_okhttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://viacep.com.br/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideViaCepService(@Named("viacep_retrofit") retrofit: Retrofit): ViaCepService {
        return retrofit.create(ViaCepService::class.java)
    }

    @Provides
    @Singleton
    @Named("sefaz_okhttp")
    fun provideSefazOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("sefaz_retrofit")
    fun provideSefazRetrofit(@Named("sefaz_okhttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.nfce.fazenda.gov.br/") // URL exemplo - substituir pela API real
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSefazService(@Named("sefaz_retrofit") retrofit: Retrofit): SefazService {
        return retrofit.create(SefazService::class.java)
    }

<<<<<<< HEAD
=======
    // OpenAI Configuration
>>>>>>> feature/melhorias-cadastro
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @Named("openai_okhttp")
    fun provideOpenAIOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("openai_retrofit")
    fun provideOpenAIRetrofit(
        @Named("openai_okhttp") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAIService(@Named("openai_retrofit") retrofit: Retrofit): OpenAIApiService {
        return retrofit.create(OpenAIApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenAIBrandSearchService(
        openAIApiService: OpenAIApiService,
        moshi: Moshi
    ): OpenAIBrandSearchService {
        return OpenAIBrandSearchService(openAIApiService, moshi)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {
    // Removido bindProductSearchService - já existe no ServiceBindsModule
}
