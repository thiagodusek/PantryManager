package com.pantrymanager.di

import android.content.Context
import androidx.room.Room
import com.pantrymanager.data.datasource.CategoryDao
import com.pantrymanager.data.datasource.PantryItemDao
import com.pantrymanager.data.datasource.ProductDao
import com.pantrymanager.data.datasource.UnitDao
import com.pantrymanager.data.datasource.PantryManagerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PantryManagerDatabase {
        return Room.databaseBuilder(
            context,
            PantryManagerDatabase::class.java,
            PantryManagerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePantryItemDao(database: PantryManagerDatabase): PantryItemDao {
        return database.pantryItemDao()
    }

    @Provides
    fun provideCategoryDao(database: PantryManagerDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideProductDao(database: PantryManagerDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideUnitDao(database: PantryManagerDatabase): UnitDao {
        return database.unitDao()
    }
}
