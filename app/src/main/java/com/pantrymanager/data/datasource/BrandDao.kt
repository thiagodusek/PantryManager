package com.pantrymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantrymanager.data.dto.BrandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    
    @Query("SELECT * FROM brands ORDER BY name ASC")
    suspend fun getAllBrands(): List<BrandEntity>
    
    @Query("SELECT * FROM brands WHERE id = :id")
    suspend fun getBrandById(id: Long): BrandEntity?
    
    @Query("SELECT * FROM brands WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findByName(name: String): BrandEntity?
    
    @Query("SELECT * FROM brands WHERE name LIKE :query ORDER BY name ASC")
    suspend fun searchBrands(query: String): List<BrandEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity): Long
    
    @Update
    suspend fun updateBrand(brand: BrandEntity)
    
    @Delete
    suspend fun deleteBrand(brand: BrandEntity)
    
    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteBrandById(id: Long)
}
