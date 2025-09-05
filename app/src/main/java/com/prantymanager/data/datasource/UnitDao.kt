package com.prantymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prantymanager.data.dto.UnitDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    
    @Query("SELECT * FROM units ORDER BY name ASC")
    fun getAllUnits(): Flow<List<UnitDbEntity>>
    
    @Query("SELECT * FROM units WHERE id = :id")
    fun getUnitById(id: Long): Flow<UnitDbEntity?>
    
    @Query("SELECT * FROM units WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchUnits(query: String): Flow<List<UnitDbEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: UnitDbEntity): Long
    
    @Update
    suspend fun updateUnit(unit: UnitDbEntity)
    
    @Delete
    suspend fun deleteUnit(unit: UnitDbEntity)
    
    @Query("DELETE FROM units WHERE id = :id")
    suspend fun deleteUnitById(id: Long)
}
