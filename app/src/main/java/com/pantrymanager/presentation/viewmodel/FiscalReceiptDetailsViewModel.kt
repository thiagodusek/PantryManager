package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.usecase.fiscalreceipt.GetFiscalReceiptByIdUseCase
import com.pantrymanager.domain.usecase.fiscalreceipt.ImportFiscalReceiptItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiscalReceiptDetailsState(
    val fiscalReceipt: FiscalReceipt? = null,
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class FiscalReceiptDetailsViewModel @Inject constructor(
    private val getFiscalReceiptByIdUseCase: GetFiscalReceiptByIdUseCase,
    private val importFiscalReceiptItemsUseCase: ImportFiscalReceiptItemsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(FiscalReceiptDetailsState())
    val state: StateFlow<FiscalReceiptDetailsState> = _state.asStateFlow()
    
    /**
     * Carrega os detalhes do cupom fiscal
     */
    fun loadFiscalReceipt(fiscalReceiptId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                getFiscalReceiptByIdUseCase(fiscalReceiptId).collect { receipt ->
                    _state.value = _state.value.copy(
                        fiscalReceipt = receipt,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar cupom fiscal: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Processa os itens do cupom fiscal para importar como produtos
     */
    fun processReceiptItems() {
        val fiscalReceipt = _state.value.fiscalReceipt ?: return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isProcessing = true,
                errorMessage = null,
                successMessage = null
            )
            
            try {
                val itemIds = fiscalReceipt.items.map { it.id }
                val result = importFiscalReceiptItemsUseCase(itemIds)
                
                if (result.isSuccess) {
                    val importedCount = result.getOrThrow().size
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        successMessage = "Produtos importados com sucesso: $importedCount itens"
                    )
                    
                    // Recarregar o cupom fiscal para atualizar o status
                    loadFiscalReceipt(fiscalReceipt.id)
                } else {
                    _state.value = _state.value.copy(
                        isProcessing = false,
                        errorMessage = "Erro ao importar produtos: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isProcessing = false,
                    errorMessage = "Erro ao processar itens: ${e.message}"
                )
            }
        }
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
}
