package com.pantrymanager.data.repository

import com.pantrymanager.data.datasource.ProductBatchDao
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.data.dto.toEntity
import com.pantrymanager.domain.entity.ProductBatch
import com.pantrymanager.domain.repository.ProductBatchRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductBatchRepositoryImpl @Inject constructor(
    private val dao: ProductBatchDao
) : ProductBatchRepository {
    
    override suspend fun insertBatch(batch: ProductBatch): Long {
        return dao.insertBatch(batch.toEntity())
    }
    
    override suspend fun updateBatch(batch: ProductBatch) {
        dao.updateBatch(batch.toEntity())
    }
    
    override suspend fun deleteBatch(batchId: Long) {
        dao.deleteBatchById(batchId)
    }
    
    override suspend fun getBatchById(batchId: Long): ProductBatch? {
        return dao.getBatchById(batchId)?.toDomain()
    }
    
    override suspend fun getBatchesByProduct(productId: Long, userId: String): List<ProductBatch> {
        return dao.getBatchesByProduct(productId, userId).map { it.toDomain() }
    }
    
    override suspend fun getBatchesByUser(userId: String): List<ProductBatch> {
        return dao.getAllBatches(userId).map { it.toDomain() }
    }
    
    override suspend fun getExpiredBatches(userId: String): List<ProductBatch> {
        val today = LocalDate.now().toString() // Convert to ISO date string (YYYY-MM-DD)
        return dao.getExpiredBatches(userId, today).map { it.toDomain() }
    }
    
    override suspend fun getExpiringBatches(userId: String, daysAhead: Int): List<ProductBatch> {
        val today = LocalDate.now().toString() // Convert to ISO date string
        val endDate = LocalDate.now().plusDays(daysAhead.toLong()).toString() // Convert to ISO date string
        return dao.getBatchesExpiringWithin(userId, today, endDate).map { it.toDomain() }
    }
    
    override suspend fun consumeBatch(batchId: Long) {
        dao.markBatchAsConsumed(batchId)
    }
}
