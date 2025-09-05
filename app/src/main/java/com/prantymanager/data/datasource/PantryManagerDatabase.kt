package com.prantymanager.data.datasource

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.prantymanager.data.dto.CategoryEntity
import com.prantymanager.data.dto.PantryItemEntity
import com.prantymanager.data.dto.ProductEntity
import com.prantymanager.data.dto.UnitDbEntity

@Database(
    entities = [
        PantryItemEntity::class, 
        CategoryEntity::class,
        ProductEntity::class,
        UnitDbEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PantryManagerDatabase : RoomDatabase() {
    
    abstract fun pantryItemDao(): PantryItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun unitDao(): UnitDao
    
    companion object {
        const val DATABASE_NAME = "pranty_manager_db"
    }
}
