package com.pantrymanager.data.service

import android.util.Log
import com.pantrymanager.data.dto.ProductSearchResult
import com.pantrymanager.domain.usecase.product.SearchProductByEANWithOpenAIUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Serviço de pesquisa de produtos por código de barras
 * Agora integrado com OpenAI ChatGPT para busca inteligente
 */
interface ProductSearchService {
    suspend fun searchByBarcode(barcode: String): ProductSearchResult
}

class ProductSearchServiceImpl @Inject constructor(
    private val searchProductByEANWithOpenAIUseCase: SearchProductByEANWithOpenAIUseCase
) : ProductSearchService {
    
    override suspend fun searchByBarcode(barcode: String): ProductSearchResult {
        Log.d("ProductSearchService", "Buscando produto com EAN: $barcode")
        
        // Primeiro tenta buscar no ChatGPT/OpenAI
        return try {
            val openAIResult = searchProductByEANWithOpenAIUseCase(barcode)
            
            if (openAIResult.isSuccess) {
                val openAIData = openAIResult.getOrThrow()
                
                if (openAIData.found) {
                    Log.d("ProductSearchService", "Produto encontrado via OpenAI: ${openAIData.name}")
                    
                    // Converter resultado do OpenAI para ProductSearchResult
                    return ProductSearchResult(
                        ean = openAIData.ean,
                        name = openAIData.name,
                        description = openAIData.description,
                        brand = openAIData.brand,
                        imageUrl = openAIData.imageUrl,
                        averagePrice = openAIData.averagePrice ?: 0.0,
                        category = openAIData.category,
                        unit = openAIData.unit,
                        unitAbbreviation = openAIData.unitAbbreviation,
                        found = true,
                        source = "openai"
                    )
                } else {
                    Log.d("ProductSearchService", "Produto não encontrado no OpenAI, tentando fallback")
                }
            } else {
                Log.w("ProductSearchService", "Erro na busca OpenAI: ${openAIResult.exceptionOrNull()?.message}")
            }
            
            // Se OpenAI falhar ou não encontrar, usar fallback (base simulada)
            searchWithFallback(barcode)
            
        } catch (e: Exception) {
            Log.e("ProductSearchService", "Erro na busca por produto: ${e.message}", e)
            searchWithFallback(barcode)
        }
    }
    
    /**
     * Método de fallback usando base simulada
     */
    private suspend fun searchWithFallback(barcode: String): ProductSearchResult {
        Log.d("ProductSearchService", "Usando busca fallback para EAN: $barcode")
        
        // Simula delay de rede
        delay(1500)
        
        // Simula pesquisa em base de dados de produtos brasileiros
        return when {
            barcode.startsWith("789") -> searchBrazilianProduct(barcode)
            barcode.length == 13 -> searchInternationalProduct(barcode)
            barcode.length == 12 -> searchUPCProduct(barcode)
            else -> ProductSearchResult(
                ean = barcode,
                name = null,
                description = null,
                brand = null,
                imageUrl = null,
                found = false,
                source = "not_found"
            )
        }
    }
    
    private fun searchBrazilianProduct(barcode: String): ProductSearchResult {
        // Simula base de dados de produtos brasileiros
        val brazilianProducts = mapOf(
            "7891000100103" to ProductSearchResult(
                ean = barcode,
                name = "Leite Condensado Nestlé",
                description = "Leite condensado tradicional 395g",
                brand = "Nestlé",
                imageUrl = "https://example.com/leite-condensado.jpg",
                averagePrice = 4.50,
                category = "Laticínios",
                source = "brazilian_db"
            ),
            "7891234567890" to ProductSearchResult(
                ean = barcode,
                name = "Arroz Tio João",
                description = "Arroz branco tipo 1, 1kg",
                brand = "Tio João",
                imageUrl = "https://example.com/arroz.jpg",
                averagePrice = 6.80,
                category = "Cereais",
                source = "brazilian_db"
            ),
            "7891000053508" to ProductSearchResult(
                ean = barcode,
                name = "Nescau 2.0",
                description = "Achocolatado em pó 400g",
                brand = "Nestlé",
                imageUrl = "https://example.com/nescau.jpg",
                averagePrice = 8.90,
                category = "Bebidas",
                source = "brazilian_db"
            )
        )
        
        return brazilianProducts[barcode] ?: generateGenericProduct(barcode, "Produto Brasileiro")
    }
    
    private fun searchInternationalProduct(barcode: String): ProductSearchResult {
        // Simula consulta a APIs internacionais (UPC Database, Open Food Facts, etc.)
        return generateGenericProduct(barcode, "Produto Importado")
    }
    
    private fun searchUPCProduct(barcode: String): ProductSearchResult {
        // Simula consulta a base UPC americana
        return generateGenericProduct(barcode, "Produto UPC")
    }
    
    private fun generateGenericProduct(barcode: String, prefix: String): ProductSearchResult {
        return ProductSearchResult(
            ean = barcode,
            name = "$prefix - $barcode",
            description = "Produto encontrado por código de barras",
            brand = "Marca não identificada",
            imageUrl = null,
            averagePrice = 0.0,
            category = "Diversos",
            source = "generated"
        )
    }
}
