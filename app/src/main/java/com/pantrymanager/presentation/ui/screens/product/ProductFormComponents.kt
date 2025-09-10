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
    onUnitChange: (Long) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Código de Barras
        OutlinedTextField(
            value = state.barcode,
            onValueChange = onBarcodeChange,
            label = { Text("Código de Barras (EAN)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = state.barcodeError != null,
            supportingText = state.barcodeError?.let { { Text(it) } },
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
            // Categoria (obrigatório)
            var categoryExpanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.categories.find { it.id == state.categoryId }?.name ?: "",
                    onValueChange = { },
                    label = { Text("Categoria *") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    readOnly = true,
                    isError = state.categoryError != null,
                    supportingText = state.categoryError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    state.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                onCategoryChange(category.id)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
            
            // Unidade (obrigatório)
            var unitExpanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = unitExpanded,
                onExpandedChange = { unitExpanded = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.units.find { it.id == state.unitId }?.let { "${it.abbreviation} - ${it.name}" } ?: "",
                    onValueChange = { },
                    label = { Text("Unidade *") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Scale,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded)
                    },
                    readOnly = true,
                    isError = state.unitError != null,
                    supportingText = state.unitError?.let { { Text(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = unitExpanded,
                    onDismissRequest = { unitExpanded = false }
                ) {
                    state.units.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text("${unit.abbreviation} - ${unit.name}") },
                            onClick = {
                                onUnitChange(unit.id)
                                unitExpanded = false
                            }
                        )
                    }
                }
            }
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            placeholder = { Text("0.00") }
        )
        
        // Observações
        OutlinedTextField(
            value = state.observation,
            onValueChange = onObservationChange,
            label = { Text("Observações") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Notes,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3,
            placeholder = { Text("Informações adicionais sobre o produto...") }
        )
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
            enabled = !state.isLoading && state.name.isNotBlank() && 
                     state.categoryId != null && state.unitId != null
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
