package com.pantrymanager.domain.usecase.fiscalreceipt

import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.repository.FiscalReceiptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case para buscar cupom fiscal por ID
 */
class GetFiscalReceiptByIdUseCase @Inject constructor(
    private val fiscalReceiptRepository: FiscalReceiptRepository
) {
    operator fun invoke(fiscalReceiptId: Long): Flow<FiscalReceipt?> {
        return fiscalReceiptRepository.getFiscalReceiptById(fiscalReceiptId)
    }
}
