package com.pantrymanager.domain.usecase.fiscalreceipt

import com.pantrymanager.data.service.FiscalReceiptQrProcessor
import com.pantrymanager.data.service.QrCodeProcessResult
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.repository.FiscalReceiptRepository
import com.pantrymanager.domain.usecase.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Resultado da importação do cupom fiscal via QR Code
 */
sealed class ImportFiscalReceiptQrResult {
    data class Success(
        val fiscalReceiptId: Long,
        val importedProductsCount: Int
    ) : ImportFiscalReceiptQrResult()
    
    data class Error(val message: String) : ImportFiscalReceiptQrResult()
    object InvalidQrCode : ImportFiscalReceiptQrResult()
    object UserNotAuthenticated : ImportFiscalReceiptQrResult()
}

/**
 * Use Case para importar cupom fiscal via QR Code
 */
class ImportFiscalReceiptByQrCodeUseCase @Inject constructor(
    private val fiscalReceiptQrProcessor: FiscalReceiptQrProcessor,
    private val fiscalReceiptRepository: FiscalReceiptRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    
    /**
     * Importa um cupom fiscal através do QR Code
     * @param qrCodeData Dados do QR Code lido
     * @param autoImportProducts Se deve importar automaticamente os produtos
     */
    suspend operator fun invoke(
        qrCodeData: String,
        autoImportProducts: Boolean = true
    ): ImportFiscalReceiptQrResult {
        
        // Verificar usuário autenticado
        val currentUserFlow = getCurrentUserUseCase()
        val currentUser = currentUserFlow.first()
        if (currentUser == null) {
            return ImportFiscalReceiptQrResult.UserNotAuthenticated
        }
        
        // Processar QR Code
        when (val processResult = fiscalReceiptQrProcessor.processQrCode(qrCodeData)) {
            is QrCodeProcessResult.Success -> {
                return try {
                    // Adicionar ID do usuário ao cupom fiscal
                    val fiscalReceiptWithUser = processResult.fiscalReceipt.copy(
                        userId = currentUser.id
                    )
                    
                    // Salvar cupom fiscal no repositório
                    val fiscalReceiptId = fiscalReceiptRepository.insertFiscalReceipt(fiscalReceiptWithUser)
                    
                    // Importar produtos automaticamente se solicitado
                    if (autoImportProducts) {
                        val importResult = fiscalReceiptRepository.processFiscalReceiptItems(fiscalReceiptId)
                        
                        if (importResult.isSuccess) {
                            val importedProductsCount = importResult.getOrThrow().size
                            ImportFiscalReceiptQrResult.Success(fiscalReceiptId, importedProductsCount)
                        } else {
                            // Cupom foi salvo, mas houve erro na importação dos produtos
                            ImportFiscalReceiptQrResult.Success(fiscalReceiptId, 0)
                        }
                    } else {
                        ImportFiscalReceiptQrResult.Success(fiscalReceiptId, 0)
                    }
                    
                } catch (e: Exception) {
                    ImportFiscalReceiptQrResult.Error("Erro ao salvar cupom fiscal: ${e.message}")
                }
            }
            
            is QrCodeProcessResult.Error -> {
                return ImportFiscalReceiptQrResult.Error(processResult.message)
            }
            
            is QrCodeProcessResult.InvalidQrCode -> {
                return ImportFiscalReceiptQrResult.InvalidQrCode
            }
        }
    }
    
    /**
     * Valida se um QR Code é de cupom fiscal
     */
    fun validateQrCode(qrCodeData: String): Boolean {
        return fiscalReceiptQrProcessor.isValidFiscalReceiptQrCode(qrCodeData)
    }
}
