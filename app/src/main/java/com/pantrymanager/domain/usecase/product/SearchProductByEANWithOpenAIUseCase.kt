package com.pantrymanager.domain.usecase.product

import com.pantrymanager.data.dto.ProductSearchResultOpenAI
import com.pantrymanager.data.service.OpenAIProductSearchService
import javax.inject.Inject

/**
 * Use Case para buscar informações de produtos via OpenAI ChatGPT
 */
class SearchProductByEANWithOpenAIUseCase @Inject constructor(
    private val openAIProductSearchService: OpenAIProductSearchService
) {
    
    /**
     * Busca informações de um produto pelo EAN usando ChatGPT
     * 
     * @param ean Código EAN do produto
     * @return Result com informações do produto ou erro
     */
    suspend operator fun invoke(ean: String): Result<ProductSearchResultOpenAI> {
        return try {
            if (ean.isBlank()) {
                return Result.failure(IllegalArgumentException("EAN não pode estar vazio"))
            }
            
            // Validar formato do EAN (básico)
            if (!isValidEANFormat(ean)) {
                return Result.failure(IllegalArgumentException("Formato de EAN inválido"))
            }
            
            openAIProductSearchService.searchProductByEAN(ean)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Validação básica do formato EAN
     */
    private fun isValidEANFormat(ean: String): Boolean {
        return when (ean.length) {
            8, 12, 13, 14 -> ean.all { it.isDigit() }
            else -> false
        }
    }
}
