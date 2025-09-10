package com.pantrymanager.domain.usecase.fiscalreceipt

import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.repository.FiscalReceiptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case para obter cupons fiscais não processados
 */
class GetUnprocessedFiscalReceiptsUseCase @Inject constructor(
    private val fiscalReceiptRepository: FiscalReceiptRepository
) {
    
    operator fun invoke(userId: String = ""): Flow<List<FiscalReceipt>> {
        return fiscalReceiptRepository.getUnprocessedFiscalReceipts(userId)
    }
}
