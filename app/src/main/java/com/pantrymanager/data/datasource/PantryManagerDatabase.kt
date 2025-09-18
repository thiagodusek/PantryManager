package com.pantrymanager.data.datasource

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.pantrymanager.data.dto.BrandEntity
import com.pantrymanager.data.dto.CategoryEntity
import com.pantrymanager.data.dto.MeasurementUnitEntity
import com.pantrymanager.data.dto.PantryItemEntity
import com.pantrymanager.data.dto.ProductBatchEntity
import com.pantrymanager.data.dto.ProductEntity

@Database(
    entities = [
        PantryItemEntity::class, 
        CategoryEntity::class,
        ProductEntity::class,
        MeasurementUnitEntity::class,
        BrandEntity::class,
        ProductBatchEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PantryManagerDatabase : RoomDatabase() {
    
    abstract fun pantryItemDao(): PantryItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun brandDao(): BrandDao
    abstract fun productBatchDao(): ProductBatchDao
    abstract fun measurementUnitDao(): MeasurementUnitDao
    
    companion object {
        const val DATABASE_NAME = "pantry_manager_db"
    }
}
