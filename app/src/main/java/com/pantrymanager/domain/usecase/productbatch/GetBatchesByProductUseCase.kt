package com.pantrymanager.domain.usecase.productbatch

import com.pantrymanager.domain.entity.ProductBatch
import com.pantrymanager.domain.repository.ProductBatchRepository
import javax.inject.Inject

class GetBatchesByProductUseCase @Inject constructor(
    private val productBatchRepository: ProductBatchRepository
) {
    suspend operator fun invoke(productId: Long, userId: String): Result<List<ProductBatch>> {
        return try {
            val batches = productBatchRepository.getBatchesByProduct(productId, userId)
            Result.success(batches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
