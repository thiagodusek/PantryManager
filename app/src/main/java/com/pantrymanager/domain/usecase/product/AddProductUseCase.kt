package com.pantrymanager.domain.usecase.product

import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Long> {
        return try {
            // Validação dos campos obrigatórios
            if (product.name.isBlank()) {
                return Result.failure(Exception("Nome do produto é obrigatório"))
            }
            
            if (product.categoryId <= 0) {
                return Result.failure(Exception("Categoria é obrigatória"))
            }
            
            if (product.unitId <= 0) {
                return Result.failure(Exception("Unidade é obrigatória"))
            }
            
            val productId = productRepository.insertProduct(product)
            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
