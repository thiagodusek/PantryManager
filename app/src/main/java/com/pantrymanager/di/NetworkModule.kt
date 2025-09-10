package com.pantrymanager.di

import com.pantrymanager.data.service.ProductSearchService
import com.pantrymanager.data.service.ProductSearchServiceImpl
import com.pantrymanager.data.service.SefazService
import com.pantrymanager.data.service.ViaCepService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {
    
    @Binds
    @Singleton
    abstract fun bindProductSearchService(
        productSearchServiceImpl: ProductSearchServiceImpl
    ): ProductSearchService
}
