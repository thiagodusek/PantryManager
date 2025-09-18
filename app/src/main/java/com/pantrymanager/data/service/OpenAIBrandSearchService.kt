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
import javax.inject.Singleton

/**
 * Serviço para buscar marcas via ChatGPT/OpenAI
 */
@Singleton
class OpenAIBrandSearchService @Inject constructor(
    private val openAIApiService: OpenAIApiService,
    private val moshi: Moshi
) {
    companion object {
        private const val TAG = "OpenAIBrandSearch"
    }

    /**
     * Busca uma lista de marcas famosas que podem ser encontradas em supermercados brasileiros
     */
    suspend fun searchPopularBrands(): Result<List<String>> {
        return try {
            Log.d(TAG, "Buscando marcas populares via ChatGPT...")
            
            val prompt = """
                Liste as 100 marcas mais populares e conhecidas que podem ser encontradas em supermercados no Brasil.
                
                Inclua marcas de:
                - Alimentos e bebidas
                - Produtos de limpeza e higiene
                - Produtos de beleza e cuidados pessoais
                - Produtos para casa
                - Marcas nacionais e internacionais
                
                Retorne apenas os nomes das marcas, um por linha, sem numeração ou formatação extra.
                Exemplo:
                Nestlé
                Coca-Cola
                Unilever
                Johnson & Johnson
                
                Importante: Retorne apenas nomes de marcas reais e conhecidas.
            """.trimIndent()

            val messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )

            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = messages,
                maxTokens = 2000,
                temperature = 0.3
            )

            val response = openAIApiService.getChatCompletion(
                "Bearer ${OpenAIConfig.getApiKey()}",
                "application/json",
                request
            )
            
            if (response.isSuccessful && response.body()?.choices?.isNotEmpty() == true) {
                val content = response.body()!!.choices[0].message.content.trim()
                Log.d(TAG, "Resposta do ChatGPT: $content")
                
                // Processar a resposta para extrair lista de marcas
                val brands = content.split("\n")
                    .map { it.trim() }
                    .filter { it.isNotBlank() && !it.matches(Regex("\\d+\\..*")) } // Remove numeração
                    .distinct()
                    .take(100) // Limita a 100 marcas
                
                Log.d(TAG, "Marcas encontradas: ${brands.size}")
                Result.success(brands)
            } else {
                Log.w(TAG, "Nenhuma resposta do ChatGPT")
                Result.failure(Exception("Nenhuma resposta do ChatGPT"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar marcas via ChatGPT: ${e.message}", e)
            Result.failure(e)
        }
    }
}
