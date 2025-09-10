package com.pantrymanager.domain.usecase.productbatch

import com.pantrymanager.domain.entity.ProductBatch
import com.pantrymanager.domain.repository.ProductBatchRepository
import javax.inject.Inject

class AddProductBatchUseCase @Inject constructor(
    private val productBatchRepository: ProductBatchRepository
) {
    suspend operator fun invoke(batch: ProductBatch): Result<Long> {
        return try {
            // Validações
            if (batch.quantity <= 0) {
                return Result.failure(Exception("Quantidade deve ser maior que zero"))
            }
            
            if (batch.batchNumber.isBlank()) {
                return Result.failure(Exception("Número do lote é obrigatório"))
            }
            
            val batchId = productBatchRepository.insertBatch(batch)
            Result.success(batchId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
