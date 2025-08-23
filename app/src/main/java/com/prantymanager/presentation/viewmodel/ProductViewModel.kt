package com.prantymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.entity.Category
import com.prantymanager.domain.entity.MeasurementUnit as UnitEntity
import com.prantymanager.domain.usecase.product.AddProductUseCase
import com.prantymanager.domain.usecase.product.GetAllProductsUseCase
import com.prantymanager.domain.usecase.product.GetProductByIdUseCase
import com.prantymanager.domain.usecase.product.UpdateProductUseCase
import com.prantymanager.domain.usecase.product.DeleteProductUseCase
import com.prantymanager.domain.usecase.category.GetAllCategoriesUseCase
import com.prantymanager.domain.usecase.unit.GetAllUnitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductState(
    val products: List<Product> = emptyList(),
    val currentProduct: Product? = null,
    val ean: String = "",
    val name: String = "",
    val description: String = "",
    val categoryId: Long? = null,
    val unitId: Long? = null,
    val observation: String = "",
    val imageUrl: String? = null,
    val categories: List<Category> = emptyList(),
    val units: List<UnitEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val validationErrors: ProductValidationErrors = ProductValidationErrors(),
    val isEditing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val productToDelete: Product? = null
)

data class ProductValidationErrors(
    val nameError: String? = null,
    val categoryError: String? = null,
    val unitError: String? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllUnitsUseCase: GetAllUnitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    // CRUD Operations
    fun loadAllProducts() {
        /*
        // Código de conexão com banco comentado
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val products = getAllProductsUseCase()
                _state.value = _state.value.copy(
                    products = products,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar produtos: ${e.message}"
                )
            }
        }
        */
        
        // Simulando lista vazia para demonstração
        _state.value = _state.value.copy(
            products = emptyList(),
            isLoading = false
        )
    }

    fun loadProductById(productId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val product = getProductByIdUseCase(productId)
                product?.let {
                    _state.value = _state.value.copy(
                        currentProduct = it,
                        ean = it.ean ?: "",
                        name = it.name,
                        description = it.description ?: "",
                        categoryId = it.categoryId,
                        unitId = it.unitId,
                        observation = it.observation ?: "",
                        imageUrl = it.imageUrl,
                        isEditing = true,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar produto: ${e.message}"
                )
            }
        }
    }

    fun loadProductForEdit(productId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            try {
                val product = getProductByIdUseCase(productId)
                product?.let { p ->
                    _state.value = _state.value.copy(
                        currentProduct = p,
                        ean = p.ean ?: "",
                        name = p.name,
                        description = p.description ?: "",
                        categoryId = p.categoryId,
                        unitId = p.unitId,
                        observation = p.observation ?: "",
                        imageUrl = p.imageUrl,
                        isEditing = true,
                        isLoading = false
                    )
                } ?: run {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Produto não encontrado"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro ao carregar produto"
                )
            }
        }
    }

    fun addProduct() {
        if (!validateProduct()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                /*
                // Código de conexão com banco comentado
                val product = Product(
                    id = 0,
                    ean = _state.value.ean.takeIf { it.isNotBlank() },
                    name = _state.value.name,
                    description = _state.value.description.takeIf { it.isNotBlank() },
                    categoryId = _state.value.categoryId!!,
                    unitId = _state.value.unitId!!,
                    observation = _state.value.observation.takeIf { it.isNotBlank() },
                    imageUrl = _state.value.imageUrl
                )
                
                val result = addProductUseCase(product)
                if (result.isSuccess) {
                    loadAllProducts() // Reload list
                }
                */
                
                // Simulação de delay para mostrar loading
                kotlinx.coroutines.delay(1000)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = "Produto cadastrado com sucesso!"
                )
                clearForm()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro desconhecido"
                )
            }
        }
    }

    fun updateProduct() {
        if (!validateProduct()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                /*
                // Código de conexão com banco comentado
                val product = _state.value.currentProduct?.copy(
                    ean = _state.value.ean.takeIf { it.isNotBlank() },
                    name = _state.value.name,
                    description = _state.value.description.takeIf { it.isNotBlank() },
                    categoryId = _state.value.categoryId!!,
                    unitId = _state.value.unitId!!,
                    observation = _state.value.observation.takeIf { it.isNotBlank() },
                    imageUrl = _state.value.imageUrl
                ) ?: return@launch
                
                val result = updateProductUseCase(product)
                if (result.isSuccess) {
                    loadAllProducts() // Reload list
                }
                */
                
                // Simulação de delay para mostrar loading
                kotlinx.coroutines.delay(1000)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = "Produto atualizado com sucesso!"
                )
                clearForm()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro desconhecido"
                )
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                /*
                // Código de conexão com banco comentado
                val result = deleteProductUseCase(product)
                if (result.isSuccess) {
                    loadAllProducts() // Reload list
                }
                */
                
                // Simulação de delay para mostrar loading
                kotlinx.coroutines.delay(1000)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = "Produto deletado com sucesso!",
                    showDeleteDialog = false,
                    productToDelete = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro desconhecido"
                )
            }
        }
    }

    // UI Event Handlers
    fun onEanChanged(ean: String) {
        _state.value = _state.value.copy(
            ean = ean,
            validationErrors = _state.value.validationErrors.copy()
        )
    }

    fun onNameChanged(name: String) {
        _state.value = _state.value.copy(
            name = name,
            validationErrors = _state.value.validationErrors.copy(nameError = null)
        )
    }

    fun onDescriptionChanged(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    fun onCategorySelected(categoryId: Long) {
        _state.value = _state.value.copy(
            categoryId = categoryId,
            validationErrors = _state.value.validationErrors.copy(categoryError = null)
        )
    }

    fun onUnitSelected(unitId: Long) {
        _state.value = _state.value.copy(
            unitId = unitId,
            validationErrors = _state.value.validationErrors.copy(unitError = null)
        )
    }

    fun onObservationChanged(observation: String) {
        _state.value = _state.value.copy(observation = observation)
    }

    fun onImageSelected(imageUrl: String) {
        _state.value = _state.value.copy(imageUrl = imageUrl)
    }

    fun startEdit(product: Product) {
        _state.value = _state.value.copy(
            currentProduct = product,
            ean = product.ean ?: "",
            name = product.name,
            description = product.description ?: "",
            categoryId = product.categoryId,
            unitId = product.unitId,
            observation = product.observation ?: "",
            imageUrl = product.imageUrl,
            isEditing = true,
            validationErrors = ProductValidationErrors()
        )
    }

    fun cancelEdit() {
        _state.value = _state.value.copy(
            currentProduct = null,
            ean = "",
            name = "",
            description = "",
            categoryId = null,
            unitId = null,
            observation = "",
            imageUrl = null,
            isEditing = false,
            validationErrors = ProductValidationErrors()
        )
    }

    fun showDeleteDialog(product: Product) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            productToDelete = product
        )
    }

    fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            productToDelete = null
        )
    }

    fun confirmDelete() {
        _state.value.productToDelete?.let { product ->
            deleteProduct(product)
        }
    }

    fun confirmDeleteProduct(product: Product) {
        _state.value = _state.value.copy(
            productToDelete = product,
            showDeleteDialog = true
        )
    }

    fun deleteProduct() {
        val productToDelete = _state.value.productToDelete ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            
            try {
                deleteProductUseCase(productToDelete)
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    showDeleteDialog = false,
                    productToDelete = null,
                    errorMessage = null
                )
                loadProducts()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro ao excluir produto"
                )
            }
        }
    }

    fun cancelDelete() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            productToDelete = null
        )
    }

    fun clearSuccess() {
        _state.value = _state.value.copy(isSuccess = false)
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun resetForm() {
        _state.value = _state.value.copy(
            currentProduct = null,
            ean = "",
            name = "",
            description = "",
            categoryId = null,
            unitId = null,
            observation = "",
            imageUrl = null,
            isEditing = false,
            validationErrors = ProductValidationErrors()
        )
    }

    fun clearForm() {
        resetForm()
    }

    fun loadProducts() {
        loadAllProducts()
    }

    private fun validateProduct(): Boolean {
        var nameError: String? = null
        var categoryError: String? = null
        var unitError: String? = null
        var hasErrors = false

        if (_state.value.name.isBlank()) {
            nameError = "Nome é obrigatório"
            hasErrors = true
        }

        if (_state.value.categoryId == null) {
            categoryError = "Categoria é obrigatória"
            hasErrors = true
        }

        if (_state.value.unitId == null) {
            unitError = "Unidade de medida é obrigatória"
            hasErrors = true
        }

        val errors = ProductValidationErrors(
            nameError = nameError,
            categoryError = categoryError,
            unitError = unitError
        )
        
        _state.value = _state.value.copy(validationErrors = errors)
        return !hasErrors
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val categories = getAllCategoriesUseCase()
                val units = getAllUnitsUseCase()
                val products = getAllProductsUseCase()
                _state.value = _state.value.copy(
                    categories = categories,
                    units = units,
                    products = products
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Erro ao carregar dados: ${e.message}"
                )
            }
        }
    }
}
