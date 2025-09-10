package com.pantrymanager.di

import com.pantrymanager.data.repository.BrandRepositoryImpl
import com.pantrymanager.data.repository.CategoryRepositoryImpl
import com.pantrymanager.data.repository.MeasurementUnitRepositoryImpl
import com.pantrymanager.data.repository.PantryItemRepositoryImpl
import com.pantrymanager.data.repository.ProductBatchRepositoryImpl
import com.pantrymanager.data.repository.ProductRepositoryImpl
import com.pantrymanager.data.repository.UserRepositoryImpl
import com.pantrymanager.domain.repository.BrandRepository
import com.pantrymanager.domain.repository.CategoryRepository
import com.pantrymanager.domain.repository.MeasurementUnitRepository
import com.pantrymanager.domain.repository.PantryItemRepository
import com.pantrymanager.domain.repository.ProductBatchRepository
import com.pantrymanager.domain.repository.ProductRepository
import com.pantrymanager.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPantryItemRepository(
        pantryItemRepositoryImpl: PantryItemRepositoryImpl
    ): PantryItemRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindBrandRepository(
        brandRepositoryImpl: BrandRepositoryImpl
    ): BrandRepository
    
    @Binds
    @Singleton
    abstract fun bindProductBatchRepository(
        productBatchRepositoryImpl: ProductBatchRepositoryImpl
    ): ProductBatchRepository
    
    @Binds
    @Singleton
    abstract fun bindMeasurementUnitRepository(
        measurementUnitRepositoryImpl: MeasurementUnitRepositoryImpl
    ): MeasurementUnitRepository

    // TODO: Implement these repository implementations
    // @Binds
    // @Singleton
    // abstract fun bindShoppingListRepository(
    //     shoppingListRepositoryImpl: ShoppingListRepositoryImpl
    // ): ShoppingListRepository

    // @Binds
    // @Singleton
    // abstract fun bindRecipeRepository(
    //     recipeRepositoryImpl: RecipeRepositoryImpl
    // ): RecipeRepository

    // @Binds
    // @Singleton
    // abstract fun bindStoreRepository(
    //     storeRepositoryImpl: StoreRepositoryImpl
    // ): StoreRepository

    // @Binds
    // @Singleton
    // abstract fun bindNFeRepository(
    //     nfeRepositoryImpl: NFeRepositoryImpl
    // ): NFeRepository
}
