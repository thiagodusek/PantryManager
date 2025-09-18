package com.pantrymanager.presentation.ui.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pantrymanager.presentation.viewmodel.ProductRegistrationState
import com.pantrymanager.presentation.viewmodel.BatchData
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Alignment
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.entity.MeasurementUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchResultCard(
    state: ProductRegistrationState
) {
    state.productSearchResult?.let { result ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Produto encontrado na base de dados",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "• Nome: ${result.name ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                result.brand?.let { brand ->
                    Text(
                        text = "• Marca: $brand",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                result.category?.let { category ->
                    Text(
                        text = "• Categoria: $category",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                if (result.averagePrice > 0) {
                    Text(
                        text = "• Preço médio: R$ ${"%.2f".format(result.averagePrice)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Text(
                    text = "• Fonte: ${result.source}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormFields(
    state: ProductRegistrationState,
    onBarcodeChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onObservationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategoryChange: (Long) -> Unit,
    onUnitChange: (Long) -> Unit,
    onScanBarcode: () -> Unit,
    onAddNewCategory: (String) -> Unit,
    onAddNewUnit: (String, String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Código de Barras com scanner
        OutlinedTextField(
            value = state.barcode,
            onValueChange = onBarcodeChange,
            label = { Text("Código de Barras") },
            placeholder = { Text("Digite ou clique na câmera para escanear") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onScanBarcode,
                    enabled = !state.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Escanear código de barras",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = state.barcodeError != null,
            supportingText = state.barcodeError?.let { { Text(it) } } ?: {
                Text(
                    text = "Clique na câmera para escanear ou digite manualmente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        // Nome (obrigatório)
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Nome do Produto *") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Inventory,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nameError != null,
            supportingText = state.nameError?.let { { Text(it) } }
        )
        
        // Marca
        OutlinedTextField(
            value = state.brand,
            onValueChange = onBrandChange,
            label = { Text("Marca") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Descrição
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = { Text("Descrição") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )
        
        // Row para Categoria e Unidade
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Categoria (obrigatório) - Dropdown com opção de adicionar nova
            CategoryDropdownWithAdd(
                categories = state.categories,
                selectedCategoryId = state.categoryId,
                onCategorySelected = onCategoryChange,
                onAddNewCategory = onAddNewCategory,
                isError = state.categoryError != null,
                errorMessage = state.categoryError,
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            )
            
            // Unidade (obrigatório) - Dropdown com opção de adicionar nova
            UnitDropdownWithAdd(
                units = state.units,
                selectedUnitId = state.unitId,
                onUnitSelected = onUnitChange,
                onAddNewUnit = onAddNewUnit,
                isError = state.unitError != null,
                errorMessage = state.unitError,
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Preço Médio
        OutlinedTextField(
            value = state.averagePrice,
            onValueChange = onPriceChange,
            label = { Text("Preço Médio (R$)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        
        // Observações
        OutlinedTextField(
            value = state.observation,
            onValueChange = onObservationChange,
            label = { Text("Observações") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Note,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchManagementSection(
    state: ProductRegistrationState,
    onBatchNumberChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onPurchaseDateChange: (String) -> Unit,
    onPurchasePriceChange: (String) -> Unit,
    onPurchaseLocationChange: (String) -> Unit,
    onAddBatch: () -> Unit,
    onEditBatch: (Int) -> Unit,
    onRemoveBatch: (Int) -> Unit,
    onCancelBatchEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Gerenciar Lotes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Batch Form
            BatchForm(
                state = state,
                onBatchNumberChange = onBatchNumberChange,
                onQuantityChange = onQuantityChange,
                onExpiryDateChange = onExpiryDateChange,
                onPurchaseDateChange = onPurchaseDateChange,
                onPurchasePriceChange = onPurchasePriceChange,
                onPurchaseLocationChange = onPurchaseLocationChange,
                onAddBatch = onAddBatch,
                onCancelBatchEdit = onCancelBatchEdit
            )
            
            // Batch List
            if (state.batches.isNotEmpty()) {
                Divider()
                BatchList(
                    batches = state.batches,
                    onEditBatch = onEditBatch,
                    onRemoveBatch = onRemoveBatch
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BatchForm(
    state: ProductRegistrationState,
    onBatchNumberChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onPurchaseDateChange: (String) -> Unit,
    onPurchasePriceChange: (String) -> Unit,
    onPurchaseLocationChange: (String) -> Unit,
    onAddBatch: () -> Unit,
    onCancelBatchEdit: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Número do Lote
        OutlinedTextField(
            value = state.batchNumber,
            onValueChange = onBatchNumberChange,
            label = { Text("Número do Lote") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Tag,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ex: LOTE001, L2024-01") }
        )
        
        // Row: Quantidade e Data de Validade
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Quantidade
            OutlinedTextField(
                value = state.quantity,
                onValueChange = onQuantityChange,
                label = { Text("Quantidade") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Numbers,
                        contentDescription = null
                    )
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.quantityError != null,
                supportingText = state.quantityError?.let { { Text(it) } }
            )
            
            // Data de Validade (obrigatória)
            OutlinedTextField(
                value = state.expiryDate,
                onValueChange = onExpiryDateChange,
                label = { Text("Data Validade *") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null
                    )
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("dd/MM/yyyy") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.expiryDateError != null,
                supportingText = state.expiryDateError?.let { { Text(it) } }
            )
        }
        
        // Row: Data de Compra e Preço de Compra
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Data de Compra
            OutlinedTextField(
                value = state.purchaseDate,
                onValueChange = onPurchaseDateChange,
                label = { Text("Data Compra") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null
                    )
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("dd/MM/yyyy") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            // Preço de Compra
            OutlinedTextField(
                value = state.purchasePrice,
                onValueChange = onPurchasePriceChange,
                label = { Text("Preço Compra (R$)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null
                    )
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
        
        // Local de Compra
        OutlinedTextField(
            value = state.purchaseLocation,
            onValueChange = onPurchaseLocationChange,
            label = { Text("Local de Compra") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ex: Supermercado ABC, Atacadão XYZ") }
        )
        
        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.editingBatchIndex >= 0) {
                // Botão Cancelar
                OutlinedButton(
                    onClick = onCancelBatchEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancelar")
                }
            }
            
            // Botão Adicionar/Atualizar Lote
            Button(
                onClick = onAddBatch,
                modifier = Modifier.weight(2f),
                enabled = state.expiryDate.isNotBlank() && state.quantity.isNotBlank()
            ) {
                Icon(
                    imageVector = if (state.editingBatchIndex >= 0) Icons.Default.Update else Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (state.editingBatchIndex >= 0) "Atualizar Lote" else "Adicionar Lote")
            }
        }
    }
}

@Composable
private fun BatchList(
    batches: List<BatchData>,
    onEditBatch: (Int) -> Unit,
    onRemoveBatch: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Lotes Cadastrados (${batches.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        
        batches.forEachIndexed { index, batch ->
            BatchItem(
                batch = batch,
                index = index,
                onEdit = { onEditBatch(index) },
                onRemove = { onRemoveBatch(index) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BatchItem(
    batch: BatchData,
    index: Int,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Lote e Quantidade
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (batch.batchNumber.isNotBlank()) {
                            Text(
                                text = "Lote: ${batch.batchNumber}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "Qtd: ${batch.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Data de Validade
                    Text(
                        text = "Validade: ${batch.expiryDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    // Dados opcionais
                    if (batch.purchaseDate.isNotBlank()) {
                        Text(
                            text = "Compra: ${batch.purchaseDate}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (batch.purchasePrice.isNotBlank()) {
                        Text(
                            text = "Preço: R$ ${batch.purchasePrice}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    
                    if (batch.purchaseLocation.isNotBlank()) {
                        Text(
                            text = "Local: ${batch.purchaseLocation}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar lote",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover lote",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    state: ProductRegistrationState,
    onSave: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Botão Limpar
        OutlinedButton(
            onClick = onClear,
            modifier = Modifier.weight(1f),
            enabled = !state.isLoading
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Limpar")
        }
        
        // Botão Salvar
        Button(
            onClick = onSave,
            modifier = Modifier.weight(2f),
            enabled = !state.isLoading && 
                     state.name.isNotBlank() && 
                     state.categoryId != null && 
                     state.unitId != null &&
                     state.batches.isNotEmpty() // Require at least one batch
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (state.isLoading) "Salvando..." else "Salvar Produto")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownWithAdd(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit,
    onAddNewCategory: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && enabled },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = categories.find { it.id == selectedCategoryId }?.name ?: "",
            onValueChange = { },
            label = { Text("Categoria *") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            isError = isError,
            supportingText = errorMessage?.let { { Text(it) } },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Opção para adicionar nova categoria
            DropdownMenuItem(
                text = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Adicionar nova categoria",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    expanded = false
                    showAddDialog = true
                }
            )
            
            if (categories.isNotEmpty()) {
                HorizontalDivider()
                
                // Categorias existentes
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    
    // Dialog para adicionar nova categoria
    if (showAddDialog) {
        AddCategoryDialog(
            onConfirm = { categoryName ->
                onAddNewCategory(categoryName)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdownWithAdd(
    units: List<MeasurementUnit>,
    selectedUnitId: Long?,
    onUnitSelected: (Long) -> Unit,
    onAddNewUnit: (String, String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && enabled },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = units.find { it.id == selectedUnitId }?.let { "${it.abbreviation} - ${it.name}" } ?: "",
            onValueChange = { },
            label = { Text("Unidade *") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Scale,
                    contentDescription = null
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            isError = isError,
            supportingText = errorMessage?.let { { Text(it) } },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Opção para adicionar nova unidade
            DropdownMenuItem(
                text = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Adicionar nova unidade",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    expanded = false
                    showAddDialog = true
                }
            )
            
            if (units.isNotEmpty()) {
                HorizontalDivider()
                
                // Unidades existentes
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text("${unit.abbreviation} - ${unit.name}") },
                        onClick = {
                            onUnitSelected(unit.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    
    // Dialog para adicionar nova unidade
    if (showAddDialog) {
        AddUnitDialog(
            onConfirm = { name, abbreviation ->
                onAddNewUnit(name, abbreviation)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun AddCategoryDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Nova Categoria",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Digite o nome da nova categoria:",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { 
                        categoryName = it
                        isError = false
                    },
                    label = { Text("Nome da categoria") },
                    singleLine = true,
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Nome da categoria é obrigatório") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (categoryName.trim().isNotBlank()) {
                        onConfirm(categoryName.trim())
                    } else {
                        isError = true
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddUnitDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var unitName by remember { mutableStateOf("") }
    var unitAbbreviation by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var abbreviationError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Nova Unidade",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Digite os dados da nova unidade de medida:",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = unitName,
                    onValueChange = { 
                        unitName = it
                        nameError = false
                    },
                    label = { Text("Nome da unidade") },
                    placeholder = { Text("Ex: Quilograma, Litro, Unidade") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("Nome da unidade é obrigatório") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = unitAbbreviation,
                    onValueChange = { 
                        // Limita a 5 caracteres e converte para maiúsculo
                        unitAbbreviation = it.take(5).uppercase()
                        abbreviationError = false
                    },
                    label = { Text("Abreviação") },
                    placeholder = { Text("Ex: KG, L, UN") },
                    singleLine = true,
                    isError = abbreviationError,
                    supportingText = if (abbreviationError) {
                        { Text("Abreviação é obrigatória (máx. 5 caracteres)") }
                    } else {
                        { Text("Máximo 5 caracteres") }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val trimmedName = unitName.trim()
                    val trimmedAbbreviation = unitAbbreviation.trim()
                    
                    nameError = trimmedName.isBlank()
                    abbreviationError = trimmedAbbreviation.isBlank()
                    
                    if (!nameError && !abbreviationError) {
                        onConfirm(trimmedName, trimmedAbbreviation)
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
