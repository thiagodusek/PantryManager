package com.pantrymanager.di

import com.pantrymanager.data.repository.BrandFirebaseRepository
import com.pantrymanager.data.repository.CategoryFirebaseRepository
import com.pantrymanager.data.repository.FiscalReceiptFirebaseRepository
import com.pantrymanager.data.repository.MeasurementUnitFirebaseRepository
import com.pantrymanager.data.repository.PantryItemRepositoryImpl
import com.pantrymanager.data.repository.ProductBatchRepositoryImpl
import com.pantrymanager.data.repository.ProductFirebaseRepository
import com.pantrymanager.data.repository.UserRepositoryImpl
import com.pantrymanager.domain.repository.BrandRepository
import com.pantrymanager.domain.repository.CategoryRepository
import com.pantrymanager.domain.repository.FiscalReceiptRepository
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

    // Firebase-based repositories
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productFirebaseRepository: ProductFirebaseRepository
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryFirebaseRepository: CategoryFirebaseRepository
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBrandRepository(
        brandFirebaseRepository: BrandFirebaseRepository
    ): BrandRepository
    
    @Binds
    @Singleton
    abstract fun bindMeasurementUnitRepository(
        measurementUnitFirebaseRepository: MeasurementUnitFirebaseRepository
    ): MeasurementUnitRepository
    
    @Binds
    @Singleton
    abstract fun bindFiscalReceiptRepository(
        fiscalReceiptFirebaseRepository: FiscalReceiptFirebaseRepository
    ): FiscalReceiptRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    // Room-based repositories (temporary for items that don't have Firebase implementations yet)
    @Binds
    @Singleton
    abstract fun bindPantryItemRepository(
        pantryItemRepositoryImpl: PantryItemRepositoryImpl
    ): PantryItemRepository
    
    @Binds
    @Singleton
    abstract fun bindProductBatchRepository(
        productBatchRepositoryImpl: ProductBatchRepositoryImpl
    ): ProductBatchRepository

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
