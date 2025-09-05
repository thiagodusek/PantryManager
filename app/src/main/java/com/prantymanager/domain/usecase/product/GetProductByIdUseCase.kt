package com.prantymanager.domain.usecase.product

import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Long): Product? {
        return productRepository.getProductById(productId).first()
    }
}
