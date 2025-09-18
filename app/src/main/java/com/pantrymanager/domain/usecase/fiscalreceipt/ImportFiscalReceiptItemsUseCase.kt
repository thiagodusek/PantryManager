package com.pantrymanager.domain.usecase.fiscalreceipt

import com.pantrymanager.domain.repository.FiscalReceiptRepository
import javax.inject.Inject

/**
 * Use Case para importar itens específicos de um cupom fiscal como produtos
 */
class ImportFiscalReceiptItemsUseCase @Inject constructor(
    private val fiscalReceiptRepository: FiscalReceiptRepository
) {
    
    /**
     * Importa itens específicos de um cupom fiscal como produtos
     * @param itemIds Lista de IDs dos itens a serem importados
     * @return Lista de IDs dos produtos criados/atualizados
     */
    suspend operator fun invoke(itemIds: List<Long>): Result<List<Long>> {
        return fiscalReceiptRepository.importItemsAsProducts(itemIds)
    }
}
