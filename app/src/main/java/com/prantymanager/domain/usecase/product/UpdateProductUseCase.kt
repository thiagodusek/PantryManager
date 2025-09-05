package com.prantymanager.domain.usecase.product

import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
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
            
            productRepository.updateProduct(product)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
