package com.pantrymanager.domain.usecase.product

import com.pantrymanager.data.dto.ProductSearchResult
import com.pantrymanager.data.service.ProductSearchService
import javax.inject.Inject

/**
 * Caso de uso para buscar produtos por código de barras
 */
class SearchProductByBarcodeUseCase @Inject constructor(
    private val productSearchService: ProductSearchService
) {
    suspend operator fun invoke(barcode: String): Result<ProductSearchResult> {
        return try {
            val result = productSearchService.searchByBarcode(barcode)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
