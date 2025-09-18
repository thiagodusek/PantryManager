package com.pantrymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantrymanager.data.dto.MeasurementUnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementUnitDao {
    
    @Query("SELECT * FROM measurement_units ORDER BY name ASC")
    suspend fun getAllUnits(): List<MeasurementUnitEntity>
    
    @Query("SELECT * FROM measurement_units WHERE id = :id")
    suspend fun getUnitById(id: Long): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findByName(name: String): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE LOWER(abbreviation) = LOWER(:abbreviation) LIMIT 1")
    suspend fun findByAbbreviation(abbreviation: String): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE name LIKE :query OR abbreviation LIKE :query ORDER BY name ASC")
    suspend fun searchUnits(query: String): List<MeasurementUnitEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: MeasurementUnitEntity): Long
    
    @Update
    suspend fun updateUnit(unit: MeasurementUnitEntity)
    
    @Delete
    suspend fun deleteUnit(unit: MeasurementUnitEntity)
    
    @Query("DELETE FROM measurement_units WHERE id = :id")
    suspend fun deleteUnitById(id: Long)
}
