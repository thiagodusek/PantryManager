package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.data.defaults.DefaultCategories
import com.pantrymanager.data.defaults.DefaultMeasurementUnits
import com.pantrymanager.data.dto.ProductSearchResult
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.entity.MeasurementUnit
import com.pantrymanager.domain.entity.Product
import com.pantrymanager.domain.entity.ProductBatch
import com.pantrymanager.domain.usecase.auth.GetCurrentUserUseCase
import com.pantrymanager.domain.usecase.brand.FindOrCreateBrandUseCase
import com.pantrymanager.domain.usecase.category.FindOrCreateCategoryUseCase
import com.pantrymanager.domain.usecase.category.GetAllCategoriesUseCase
import com.pantrymanager.domain.usecase.product.AddProductUseCase
import com.pantrymanager.domain.usecase.product.SearchProductByBarcodeUseCase
import com.pantrymanager.domain.usecase.productbatch.AddProductBatchUseCase
import com.pantrymanager.domain.usecase.unit.FindOrCreateMeasurementUnitUseCase
import com.pantrymanager.domain.usecase.unit.GetAllMeasurementUnitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ProductRegistrationState(
    // Campos do produto
    val barcode: String = "",
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val categoryId: Long? = null,
    val unitId: Long? = null,
    val observation: String = "",
    val imageUrl: String? = null,
    val averagePrice: String = "",
    
    // Campos do lote (obrigatórios para cada cadastro)
    val batchNumber: String = "",
    val quantity: String = "",
    val expiryDate: String = "",
    val purchaseDate: String = "",
    val purchasePrice: String = "",
    val purchaseLocation: String = "",
    
    // Dados auxiliares
    val categories: List<Category> = emptyList(),
    val units: List<MeasurementUnit> = emptyList(),
    val currentUserId: String? = null,
    
    // Estados da UI
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isScannerVisible: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val productSearchResult: ProductSearchResult? = null,
    
    // Validações
    val nameError: String? = null,
    val categoryError: String? = null,
    val unitError: String? = null,
    val barcodeError: String? = null,
    val batchNumberError: String? = null,
    val quantityError: String? = null,
    val expiryDateError: String? = null,
    
    // Estados específicos do scanner
    val lastScannedCode: String? = null,
    val scannerError: String? = null
)

