package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import com.pantrymanager.domain.usecase.fiscalreceipt.GetAllFiscalReceiptsUseCase
import com.pantrymanager.domain.usecase.fiscalreceipt.GetUnprocessedFiscalReceiptsUseCase
import com.pantrymanager.domain.usecase.fiscalreceipt.ImportFiscalReceiptItemsUseCase
import com.pantrymanager.domain.usecase.fiscalreceipt.ImportFiscalReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class FiscalReceiptState(
    val fiscalReceipts: List<FiscalReceipt> = emptyList(),
    val unprocessedReceipts: List<FiscalReceipt> = emptyList(),
    val selectedReceipt: FiscalReceipt? = null,
    val selectedItems: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val isImporting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showImportDialog: Boolean = false,
    
    // Dados para importação manual
    val importData: FiscalReceiptImportData = FiscalReceiptImportData()
)

data class FiscalReceiptImportData(
    val storeName: String = "",
    val receiptNumber: String = "",
    val totalAmount: String = "",
    val purchaseDate: String = "",
    val items: List<FiscalReceiptItemImport> = emptyList()
)

data class FiscalReceiptItemImport(
    val name: String = "",
    val ean: String = "",
    val quantity: String = "",
    val unitPrice: String = "",
    val unit: String = "un",
    val category: String = "",
    val brand: String = ""
)

