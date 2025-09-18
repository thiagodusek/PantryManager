package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.domain.usecase.fiscalreceipt.ImportFiscalReceiptByQrCodeUseCase
import com.pantrymanager.domain.usecase.fiscalreceipt.ImportFiscalReceiptQrResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiscalReceiptScannerState(
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val lastScannedQrCode: String? = null,
    val lastImportedReceiptId: Long? = null,
    val lastImportedProductsCount: Int? = null
)

@HiltViewModel
class FiscalReceiptScannerViewModel @Inject constructor(
    private val importFiscalReceiptByQrCodeUseCase: ImportFiscalReceiptByQrCodeUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(FiscalReceiptScannerState())
    val state: StateFlow<FiscalReceiptScannerState> = _state.asStateFlow()
    
    /**
     * Processa um QR Code escaneado
     */
    fun processQrCode(qrCodeData: String) {
        // Evitar processar o mesmo QR Code múltiplas vezes
        if (_state.value.lastScannedQrCode == qrCodeData || _state.value.isProcessing) {
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isProcessing = true,
                errorMessage = null,
                successMessage = null,
                lastScannedQrCode = qrCodeData
            )
            
            // Validar se é um QR Code de cupom fiscal
            if (!importFiscalReceiptByQrCodeUseCase.validateQrCode(qrCodeData)) {
                _state.value = _state.value.copy(
                    isProcessing = false,
                    errorMessage = "QR Code inválido. Por favor, escaneie um QR Code de cupom fiscal (NFC-e ou CF-e SAT)."
                )
                return@launch
            }
            
            // Importar cupom fiscal
            when (val result = importFiscalReceiptByQrCodeUseCase(qrCodeData, autoImportProducts = true)) {
                is ImportFiscalReceiptQrResult.Success -> {
                    val message = buildString {
                        append("Cupom fiscal importado com sucesso!")
                        if (result.importedProductsCount > 0) {
                            append(" ${result.importedProductsCount} produtos foram adicionados ao seu estoque.")
                        }
                    }
                    
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        successMessage = message,
                        lastImportedReceiptId = result.fiscalReceiptId,
                        lastImportedProductsCount = result.importedProductsCount
                    )
                }
                
                is ImportFiscalReceiptQrResult.Error -> {
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        errorMessage = result.message
                    )
                }
                
                is ImportFiscalReceiptQrResult.InvalidQrCode -> {
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        errorMessage = "QR Code inválido ou não reconhecido como cupom fiscal."
                    )
                }
                
                is ImportFiscalReceiptQrResult.UserNotAuthenticated -> {
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        errorMessage = "Usuário não autenticado. Faça login e tente novamente."
                    )
                }
            }
        }
    }
    
    /**
     * Define uma mensagem de erro
     */
    fun setError(message: String) {
        _state.value = _state.value.copy(
            errorMessage = message,
            isProcessing = false
        )
    }
    
    /**
     * Limpa mensagens de erro e sucesso
     */
    fun clearMessages() {
        _state.value = _state.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
    
    /**
     * Reset do estado para permitir novo escaneamento
     */
    fun resetForNewScan() {
        _state.value = _state.value.copy(
            lastScannedQrCode = null,
            errorMessage = null,
            successMessage = null,
            isProcessing = false
        )
    }
}
