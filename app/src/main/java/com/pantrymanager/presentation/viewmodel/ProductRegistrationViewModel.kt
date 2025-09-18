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
import com.pantrymanager.domain.repository.ProductRepository
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

data class BatchData(
    val batchNumber: String,
    val quantity: String,
    val expiryDate: String,
    val purchaseDate: String,
    val purchasePrice: String,
    val purchaseLocation: String
)

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
    
    // Campos do lote atual (sendo editado)
    val batchNumber: String = "",
    val quantity: String = "",
    val expiryDate: String = "",
    val purchaseDate: String = "",
    val purchasePrice: String = "",
    val purchaseLocation: String = "",
    
    // Lista de lotes para o produto
    val batches: List<BatchData> = emptyList(),
    val editingBatchIndex: Int = -1, // -1 = novo lote, >= 0 = editando lote existente
    
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
    val purchaseDateError: String? = null,
    
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
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val productRepository: ProductRepository
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
                
                // Carregar categorias e unidades do Firebase (combinando com dados padrão)
                val categories = try {
                    val firebaseCategories = getAllCategoriesUseCase()
                    // Combinar categorias padrão com as do Firebase
                    val allCategories = mutableListOf<Category>()
                    
                    // Adicionar categorias padrão primeiro
                    allCategories.addAll(DefaultCategories.defaultCategories)
                    
                    // Adicionar categorias do Firebase que não existem nas padrão
                    firebaseCategories.forEach { firebaseCategory ->
                        val existsInDefault = DefaultCategories.defaultCategories.any { 
                            it.name.equals(firebaseCategory.name, ignoreCase = true) 
                        }
                        if (!existsInDefault) {
                            allCategories.add(firebaseCategory)
                        }
                    }
                    
                    allCategories
                } catch (e: Exception) {
                    DefaultCategories.defaultCategories
                }
                
                val units = try {
                    val firebaseUnits = getAllMeasurementUnitsUseCase()
                    // Combinar unidades padrão com as do Firebase
                    val allUnits = mutableListOf<MeasurementUnit>()
                    
                    // Adicionar unidades padrão primeiro
                    allUnits.addAll(DefaultMeasurementUnits.defaultUnits)
                    
                    // Adicionar unidades do Firebase que não existem nas padrão
                    firebaseUnits.forEach { firebaseUnit ->
                        val existsInDefault = DefaultMeasurementUnits.defaultUnits.any { 
                            it.name.equals(firebaseUnit.name, ignoreCase = true) ||
                            it.abbreviation.equals(firebaseUnit.abbreviation, ignoreCase = true)
                        }
                        if (!existsInDefault) {
                            allUnits.add(firebaseUnit)
                        }
                    }
                    
                    allUnits
                } catch (e: Exception) {
                    DefaultMeasurementUnits.defaultUnits
                }
                
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
                                    when (searchResult.source) {
                                        "openai" -> append("🤖 Produto encontrado via ChatGPT! Campos preenchidos automaticamente.")
                                        "openai_fallback" -> append("🤖 Produto encontrado via ChatGPT (dados parciais). Campos preenchidos automaticamente.")
                                        else -> append("Produto encontrado! Campos preenchidos automaticamente.")
                                    }
                                    if (!searchResult.category.isNullOrBlank()) {
                                        append("\n• Categoria: ${searchResult.category}")
                                    }
                                    if (!searchResult.unit.isNullOrBlank()) {
                                        append("\n• Unidade: ${searchResult.unit}")
                                    }
                                    if (!searchResult.brand.isNullOrBlank()) {
                                        append("\n• Marca: ${searchResult.brand}")
                                    }
                                    if (searchResult.averagePrice > 0) {
                                        append("\n• Preço médio: R$ ${String.format("%.2f", searchResult.averagePrice)}")
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
        
        // Validar EAN único em tempo real (com debounce)
        validateEanUniqueness(barcode)
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
        viewModelScope.launch {
            // Primeiro fazer a validação assíncrona
            if (!validateFields()) return@launch
            
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
                
                // 5. Criar todos os lotes do produto
                for (batchData in currentState.batches) {
                    val expiryDate = LocalDate.parse(batchData.expiryDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    val purchaseDate = if (batchData.purchaseDate.isNotBlank()) {
                        LocalDate.parse(batchData.purchaseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } else null
                    
                    val batch = ProductBatch(
                        id = 0L,
                        productId = productId,
                        userId = userId,
                        batchNumber = batchData.batchNumber,
                        quantity = batchData.quantity.toDouble(),
                        expiryDate = expiryDate,
                        purchaseDate = purchaseDate,
                        purchasePrice = batchData.purchasePrice.toDoubleOrNull(),
                        purchaseLocation = batchData.purchaseLocation.takeIf { it.isNotBlank() }
                    )
                    
                    val batchResult = addProductBatchUseCase(batch)
                    if (batchResult.isFailure) {
                        throw batchResult.exceptionOrNull() ?: Exception("Erro ao cadastrar lote do produto")
                    }
                }
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = buildString {
                        append("Produto cadastrado com sucesso!")
                        append("\n• ${currentState.batches.size} lote(s) adicionado(s)")
                        currentState.productSearchResult?.let { result ->
                            if (!result.category.isNullOrBlank() || !result.unit.isNullOrBlank() || !result.brand.isNullOrBlank()) {
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
    private suspend fun validateFields(): Boolean {
        val currentState = _state.value
        var hasErrors = false
        
        val nameError = when {
            currentState.name.isBlank() -> "Nome é obrigatório"
            currentState.name.length < 2 -> "Nome deve ter pelo menos 2 caracteres"
            else -> null
        }
        
        val categoryError = if (currentState.categoryId == null) "Categoria é obrigatória" else null
        val unitError = if (currentState.unitId == null) "Unidade é obrigatória" else null
        
        // Validação de EAN único (se fornecido)
        var barcodeError: String? = null
        if (currentState.barcode.isNotBlank()) {
            try {
                // Usar o método do ProductFirebaseRepository para verificar EAN único
                val productFirebaseRepo = productRepository as? com.pantrymanager.data.repository.ProductFirebaseRepository
                val eanExists = productFirebaseRepo?.isEanAlreadyExists(currentState.barcode) ?: false
                if (eanExists) {
                    barcodeError = "Já existe um produto cadastrado com este código EAN"
                }
            } catch (e: Exception) {
                barcodeError = "Erro ao validar código EAN: ${e.message}"
            }
        }
        
        // Validação de lotes - pelo menos um lote deve estar cadastrado
        val batchError = if (currentState.batches.isEmpty()) {
            "Pelo menos um lote deve ser cadastrado"
        } else null
        
        if (nameError != null || categoryError != null || unitError != null || barcodeError != null || batchError != null) {
            hasErrors = true
        }
        
        _state.value = _state.value.copy(
            nameError = nameError,
            categoryError = categoryError,
            unitError = unitError,
            barcodeError = barcodeError,
            errorMessage = batchError
        )
        
        return !hasErrors
    }
    
    // Utility Methods
    private fun findCategoryByName(categoryName: String?): Long? {
        if (categoryName == null) return null
        // Buscar primeiro nas categorias carregadas (padrão + personalizadas)
        return _state.value.categories.find { category ->
            category.name.equals(categoryName, ignoreCase = true) ||
            categoryName.contains(category.name, ignoreCase = true) ||
            category.name.contains(categoryName, ignoreCase = true)
        }?.id
    }
    
    private fun findUnitByName(unitName: String?): Long? {
        if (unitName == null) return null
        // Buscar primeiro nas unidades carregadas (padrão + personalizadas)
        return _state.value.units.find { unit ->
            unit.name.equals(unitName, ignoreCase = true) ||
            unit.abbreviation.equals(unitName, ignoreCase = true) ||
            unitName.contains(unit.name, ignoreCase = true) ||
            unit.name.contains(unitName, ignoreCase = true)
        }?.id
    }
    
    // Batch Management Methods
    fun addBatch() {
        val currentState = _state.value
        
        // Validar campos obrigatórios do lote
        if (currentState.quantity.isBlank()) {
            _state.value = _state.value.copy(quantityError = "Quantidade é obrigatória")
            return
        }
        
        if (currentState.expiryDate.isBlank()) {
            _state.value = _state.value.copy(expiryDateError = "Data de validade é obrigatória")
            return
        }
        
        // Validar formato da data de validade
        try {
            LocalDate.parse(currentState.expiryDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            _state.value = _state.value.copy(expiryDateError = "Formato de data inválido (dd/MM/yyyy)")
            return
        }
        
        // Validar data de compra se informada
        if (currentState.purchaseDate.isNotBlank()) {
            try {
                LocalDate.parse(currentState.purchaseDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(purchaseDateError = "Formato de data inválido (dd/MM/yyyy)")
                return
            }
        }
        
        val newBatch = BatchData(
            batchNumber = currentState.batchNumber.takeIf { it.isNotBlank() } ?: generateDefaultBatchNumber(),
            quantity = currentState.quantity,
            expiryDate = currentState.expiryDate,
            purchaseDate = currentState.purchaseDate,
            purchasePrice = currentState.purchasePrice,
            purchaseLocation = currentState.purchaseLocation
        )
        
        val updatedBatches = if (currentState.editingBatchIndex >= 0) {
            // Editando lote existente
            currentState.batches.toMutableList().apply {
                set(currentState.editingBatchIndex, newBatch)
            }
        } else {
            // Adicionando novo lote
            currentState.batches + newBatch
        }
        
        // Limpar formulário do lote e atualizar lista
        _state.value = _state.value.copy(
            batches = updatedBatches,
            editingBatchIndex = -1,
            batchNumber = "",
            quantity = "",
            expiryDate = "",
            purchaseDate = "",
            purchasePrice = "",
            purchaseLocation = "",
            quantityError = null,
            expiryDateError = null,
            purchaseDateError = null
        )
    }
    
    fun editBatch(index: Int) {
        val batch = _state.value.batches.getOrNull(index) ?: return
        
        _state.value = _state.value.copy(
            editingBatchIndex = index,
            batchNumber = batch.batchNumber,
            quantity = batch.quantity,
            expiryDate = batch.expiryDate,
            purchaseDate = batch.purchaseDate,
            purchasePrice = batch.purchasePrice,
            purchaseLocation = batch.purchaseLocation
        )
    }
    
    fun removeBatch(index: Int) {
        val updatedBatches = _state.value.batches.toMutableList().apply {
            removeAt(index)
        }
        
        _state.value = _state.value.copy(
            batches = updatedBatches,
            editingBatchIndex = if (_state.value.editingBatchIndex == index) -1 else _state.value.editingBatchIndex
        )
    }
    
    fun cancelBatchEdit() {
        _state.value = _state.value.copy(
            editingBatchIndex = -1,
            batchNumber = "",
            quantity = "",
            expiryDate = "",
            purchaseDate = "",
            purchasePrice = "",
            purchaseLocation = "",
            quantityError = null,
            expiryDateError = null,
            purchaseDateError = null
        )
    }
    
    private fun generateDefaultBatchNumber(): String {
        val now = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val batchCount = _state.value.batches.size + 1
        return "L${now.format(formatter)}-${batchCount.toString().padStart(3, '0')}"
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
            batches = emptyList(),
            editingBatchIndex = -1,
            
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
    
    // QR Code and automatic search functions
    fun setEanFromScanner(ean: String) {
        _state.value = _state.value.copy(
            barcode = ean,
            barcodeError = null
        )
        // Automatically search for product information
        searchProductByEAN(ean)
    }

    fun searchProductByEAN(eanCode: String) {
        if (eanCode.isBlank()) return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                // Simulate product search (in real app, this would call an external API or Copilot)
                kotlinx.coroutines.delay(2000)
                
                val productInfo = searchProductInformation(eanCode)
                
                if (productInfo != null) {
                    // Auto-fill form with found information
                    _state.value = _state.value.copy(
                        isLoading = false,
                        name = productInfo.name,
                        description = productInfo.description,
                        brand = productInfo.brand,
                        categoryId = findCategoryId(productInfo.category),
                        unitId = findUnitId(productInfo.unit),
                        averagePrice = productInfo.averagePrice.toString(),
                        errorMessage = "Produto encontrado! Dados preenchidos automaticamente. Verifique as informações e preencha os campos obrigatórios de lote."
                    )
                    
                    // Automatically create/find category and unit if needed
                    createMissingEntities(productInfo)
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Produto não encontrado na base de dados. Preencha as informações manualmente."
                    )
                }
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao buscar informações do produto: ${e.message}"
                )
            }
        }
    }

    private data class ProductInfo(
        val name: String,
        val description: String,
        val brand: String,
        val category: String,
        val unit: String,
        val averagePrice: Double
    )

    private suspend fun searchProductInformation(eanCode: String): ProductInfo? {
        // Simulate different products based on EAN patterns
        // In a real app, this would call Copilot or external product database APIs
        return when {
            eanCode.startsWith("789123") -> ProductInfo(
                name = "Arroz Branco Tipo 1 5kg",
                description = "Arroz branco polido tipo 1, grão longo, pacote de 5kg",
                brand = "Tio João",
                category = "Grãos e Cereais",
                unit = "Quilograma",
                averagePrice = 25.90
            )
            eanCode.startsWith("789124") -> ProductInfo(
                name = "Leite Integral UHT 1L",
                description = "Leite integral longa vida, tetra pak 1 litro",
                brand = "Nestlé",
                category = "Laticínios",
                unit = "Litro",
                averagePrice = 4.50
            )
            eanCode.startsWith("789125") -> ProductInfo(
                name = "Sabonete Líquido Antibacteriano",
                description = "Sabonete líquido para mãos com ação antibacteriana",
                brand = "Protex",
                category = "Higiene e Limpeza",
                unit = "Unidade",
                averagePrice = 8.90
            )
            eanCode.startsWith("789126") -> ProductInfo(
                name = "Óleo de Soja 900ml",
                description = "Óleo de soja refinado, garrafa de 900ml",
                brand = "Liza",
                category = "Óleos e Azeites",
                unit = "Mililitro",
                averagePrice = 7.80
            )
            eanCode.startsWith("789127") -> ProductInfo(
                name = "Feijão Carioca Tipo 1",
                description = "Feijão carioca tipo 1, pacote de 1kg",
                brand = "Camil",
                category = "Grãos e Cereais",
                unit = "Quilograma",
                averagePrice = 8.50
            )
            eanCode.startsWith("789128") -> ProductInfo(
                name = "Açúcar Cristal 1kg",
                description = "Açúcar cristal refinado especial, pacote de 1kg",
                brand = "União",
                category = "Açúcares e Adoçantes",
                unit = "Quilograma",
                averagePrice = 4.20
            )
            eanCode.startsWith("789129") -> ProductInfo(
                name = "Café Torrado e Moído 500g",
                description = "Café torrado e moído tradicional, pacote à vácuo 500g",
                brand = "Pilão",
                category = "Bebidas",
                unit = "Grama",
                averagePrice = 12.90
            )
            eanCode.contains("123456") -> ProductInfo(
                name = "Produto Genérico ${eanCode.takeLast(4)}",
                description = "Produto identificado pelo código de barras",
                brand = "Marca Genérica",
                category = "Outros",
                unit = "Unidade",
                averagePrice = 10.00
            )
            else -> null // Product not found
        }
    }

    private fun findCategoryId(categoryName: String): Long? {
        return DefaultCategories.defaultCategories.find { 
            it.name.equals(categoryName, ignoreCase = true) 
        }?.id
    }

    private fun findUnitId(unitName: String): Long? {
        return DefaultMeasurementUnits.defaultUnits.find { 
            it.name.equals(unitName, ignoreCase = true) ||
            it.abbreviation.equals(unitName, ignoreCase = true)
        }?.id
    }

    private suspend fun createMissingEntities(productInfo: ProductInfo) {
        try {
            // Create category if not found
            if (findCategoryId(productInfo.category) == null) {
                // In real app, create new category
                // val newCategory = findOrCreateCategoryUseCase(productInfo.category)
            }
            
            // Create unit if not found
            if (findUnitId(productInfo.unit) == null) {
                // In real app, create new measurement unit
                // val newUnit = findOrCreateMeasurementUnitUseCase(productInfo.unit)
            }
            
            // Create brand if not exists
            // val brand = findOrCreateBrandUseCase(productInfo.brand)
            
        } catch (e: Exception) {
            // Handle entity creation errors
        }
    }

    fun triggerProductSearch() {
        if (_state.value.barcode.isNotBlank()) {
            searchProductByEAN(_state.value.barcode)
        }
    }
    
    /**
     * Valida se o EAN é único para o usuário (validação em tempo real)
     */
    private fun validateEanUniqueness(ean: String) {
        if (ean.isBlank()) {
            _state.value = _state.value.copy(barcodeError = null)
            return
        }
        
        // Debounce para evitar muitas consultas
        viewModelScope.launch {
            kotlinx.coroutines.delay(500) // Aguarda 500ms após a última digitação
            
            try {
                val productFirebaseRepo = productRepository as? com.pantrymanager.data.repository.ProductFirebaseRepository
                val eanExists = productFirebaseRepo?.isEanAlreadyExists(ean) ?: false
                
                // Só atualiza o erro se o EAN ainda for o mesmo (não mudou durante o delay)
                if (_state.value.barcode == ean) {
                    _state.value = _state.value.copy(
                        barcodeError = if (eanExists) "Já existe um produto cadastrado com este código EAN" else null
                    )
                }
            } catch (e: Exception) {
                if (_state.value.barcode == ean) {
                    _state.value = _state.value.copy(
                        barcodeError = "Erro ao validar código EAN"
                    )
                }
            }
        }
    }
    
    // Category and Unit Management
    fun addNewCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                // Verifica se já existe uma categoria com este nome
                val existingCategory = _state.value.categories.find { 
                    it.name.equals(categoryName.trim(), ignoreCase = true) 
                }
                
                if (existingCategory != null) {
                    // Se já existe, apenas seleciona
                    _state.value = _state.value.copy(
                        categoryId = existingCategory.id,
                        isLoading = false,
                        errorMessage = "Categoria já existe - selecionada automaticamente"
                    )
                    return@launch
                }
                
                // Cria nova categoria usando o use case
                val categoryResult = findOrCreateCategoryUseCase(categoryName.trim())
                
                categoryResult.onSuccess { newCategory ->
                    // Atualiza a lista de categorias
                    val updatedCategories = _state.value.categories.toMutableList()
                    updatedCategories.add(newCategory)
                    
                    _state.value = _state.value.copy(
                        categories = updatedCategories,
                        categoryId = newCategory.id,
                        isLoading = false,
                        successMessage = "Categoria '$categoryName' adicionada com sucesso!"
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Erro ao criar categoria: ${error.message}"
                    )
                }
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao adicionar categoria: ${e.message}"
                )
            }
        }
    }
    
    fun addNewUnit(unitName: String, unitAbbreviation: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                // Verifica se já existe uma unidade com este nome ou abreviação
                val existingUnit = _state.value.units.find { 
                    it.name.equals(unitName.trim(), ignoreCase = true) ||
                    it.abbreviation.equals(unitAbbreviation.trim(), ignoreCase = true)
                }
                
                if (existingUnit != null) {
                    // Se já existe, apenas seleciona
                    _state.value = _state.value.copy(
                        unitId = existingUnit.id,
                        isLoading = false,
                        errorMessage = "Unidade já existe - selecionada automaticamente"
                    )
                    return@launch
                }
                
                // Cria nova unidade usando o use case
                val unitResult = findOrCreateMeasurementUnitUseCase(
                    unitName = unitName.trim(),
                    abbreviation = unitAbbreviation.trim().uppercase()
                )
                
                unitResult.onSuccess { newUnit ->
                    // Atualiza a lista de unidades
                    val updatedUnits = _state.value.units.toMutableList()
                    updatedUnits.add(newUnit)
                    
                    _state.value = _state.value.copy(
                        units = updatedUnits,
                        unitId = newUnit.id,
                        isLoading = false,
                        successMessage = "Unidade '${newUnit.abbreviation} - ${newUnit.name}' adicionada com sucesso!"
                    )
                }.onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Erro ao criar unidade: ${error.message}"
                    )
                }
                
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao adicionar unidade: ${e.message}"
                )
            }
        }
    }
}
