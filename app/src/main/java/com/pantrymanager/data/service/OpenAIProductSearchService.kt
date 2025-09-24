package com.pantrymanager.data.service

import android.util.Log
import com.pantrymanager.data.config.OpenAIConfig
import com.pantrymanager.data.dto.OpenAIMessage
import com.pantrymanager.data.dto.OpenAIRequest
import com.pantrymanager.data.dto.ProductSearchResultOpenAI
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Serviço para buscar informações de produtos usando OpenAI ChatGPT
 */
@Singleton
class OpenAIProductSearchService @Inject constructor(
    private val openAIApiService: OpenAIApiService
) {
    
    companion object {
        private const val TAG = "OpenAIProductSearch"
        private const val BEARER_PREFIX = "Bearer "
    }
    
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val productAdapter: JsonAdapter<ProductSearchResultOpenAI> = 
        moshi.adapter(ProductSearchResultOpenAI::class.java)
    
    /**
     * Busca informações de um produto pelo código EAN usando ChatGPT
     */
    suspend fun searchProductByEAN(ean: String): Result<ProductSearchResultOpenAI> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Buscando produto com EAN: $ean")
            
            // Verificar se a chave da API está configurada
            if (!OpenAIConfig.isApiKeyConfigured()) {
                Log.w(TAG, "Chave da OpenAI não configurada")
                return@withContext Result.failure(
                    IllegalStateException("Chave da OpenAI não configurada. Configure em OpenAIConfig.kt")
                )
            }
            
            // Construir prompt estruturado para o ChatGPT
            val prompt = buildProductSearchPrompt(ean)
            
            val request = OpenAIRequest(
                model = OpenAIConfig.DEFAULT_MODEL,
                messages = listOf(
                    OpenAIMessage(
                        role = "system",
                        content = """Você é um assistente especializado em informações de produtos.
                        |Quando receber um código EAN/código de barras, pesquise informações sobre o produto correspondente.
                        |Responda sempre em formato JSON válido com os campos solicitados.
                        |Se não encontrar o produto, retorne found: false.
                        |Use informações precisas e atualizadas sobre produtos brasileiros sempre que possível.""".trimMargin()
                    ),
                    OpenAIMessage(
                        role = "user", 
                        content = prompt
                    )
                ),
                maxTokens = OpenAIConfig.MAX_TOKENS,
                temperature = OpenAIConfig.TEMPERATURE
            )
            
            val response = openAIApiService.getChatCompletion(
                authorization = "$BEARER_PREFIX${OpenAIConfig.getApiKey()}",
                request = request
            )
            
            if (response.isSuccessful && response.body() != null) {
                val openAIResponse = response.body()!!
                val content = openAIResponse.choices.firstOrNull()?.message?.content
                
                if (content != null) {
                    Log.d(TAG, "Resposta do ChatGPT: $content")
                    
                    // Tentar fazer parse da resposta JSON
                    val productResult = parseProductResponse(content, ean)
                    Log.d(TAG, "Produto encontrado: ${productResult.found}")
                    
                    Result.success(productResult)
                } else {
                    Log.e(TAG, "Resposta vazia do ChatGPT")
                    Result.success(createNotFoundResult(ean))
                }
            } else {
                Log.e(TAG, "Erro na requisição: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Erro na consulta à OpenAI: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar produto: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Constrói o prompt estruturado para buscar informações do produto
     */
    private fun buildProductSearchPrompt(ean: String): String {
        return """
        |Busque para mim em forma de tabela o nome do produto, descrição, unidade de medida, categoria, observações e foto do produto com EAN $ean da pesquisa
        |
        |Relacione a pesquisa aos campos de cadastro de produtos que temos no Firebase:
        |
        |Retorne em formato JSON:
        |{
        |    "ean": "$ean",
        |    "name": "Nome completo do produto (campo 'name' no Firebase)",
        |    "description": "Descrição detalhada do produto (campo 'description' no Firebase)", 
        |    "imageUrl": "URL de uma foto/imagem do produto (campo 'imageUrl' no Firebase)",
        |    "brand": "Marca/fabricante do produto (campo 'brand' no Firebase)",
        |    "category": "Categoria do produto (campo 'category' no Firebase)",
        |    "unit": "Unidade de medida principal (campo 'measurementUnit' no Firebase)",
        |    "unitAbbreviation": "Abreviação da unidade (ex: kg, L, un, g)",
        |    "averagePrice": valor_numerico_preco_medio_em_reais,
        |    "weight": "Peso/volume/quantidade da embalagem (ex: 1.0 para 1kg, 500 para 500ml)",
        |    "nutritionalInfo": "Informações nutricionais resumidas se for alimento",
        |    "observations": "Observações importantes sobre o produto (campo 'observation' no Firebase)",
        |    "found": true_ou_false
        |}
        |
        |INSTRUÇÕES IMPORTANTES:
        |- Busque informações reais e precisas sobre produtos vendidos no Brasil
        |- Se o produto não for encontrado ou não existir, retorne found: false
        |- Para imageUrl, use URLs de imagens reais dos produtos quando possível
        |- Para category, use categorias brasileiras: "Alimentos e Bebidas", "Higiene e Beleza", "Limpeza e Casa", "Eletrônicos", "Roupas e Acessórios", "Farmácia", "Pet Shop", "Bebê", "Automotivo", "Esporte e Lazer"
        |- Para unit, use: "Quilograma", "Grama", "Litro", "Mililitro", "Unidade", "Metro", "Centímetro", "Pacote"
        |- Para averagePrice, pesquise preços atuais em supermercados brasileiros
        |- Para weight, extraia o peso/volume da embalagem (ex: 1 para 1kg, 0.5 para 500g, 2 para 2L)
        |
        |Responda APENAS com o JSON válido, sem texto adicional ou formatação markdown.
        """.trimMargin()
    }
    
    /**
     * Faz parse da resposta JSON do ChatGPT
     */
    private fun parseProductResponse(content: String, ean: String): ProductSearchResultOpenAI {
        return try {
            // Limpar a resposta (remover markdown, etc.)
            val cleanContent = content
                .replace("```json", "")
                .replace("```", "")
                .trim()
            
            Log.d(TAG, "JSON limpo: $cleanContent")
            
            // Tentar fazer parse do JSON
            val result = productAdapter.fromJson(cleanContent)
            result ?: createNotFoundResult(ean)
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao fazer parse da resposta JSON: ${e.message}", e)
            Log.d(TAG, "Conteúdo que causou erro: $content")
            
            // Tentar extrair informações básicas da resposta se o JSON falhar
            tryExtractBasicInfo(content, ean)
        }
    }
    
    /**
     * Tenta extrair informações básicas se o parse JSON falhar
     */
    private fun tryExtractBasicInfo(content: String, ean: String): ProductSearchResultOpenAI {
        return try {
            // Procurar por padrões básicos na resposta
            val nameRegex = "\"name\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val descriptionRegex = "\"description\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val brandRegex = "\"brand\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val categoryRegex = "\"category\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            
            val name = nameRegex.find(content)?.groupValues?.get(1)
            val description = descriptionRegex.find(content)?.groupValues?.get(1)
            val brand = brandRegex.find(content)?.groupValues?.get(1)
            val category = categoryRegex.find(content)?.groupValues?.get(1)
            
            if (name != null) {
                ProductSearchResultOpenAI(
                    ean = ean,
                    name = name ?: "",
                    description = description ?: "",
                    brand = brand ?: "",
                    category = category ?: "",
                    unit = "Unidade", // Padrão
                    unitAbbreviation = "un",
                    averagePrice = 0.0,
                    weight = "",
                    nutritionalInfo = null,
                    observations = null,
                    found = true,
                    source = "openai_fallback"
                )
            } else {
                createNotFoundResult(ean)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro na extração básica: ${e.message}", e)
            createNotFoundResult(ean)
        }
    }
    
    /**
     * Cria resultado para produto não encontrado
     */
    private fun createNotFoundResult(ean: String): ProductSearchResultOpenAI {
        return ProductSearchResultOpenAI(
            ean = ean,
            name = "",
            description = "",
            imageUrl = null,
            brand = "",
            category = "",
            unit = "",
            unitAbbreviation = "",
            averagePrice = 0.0,
            weight = "",
            nutritionalInfo = null,
            observations = null,
            found = false,
            source = "openai_not_found"
        )
    }
}