@HiltViewModel
class FiscalReceiptViewModel @Inject constructor(
    private val getAllFiscalReceiptsUseCase: GetAllFiscalReceiptsUseCase,
    private val getUnprocessedFiscalReceiptsUseCase: GetUnprocessedFiscalReceiptsUseCase,
    private val importFiscalReceiptUseCase: ImportFiscalReceiptUseCase,
    private val importFiscalReceiptItemsUseCase: ImportFiscalReceiptItemsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(FiscalReceiptState())
    val state: StateFlow<FiscalReceiptState> = _state.asStateFlow()
    
    init {
        loadFiscalReceipts()
        loadUnprocessedReceipts()
    }
    
    fun loadFiscalReceipts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                getAllFiscalReceiptsUseCase().collect { receipts ->
                    _state.value = _state.value.copy(
                        fiscalReceipts = receipts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar cupons fiscais: ${e.message}"
                )
            }
        }
    }
    
    fun loadUnprocessedReceipts() {
        viewModelScope.launch {
            try {
                getUnprocessedFiscalReceiptsUseCase().collect { receipts ->
                    _state.value = _state.value.copy(
                        unprocessedReceipts = receipts
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Erro ao carregar cupons não processados: ${e.message}"
                )
            }
        }
    }
    
    fun selectReceipt(receipt: FiscalReceipt) {
        _state.value = _state.value.copy(
            selectedReceipt = receipt,
            selectedItems = emptySet()
        )
    }
    
    fun toggleItemSelection(itemId: Long) {
        val currentSelection = _state.value.selectedItems
        val newSelection = if (currentSelection.contains(itemId)) {
            currentSelection - itemId
        } else {
            currentSelection + itemId
        }
        
        _state.value = _state.value.copy(selectedItems = newSelection)
    }
    
    fun selectAllItems() {
        val receipt = _state.value.selectedReceipt
        if (receipt != null) {
            val allItemIds = receipt.items
                .filter { !it.isImported }
                .map { it.id }
                .toSet()
            
            _state.value = _state.value.copy(selectedItems = allItemIds)
        }
    }
    
    fun clearItemSelection() {
        _state.value = _state.value.copy(selectedItems = emptySet())
    }
    
    fun importSelectedItems() {
        val selectedItems = _state.value.selectedItems
        if (selectedItems.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Selecione pelo menos um item para importar"
            )
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isImporting = true,
                errorMessage = null,
                successMessage = null
            )
            
            try {
                val result = importFiscalReceiptItemsUseCase(selectedItems.toList())
                
                if (result.isSuccess) {
                    val importedCount = result.getOrThrow().size
                    _state.value = _state.value.copy(
                        isImporting = false,
                        successMessage = "$importedCount produtos importados com sucesso!",
                        selectedItems = emptySet()
                    )
                    
                    // Recarregar dados
                    loadFiscalReceipts()
                    loadUnprocessedReceipts()
                } else {
                    _state.value = _state.value.copy(
                        isImporting = false,
                        errorMessage = "Erro ao importar itens: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isImporting = false,
                    errorMessage = "Erro ao importar itens: ${e.message}"
                )
            }
        }
    }
    
    fun showImportDialog() {
        _state.value = _state.value.copy(
            showImportDialog = true,
            importData = FiscalReceiptImportData(
                purchaseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            )
        )
    }
    
    fun hideImportDialog() {
        _state.value = _state.value.copy(
            showImportDialog = false,
            importData = FiscalReceiptImportData()
        )
    }
    
    fun updateImportData(data: FiscalReceiptImportData) {
        _state.value = _state.value.copy(importData = data)
    }
    
    fun addImportItem(item: FiscalReceiptItemImport) {
        val currentItems = _state.value.importData.items
        _state.value = _state.value.copy(
            importData = _state.value.importData.copy(
                items = currentItems + item
            )
        )
    }
    
    fun removeImportItem(index: Int) {
        val currentItems = _state.value.importData.items
        if (index in currentItems.indices) {
            _state.value = _state.value.copy(
                importData = _state.value.importData.copy(
                    items = currentItems.toMutableList().apply { removeAt(index) }
                )
            )
        }
    }
    
    fun importManualReceipt() {
        val importData = _state.value.importData
        
        if (importData.storeName.isBlank() || importData.items.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Preencha todos os campos obrigatórios"
            )
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isImporting = true,
                errorMessage = null,
                successMessage = null
            )
            
            try {
                // Converter dados para entidades
                val fiscalReceipt = FiscalReceipt(
                    storeName = importData.storeName,
                    receiptNumber = importData.receiptNumber,
                    totalAmount = importData.totalAmount.toDoubleOrNull() ?: 0.0,
                    purchaseDate = parseDate(importData.purchaseDate),
                    items = importData.items.mapIndexed { index, item ->
                        FiscalReceiptItem(
                            id = index.toLong(),
                            name = item.name,
                            ean = item.ean.takeIf { it.isNotBlank() },
                            quantity = item.quantity.toDoubleOrNull() ?: 0.0,
                            unitPrice = item.unitPrice.toDoubleOrNull() ?: 0.0,
                            totalPrice = (item.quantity.toDoubleOrNull() ?: 0.0) * 
                                       (item.unitPrice.toDoubleOrNull() ?: 0.0),
                            unit = item.unit,
                            category = item.category.takeIf { it.isNotBlank() },
                            brand = item.brand.takeIf { it.isNotBlank() }
                        )
                    }
                )
                
                val result = importFiscalReceiptUseCase(fiscalReceipt)
                
                if (result.isSuccess) {
                    _state.value = _state.value.copy(
                        isImporting = false,
                        successMessage = "Cupom fiscal importado com sucesso!",
                        showImportDialog = false,
                        importData = FiscalReceiptImportData()
                    )
                    
                    // Recarregar dados
                    loadFiscalReceipts()
                    loadUnprocessedReceipts()
                } else {
                    _state.value = _state.value.copy(
                        isImporting = false,
                        errorMessage = "Erro ao importar cupom: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isImporting = false,
                    errorMessage = "Erro ao importar cupom: ${e.message}"
                )
            }
        }
    }
    
    private fun parseDate(dateString: String): LocalDateTime {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            LocalDateTime.of(
                java.time.LocalDate.parse(dateString, formatter),
                java.time.LocalTime.now()
            )
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }
    
    fun clearMessages() {
        _state.value = _state.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}
