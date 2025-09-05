package com.pantrymanager.data.repository

import com.pantrymanager.data.dto.ViaCepResponse
import com.pantrymanager.data.service.ViaCepService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositório para serviços de CEP (ViaCEP API)
 */
@Singleton
class CepRepository @Inject constructor(
    private val viaCepService: ViaCepService
) {
    
    /**
     * Busca informações de endereço pelo CEP
     * @param cep CEP para busca (formato: 12345678)
     * @return Resposta com informações do endereço ou null em caso de erro
     */
    suspend fun getAddressByCep(cep: String): ViaCepResponse? {
        return try {
            val cleanCep = cep.replace("-", "").replace(".", "").trim()
            if (cleanCep.length != 8 || !cleanCep.all { it.isDigit() }) {
                return null
            }
            
            val response = viaCepService.getAddressByCep(cleanCep)
            if (response.isSuccessful && response.body()?.erro != true) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
