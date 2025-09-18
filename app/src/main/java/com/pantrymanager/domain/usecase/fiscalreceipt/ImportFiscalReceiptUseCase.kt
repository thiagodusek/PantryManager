package com.pantrymanager.domain.usecase.fiscalreceipt

import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.repository.FiscalReceiptRepository
import javax.inject.Inject

/**
 * Use Case para importar um cupom fiscal
 */
class ImportFiscalReceiptUseCase @Inject constructor(
    private val fiscalReceiptRepository: FiscalReceiptRepository
) {
    
    /**
     * Importa um cupom fiscal e todos os seus produtos
     * Respeita o EAN como chave única por usuário
     */
    suspend operator fun invoke(fiscalReceipt: FiscalReceipt): Result<Long> {
        return try {
            // Primeiro salva o cupom fiscal
            val receiptId = fiscalReceiptRepository.insertFiscalReceipt(fiscalReceipt)
            
            // Depois processa os itens (importa os produtos respeitando o EAN único)
            val processResult = fiscalReceiptRepository.processFiscalReceiptItems(receiptId)
            
            if (processResult.isSuccess) {
                Result.success(receiptId)
            } else {
                Result.failure(processResult.exceptionOrNull() 
                    ?: Exception("Erro ao processar itens do cupom fiscal"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
