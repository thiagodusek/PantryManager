package com.pantrymanager.data.service

import com.pantrymanager.data.dto.ViaCepResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface para serviços da API ViaCEP
 */
interface ViaCepService {
    
    /**
     * Busca informações de endereço pelo CEP
     * @param cep CEP para busca (formato: 12345678)
     * @return Resposta com informações do endereço
     */
    @GET("ws/{cep}/json/")
    suspend fun getAddressByCep(@Path("cep") cep: String): Response<ViaCepResponse>
}
