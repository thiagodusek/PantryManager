package com.pantrymanager.domain.repository

import com.pantrymanager.domain.entity.ProductBatch

interface ProductBatchRepository {
    suspend fun insertBatch(batch: ProductBatch): Long
    suspend fun updateBatch(batch: ProductBatch)
    suspend fun deleteBatch(batchId: Long)
    suspend fun getBatchById(batchId: Long): ProductBatch?
    suspend fun getBatchesByProduct(productId: Long, userId: String): List<ProductBatch>
    suspend fun getBatchesByUser(userId: String): List<ProductBatch>
    suspend fun getExpiredBatches(userId: String): List<ProductBatch>
    suspend fun getExpiringBatches(userId: String, daysAhead: Int = 7): List<ProductBatch>
    suspend fun consumeBatch(batchId: Long)
}
