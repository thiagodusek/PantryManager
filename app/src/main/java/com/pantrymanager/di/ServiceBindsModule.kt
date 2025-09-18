package com.pantrymanager.di

import com.pantrymanager.data.service.OpenAICategorySearchService
import com.pantrymanager.data.service.OpenAICategorySearchServiceImpl
import com.pantrymanager.data.service.OpenAIMeasurementUnitSearchService
import com.pantrymanager.data.service.OpenAIMeasurementUnitSearchServiceImpl
import com.pantrymanager.data.service.ProductSearchService
import com.pantrymanager.data.service.ProductSearchServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo para binding de implementações reais (não-Retrofit) de serviços
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceBindsModule {
    
    @Binds
    @Singleton
    abstract fun bindProductSearchService(
        impl: ProductSearchServiceImpl
    ): ProductSearchService
    
    @Binds
    @Singleton
    abstract fun bindOpenAICategorySearchService(
        impl: OpenAICategorySearchServiceImpl
    ): OpenAICategorySearchService
    
    @Binds
    @Singleton
    abstract fun bindOpenAIMeasurementUnitSearchService(
        impl: OpenAIMeasurementUnitSearchServiceImpl
    ): OpenAIMeasurementUnitSearchService
}
