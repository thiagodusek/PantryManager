package com.pantrymanager.domain.usecase.address

import com.pantrymanager.data.repository.CepRepository
import com.pantrymanager.domain.entity.Address
import javax.inject.Inject

/**
 * Use case para buscar endereço por CEP usando ViaCEP API
 */
class GetAddressByCepUseCase @Inject constructor(
    private val cepRepository: CepRepository
) {
    
    /**
     * Busca informações de endereço pelo CEP
     * @param cep CEP para busca
     * @return Address ou null se não encontrado
     */
    suspend fun execute(cep: String): Address? {
        val response = cepRepository.getAddressByCep(cep)
        return response?.let {
            Address(
                endereco = it.logradouro,
                numero = "",
                complemento = it.complemento.takeIf { comp -> comp.isNotBlank() },
                cep = it.cep,
                cidade = it.localidade,
                estado = it.uf
            )
        }
    }
}
