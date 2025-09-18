package com.pantrymanager.data.service

import android.util.Log
import com.pantrymanager.data.config.OpenAIConfig
import com.pantrymanager.data.dto.OpenAIRequest
import com.pantrymanager.data.dto.OpenAIMessage
import com.pantrymanager.data.dto.OpenAIResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

/**
 * Serviço para buscar categorias de supermercado via OpenAI ChatGPT
 */
interface OpenAICategorySearchService {
    suspend fun getAllSupermarketCategories(): List<String>
}

class OpenAICategorySearchServiceImpl @Inject constructor(
    private val openAIApiService: OpenAIApiService,
    private val moshi: Moshi
) : OpenAICategorySearchService {
    
    override suspend fun getAllSupermarketCategories(): List<String> {
        return try {
            val prompt = """
                Liste todas as categorias de produtos que existem em supermercados brasileiros.
                Forneça uma lista completa e organizada das principais categorias, incluindo:
                - Alimentos básicos (cereais, grãos, farinhas, etc.)
                - Proteínas (carnes, peixes, ovos, etc.)
                - Laticínios (leites, queijos, iogurtes, etc.)
                - Frutas, legumes e verduras
                - Bebidas (águas, refrigerantes, sucos, etc.)
                - Produtos de higiene pessoal
                - Produtos de limpeza e casa
                - Produtos para bebês
                - Produtos para pets
                - Farmácia e saúde
                - E outras categorias relevantes
                
                Retorne APENAS uma lista JSON simples com os nomes das categorias, no formato:
                ["Categoria 1", "Categoria 2", "Categoria 3"]
                
                Sem explicações adicionais, apenas o JSON.
            """.trimIndent()
            
            val messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )
            
            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = messages,
                maxTokens = 1000,
                temperature = 0.3
            )
            
            val response = openAIApiService.getChatCompletion(
                "Bearer ${OpenAIConfig.getApiKey()}",
                "application/json",
                request
            )
            
            Log.d("OpenAICategorySearch", "Response: ${response.body()?.choices?.firstOrNull()?.message?.content}")
            
            if (response.isSuccessful && response.body() != null) {
                parseCategoriesFromResponse(response.body()!!)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OpenAICategorySearch", "Erro ao buscar categorias: ${e.message}", e)
            emptyList()
        }
    }
    
    private fun parseCategoriesFromResponse(response: OpenAIResponse): List<String> {
        return try {
            val content = response.choices.firstOrNull()?.message?.content ?: return emptyList()
            
            // Tentar extrair JSON da resposta
            val jsonContent = extractJsonFromContent(content)
            
            // Parse do JSON
            val listType = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter: JsonAdapter<List<String>> = moshi.adapter(listType)
            
            adapter.fromJson(jsonContent) ?: emptyList()
        } catch (e: Exception) {
            Log.w("OpenAICategorySearch", "Erro ao fazer parse das categorias: ${e.message}")
            // Fallback: tentar extrair manualmente linha por linha
            extractCategoriesManually(response.choices.firstOrNull()?.message?.content ?: "")
        }
    }
    
    private fun extractJsonFromContent(content: String): String {
        // Procurar por JSON array na resposta
        val jsonStart = content.indexOf("[")
        val jsonEnd = content.lastIndexOf("]")
        
        return if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            content.substring(jsonStart, jsonEnd + 1)
        } else {
            content
        }
    }
    
    private fun extractCategoriesManually(content: String): List<String> {
        return content.lines()
            .mapNotNull { line ->
                line.trim()
                    .removePrefix("-")
                    .removePrefix("*")
                    .removePrefix("•")
                    .trim()
                    .takeIf { it.isNotBlank() && !it.startsWith("Lista") && !it.startsWith("Categorias") }
            }
            .filter { it.length > 3 }
            .distinct()
    }
}