@HiltViewModel
class ProductRegistrationViewModel @Inject constructor(
    private val searchProductByBarcodeUseCase: SearchProductByBarcodeUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val addProductBatchUseCase: AddProductBatchUseCase,
    private val findOrCreateBrandUseCase: FindOrCreateBrandUseCase,
    private val findOrCreateCategoryUseCase: FindOrCreateCategoryUseCase,
    private val findOrCreateMeasurementUnitUseCase: FindOrCreateMeasurementUnitUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllMeasurementUnitsUseCase: GetAllMeasurementUnitsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(ProductRegistrationState())
    val state: StateFlow<ProductRegistrationState> = _state.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                // Carregar usuário atual
                val currentUser = getCurrentUserUseCase().first()
                
                // Carregar categorias e unidades dos dados padrão
                val categories = DefaultCategories.defaultCategories
                val units = DefaultMeasurementUnits.defaultUnits
                
                _state.value = _state.value.copy(
                    categories = categories,
                    units = units,
                    currentUserId = currentUser?.id ?: "",
                    isLoading = false,
                    // Definir data de compra como hoje por padrão
                    purchaseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar dados iniciais: ${e.message}"
                )
            }
        }
    }
    
    // Scanner Actions
    fun showScanner() {
        _state.value = _state.value.copy(
            isScannerVisible = true,
            scannerError = null
        )
    }
    
    fun hideScanner() {
        _state.value = _state.value.copy(
            isScannerVisible = false
        )
    }
    
    fun onBarcodeScanned(barcode: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isSearching = true,
                    isScannerVisible = false,
                    lastScannedCode = barcode,
                    scannerError = null,
                    barcode = barcode
                )
                
                // Buscar produto por código de barras
                val result = searchProductByBarcodeUseCase(barcode)
                
                result.fold(
                    onSuccess = { searchResult ->
                        if (searchResult.found) {
                            // Produto encontrado, preencher campos e criar categoria/unidade automaticamente
                            var suggestedCategoryId = findCategoryByName(searchResult.category)
                            var suggestedUnitId = findUnitByName(searchResult.unit)
                            
                            // Criar categoria automaticamente se não encontrada
                            if (suggestedCategoryId == null && !searchResult.category.isNullOrBlank()) {
                                viewModelScope.launch {
                                    try {
                                        val categoryResult = findOrCreateCategoryUseCase(searchResult.category)
                                        if (categoryResult.isSuccess) {
                                            suggestedCategoryId = categoryResult.getOrThrow().id
                                        }
                                    } catch (e: Exception) {
                                        // Se falhar, mantém null e deixa o usuário escolher
                                    }
                                }
                            }
                            
                            // Criar unidade de medida automaticamente se não encontrada
                            if (suggestedUnitId == null && !searchResult.unit.isNullOrBlank()) {
                                viewModelScope.launch {
                                    try {
                                        val unitResult = findOrCreateMeasurementUnitUseCase(
                                            searchResult.unit,
                                            searchResult.unitAbbreviation
                                        )
                                        if (unitResult.isSuccess) {
                                            suggestedUnitId = unitResult.getOrThrow().id
                                        }
                                    } catch (e: Exception) {
                                        // Se falhar, mantém null e deixa o usuário escolher
                                    }
                                }
                            }
                            
                            _state.value = _state.value.copy(
                                isSearching = false,
                                productSearchResult = searchResult,
                                name = searchResult.name ?: "",
                                description = searchResult.description ?: "",
                                brand = searchResult.brand ?: "",
                                imageUrl = searchResult.imageUrl,
                                averagePrice = if (searchResult.averagePrice > 0) 
                                    searchResult.averagePrice.toString() else "",
                                categoryId = suggestedCategoryId,
                                unitId = suggestedUnitId,
                                successMessage = buildString {
                                    append("Produto encontrado! Campos preenchidos automaticamente.")
                                    if (!searchResult.category.isNullOrBlank()) {
                                        append("\n• Categoria: ${searchResult.category}")
                                    }
                                    if (!searchResult.unit.isNullOrBlank()) {
                                        append("\n• Unidade: ${searchResult.unit}")
                                    }
                                },
                                // Gerar número de lote padrão
                                batchNumber = generateDefaultBatchNumber(),
                                // Limpar erros
                                nameError = null,
                                barcodeError = null
                            )
                        } else {
                            // Produto não encontrado
                            _state.value = _state.value.copy(
                                isSearching = false,
                                productSearchResult = searchResult,
                                errorMessage = "Produto não encontrado na base de dados. Preencha os dados manualmente."
                            )
                        }
                    },
                    onFailure = { exception ->
                        _state.value = _state.value.copy(
                            isSearching = false,
                            errorMessage = "Erro na pesquisa: ${exception.message}"
                        )
                    }
                )
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSearching = false,
                    scannerError = "Erro ao processar código: ${e.message}"
                )
            }
        }
    }
    
    // Field Updates
    fun updateBarcode(barcode: String) {
        _state.value = _state.value.copy(
            barcode = barcode,
            barcodeError = null
        )
    }
    
    fun updateName(name: String) {
        _state.value = _state.value.copy(
            name = name,
            nameError = if (name.isBlank()) null else null
        )
    }
    
    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }
    
    fun updateBrand(brand: String) {
        _state.value = _state.value.copy(brand = brand)
    }
    
    fun updateObservation(observation: String) {
        _state.value = _state.value.copy(observation = observation)
    }
    
    fun updateAveragePrice(price: String) {
        _state.value = _state.value.copy(averagePrice = price)
    }
    
    // Novos métodos para campos de lote
    fun updateBatchNumber(batchNumber: String) {
        _state.value = _state.value.copy(
            batchNumber = batchNumber,
            batchNumberError = null
        )
    }
    
    fun updateQuantity(quantity: String) {
        _state.value = _state.value.copy(
            quantity = quantity,
            quantityError = null
        )
    }
    
    fun updateExpiryDate(date: String) {
        _state.value = _state.value.copy(
            expiryDate = date,
            expiryDateError = null
        )
    }
    
    fun updatePurchaseDate(date: String) {
        _state.value = _state.value.copy(purchaseDate = date)
    }
    
    fun updatePurchasePrice(price: String) {
        _state.value = _state.value.copy(purchasePrice = price)
    }
    
    fun updatePurchaseLocation(location: String) {
        _state.value = _state.value.copy(purchaseLocation = location)
    }
    
    fun updateCategory(categoryId: Long) {
        _state.value = _state.value.copy(
            categoryId = categoryId,
            categoryError = null
        )
    }
    
    fun updateUnit(unitId: Long) {
        _state.value = _state.value.copy(
            unitId = unitId,
            unitError = null
        )
    }
    
    // Save Product
    fun saveProduct() {
        if (!validateFields()) return
        
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                val currentState = _state.value
                val userId = currentState.currentUserId ?: throw Exception("Usuário não identificado")
                
                // 1. Buscar ou criar marca se informada
                var brandId: Long? = null
                if (currentState.brand.isNotBlank()) {
                    val brandResult = findOrCreateBrandUseCase(currentState.brand)
                    if (brandResult.isSuccess) {
                        brandId = brandResult.getOrThrow().id
                    }
                }
                
                // 2. Buscar ou criar categoria
                var finalCategoryId = currentState.categoryId!!
                if (currentState.productSearchResult != null && currentState.productSearchResult.category?.isNotBlank() == true) {
                    val categoryResult = findOrCreateCategoryUseCase(currentState.productSearchResult.category!!)
                    if (categoryResult.isSuccess) {
                        finalCategoryId = categoryResult.getOrThrow().id
                    }
                }
                
                // 3. Buscar ou criar unidade de medida
                var finalUnitId = currentState.unitId!!
                if (currentState.productSearchResult != null && currentState.productSearchResult.unit?.isNotBlank() == true) {
                    val unitResult = findOrCreateMeasurementUnitUseCase(
                        currentState.productSearchResult.unit!!,
                        currentState.productSearchResult.unitAbbreviation
                    )
                    if (unitResult.isSuccess) {
                        finalUnitId = unitResult.getOrThrow().id
                    }
                }
                
                // 4. Criar o produto
                val product = Product(
                    id = 0L,
                    userId = userId,
                    ean = currentState.barcode.takeIf { it.isNotBlank() },
                    name = currentState.name,
                    description = currentState.description.takeIf { it.isNotBlank() },
                    categoryId = finalCategoryId,
                    unitId = finalUnitId,
                    brandId = brandId,
                    imageUrl = currentState.imageUrl,
                    averagePrice = currentState.averagePrice.toDoubleOrNull() ?: 0.0,
                    observation = currentState.observation.takeIf { it.isNotBlank() }
                )
                
                val productResult = addProductUseCase(product)
                if (productResult.isFailure) {
                    throw productResult.exceptionOrNull() ?: Exception("Erro ao cadastrar produto")
                }
                
                val productId = productResult.getOrThrow()
                
                // 5. Criar o lote do produto (obrigatório)
                val expiryDate = LocalDate.parse(currentState.expiryDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val purchaseDate = if (currentState.purchaseDate.isNotBlank()) {
                    LocalDate.parse(currentState.purchaseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                } else null
                
                val batch = ProductBatch(
                    id = 0L,
                    productId = productId,
                    userId = userId,
                    batchNumber = currentState.batchNumber,
                    quantity = currentState.quantity.toDouble(),
                    expiryDate = expiryDate,
                    purchaseDate = purchaseDate,
                    purchasePrice = currentState.purchasePrice.toDoubleOrNull(),
                    purchaseLocation = currentState.purchaseLocation.takeIf { it.isNotBlank() }
                )
                
                val batchResult = addProductBatchUseCase(batch)
                if (batchResult.isFailure) {
                    throw batchResult.exceptionOrNull() ?: Exception("Erro ao cadastrar lote do produto")
                }
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = buildString {
                        append("Produto e lote cadastrados com sucesso!")
                        currentState.productSearchResult?.let { result ->
                            if (!result.category.isNullOrBlank() || !result.unit.isNullOrBlank()) {
                                append("\n\nCriados automaticamente:")
                            }
                            if (!result.category.isNullOrBlank()) {
                                append("\n• Categoria: ${result.category}")
                            }
                            if (!result.unit.isNullOrBlank()) {
                                append("\n• Unidade: ${result.unit}")
                            }
                            if (!result.brand.isNullOrBlank()) {
                                append("\n• Marca: ${result.brand}")
                            }
                        }
                    },
                    errorMessage = null
                )
                
                // Limpar formulário após sucesso
                clearForm()
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao salvar produto: ${e.message}"
                )
            }
        }
    }
    
    // Validation
    private fun validateFields(): Boolean {
        val currentState = _state.value
        var hasErrors = false
        
        val nameError = when {
            currentState.name.isBlank() -> "Nome é obrigatório"
            currentState.name.length < 2 -> "Nome deve ter pelo menos 2 caracteres"
            else -> null
        }
        
        val categoryError = if (currentState.categoryId == null) "Categoria é obrigatória" else null
        val unitError = if (currentState.unitId == null) "Unidade é obrigatória" else null
        
        // Validações dos campos de lote (obrigatórios)
        val batchNumberError = when {
            currentState.batchNumber.isBlank() -> "Número do lote é obrigatório"
            else -> null
        }
        
        val quantityError = when {
            currentState.quantity.isBlank() -> "Quantidade é obrigatória"
            else -> {
                try {
                    val qty = currentState.quantity.toDouble()
                    if (qty <= 0) "Quantidade deve ser maior que zero" else null
                } catch (e: NumberFormatException) {
                    "Quantidade deve ser um número válido"
                }
            }
        }
        
        val expiryDateError = when {
            currentState.expiryDate.isBlank() -> "Data de validade é obrigatória"
            else -> {
                try {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val date = LocalDate.parse(currentState.expiryDate, formatter)
                    // Pode ser no passado para produtos já comprados
                    null
                } catch (e: Exception) {
                    "Data inválida. Use o formato dd/MM/yyyy"
                }
            }
        }
        
        if (nameError != null || categoryError != null || unitError != null || 
            batchNumberError != null || quantityError != null || expiryDateError != null) {
            hasErrors = true
        }
        
        _state.value = _state.value.copy(
            nameError = nameError,
            categoryError = categoryError,
            unitError = unitError,
            batchNumberError = batchNumberError,
            quantityError = quantityError,
            expiryDateError = expiryDateError
        )
        
        return !hasErrors
    }
    
    // Utility Methods
    private fun findCategoryByName(categoryName: String?): Long? {
        if (categoryName == null) return null
        return DefaultCategories.findByName(categoryName)?.id
    }
    
    private fun findUnitByName(unitName: String?): Long? {
        if (unitName == null) return null
        return DefaultMeasurementUnits.defaultUnits.find { unit ->
            unit.name.equals(unitName, ignoreCase = true) ||
            unit.abbreviation.equals(unitName, ignoreCase = true) ||
            unitName.contains(unit.name, ignoreCase = true) ||
            unit.name.contains(unitName, ignoreCase = true)
        }?.id
    }
    
    private fun generateDefaultBatchNumber(): String {
        val today = LocalDate.now()
        val timestamp = System.currentTimeMillis().toString().takeLast(6)
        return "LOTE${today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}$timestamp"
    }
    
    fun clearForm() {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        
        _state.value = _state.value.copy(
            barcode = "",
            name = "",
            description = "",
            brand = "",
            categoryId = null,
            unitId = null,
            observation = "",
            imageUrl = null,
            averagePrice = "",
            
            // Limpar campos de lote
            batchNumber = "",
            quantity = "",
            expiryDate = "",
            purchaseDate = today, // Manter data de hoje como padrão
            purchasePrice = "",
            purchaseLocation = "",
            
            productSearchResult = null,
            nameError = null,
            categoryError = null,
            unitError = null,
            barcodeError = null,
            batchNumberError = null,
            quantityError = null,
            expiryDateError = null,
            lastScannedCode = null
        )
    }
    
    fun clearMessages() {
        _state.value = _state.value.copy(
            errorMessage = null,
            successMessage = null,
            scannerError = null
        )
    }
}
