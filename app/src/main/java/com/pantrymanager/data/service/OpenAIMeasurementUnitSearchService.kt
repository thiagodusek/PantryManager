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
 * DTO para unidade de medida retornada pelo ChatGPT
 */
data class OpenAIMeasurementUnitData(
    val name: String,
    val abbreviation: String,
    val description: String? = null,
    val multiplyQuantityByPrice: Boolean = false
)

/**
 * Serviço para buscar unidades de medida via OpenAI ChatGPT
 */
interface OpenAIMeasurementUnitSearchService {
    suspend fun getAllMeasurementUnits(): List<OpenAIMeasurementUnitData>
}

class OpenAIMeasurementUnitSearchServiceImpl @Inject constructor(
    private val openAIApiService: OpenAIApiService,
    private val moshi: Moshi
) : OpenAIMeasurementUnitSearchService {
    
    override suspend fun getAllMeasurementUnits(): List<OpenAIMeasurementUnitData> {
        return try {
            val prompt = """
                Liste todas as unidades de medida utilizadas em supermercados brasileiros para produtos alimentícios e domésticos.
                Inclua unidades de:
                - Peso (kg, g, mg, t, etc.)
                - Volume (L, mL, m³, cm³, etc.)
                - Quantidade (un, par, dz, cento, etc.)
                - Comprimento (m, cm, mm, etc.)
                - Área (m², cm², etc.)
                - Embalagens (pct, cx, fardo, saco, lata, frasco, etc.)
                - Culinária (colher, xícara, copo, pitada, etc.)
                - Tempo (dia, semana, mês, ano, etc.)
                - Energia (cal, kcal, J, etc.)
                - Especiais (garrafa, tubo, sachê, spray, etc.)
                
                Retorne um JSON array com objetos contendo:
                - name: nome completo da unidade
                - abbreviation: abreviação
                - description: descrição breve (opcional)
                - multiplyQuantityByPrice: boolean indicando se deve multiplicar quantidade por preço (true para peso/volume, false para unidades)
                
                Formato esperado:
                [
                  {
                    "name": "Quilograma",
                    "abbreviation": "kg",
                    "description": "Unidade de massa - 1000 gramas",
                    "multiplyQuantityByPrice": true
                  },
                  {
                    "name": "Unidade",
                    "abbreviation": "un",
                    "description": "Peça individual",
                    "multiplyQuantityByPrice": false
                  }
                ]
                
                Retorne APENAS o JSON, sem explicações adicionais.
            """.trimIndent()
            
            val messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )
            
            val request = OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = messages,
                maxTokens = 1500,
                temperature = 0.3
            )
            
            val response = openAIApiService.getChatCompletion(
                "Bearer ${OpenAIConfig.getApiKey()}",
                "application/json",
                request
            )
            
            Log.d("OpenAIUnitSearch", "Response: ${response.body()?.choices?.firstOrNull()?.message?.content}")
            
            if (response.isSuccessful && response.body() != null) {
                parseUnitsFromResponse(response.body()!!)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OpenAIUnitSearch", "Erro ao buscar unidades: ${e.message}", e)
            emptyList()
        }
    }
    
    private fun parseUnitsFromResponse(response: OpenAIResponse): List<OpenAIMeasurementUnitData> {
        return try {
            val content = response.choices.firstOrNull()?.message?.content ?: return emptyList()
            
            // Tentar extrair JSON da resposta
            val jsonContent = extractJsonFromContent(content)
            
            // Parse do JSON
            val listType = Types.newParameterizedType(List::class.java, OpenAIMeasurementUnitData::class.java)
            val adapter: JsonAdapter<List<OpenAIMeasurementUnitData>> = moshi.adapter(listType)
            
            adapter.fromJson(jsonContent) ?: emptyList()
        } catch (e: Exception) {
            Log.w("OpenAIUnitSearch", "Erro ao fazer parse das unidades: ${e.message}")
            // Fallback: retornar lista vazia, as unidades padrão já existem
            emptyList()
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
}
