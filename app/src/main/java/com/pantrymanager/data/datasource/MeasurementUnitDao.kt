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
    suspend fun getAllMeasurementUnits(): List<MeasurementUnitEntity>
    
    @Query("SELECT * FROM measurement_units WHERE id = :id")
    suspend fun getMeasurementUnitById(id: Long): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun findByName(name: String): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE LOWER(abbreviation) = LOWER(:abbreviation) LIMIT 1")
    suspend fun findByAbbreviation(abbreviation: String): MeasurementUnitEntity?
    
    @Query("SELECT * FROM measurement_units WHERE name LIKE :query OR abbreviation LIKE :query ORDER BY name ASC")
    suspend fun searchMeasurementUnits(query: String): List<MeasurementUnitEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurementUnit(unit: MeasurementUnitEntity): Long
    
    @Update
    suspend fun updateMeasurementUnit(unit: MeasurementUnitEntity)
    
    @Delete
    suspend fun deleteMeasurementUnit(unit: MeasurementUnitEntity)
    
    @Query("DELETE FROM measurement_units WHERE id = :id")
    suspend fun deleteMeasurementUnitById(id: Long)
    
    @Query("DELETE FROM measurement_units WHERE id IN (:ids)")
    suspend fun deleteMeasurementUnits(ids: List<Long>)
}
