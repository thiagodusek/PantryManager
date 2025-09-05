package com.pantrymanager.domain.usecase.product

import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getAllProducts().first()
    }
}
