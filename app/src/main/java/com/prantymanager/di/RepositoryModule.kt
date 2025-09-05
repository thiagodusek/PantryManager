package com.prantymanager.di

import com.prantymanager.data.repository.CategoryRepositoryImpl
import com.prantymanager.data.repository.PantryItemRepositoryImpl
import com.prantymanager.data.repository.ProductRepositoryImpl
import com.prantymanager.data.repository.UnitRepositoryImpl
import com.prantymanager.data.repository.UserRepositoryImpl
import com.prantymanager.domain.repository.CategoryRepository
import com.prantymanager.domain.repository.PantryItemRepository
import com.prantymanager.domain.repository.ProductRepository
import com.prantymanager.domain.repository.UnitRepository
import com.prantymanager.domain.repository.UserRepository
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
    abstract fun bindUnitRepository(
        unitRepositoryImpl: UnitRepositoryImpl
    ): UnitRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

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
