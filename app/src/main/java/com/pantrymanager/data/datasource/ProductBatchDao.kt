package com.pantrymanager.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pantrymanager.data.dto.ProductBatchEntity

@Dao
interface ProductBatchDao {
    
    @Query("SELECT * FROM product_batches WHERE userId = :userId ORDER BY expiryDate ASC")
    suspend fun getAllBatches(userId: String): List<ProductBatchEntity>
    
    @Query("SELECT * FROM product_batches WHERE id = :id")
    suspend fun getBatchById(id: Long): ProductBatchEntity?
    
    @Query("SELECT * FROM product_batches WHERE productId = :productId AND userId = :userId ORDER BY expiryDate ASC")
    suspend fun getBatchesByProduct(productId: Long, userId: String): List<ProductBatchEntity>
    
    @Query("SELECT * FROM product_batches WHERE batchNumber = :batchNumber AND userId = :userId LIMIT 1")
    suspend fun findByBatchNumber(batchNumber: String, userId: String): ProductBatchEntity?
    
    @Query("SELECT * FROM product_batches WHERE expiryDate < :date AND isConsumed = 0 AND userId = :userId ORDER BY expiryDate ASC")
    suspend fun getExpiredBatches(userId: String, date: String): List<ProductBatchEntity>
    
    @Query("SELECT * FROM product_batches WHERE expiryDate BETWEEN :startDate AND :endDate AND isConsumed = 0 AND userId = :userId ORDER BY expiryDate ASC")
    suspend fun getBatchesExpiringWithin(userId: String, startDate: String, endDate: String): List<ProductBatchEntity>
    
    @Query("SELECT * FROM product_batches WHERE isConsumed = 0 AND userId = :userId ORDER BY quantity DESC")
    suspend fun getActiveBatchesByQuantity(userId: String): List<ProductBatchEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: ProductBatchEntity): Long
    
    @Update
    suspend fun updateBatch(batch: ProductBatchEntity)
    
    @Delete
    suspend fun deleteBatch(batch: ProductBatchEntity)
    
    @Query("DELETE FROM product_batches WHERE id = :id")
    suspend fun deleteBatchById(id: Long)
    
    @Query("UPDATE product_batches SET quantity = quantity - :consumedQuantity WHERE id = :id")
    suspend fun consumeFromBatch(id: Long, consumedQuantity: Double)
    
    @Query("UPDATE product_batches SET isConsumed = 1 WHERE id = :id")
    suspend fun markBatchAsConsumed(id: Long)
}
