package com.pantrymanager.presentation.ui.screens.fiscalreceipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pantrymanager.presentation.ui.theme.PantryColors
import com.pantrymanager.presentation.viewmodel.FiscalReceiptImportData
import com.pantrymanager.presentation.viewmodel.FiscalReceiptItemImport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiscalReceiptImportDialog(
    importData: FiscalReceiptImportData,
    isImporting: Boolean,
    onDismiss: () -> Unit,
    onUpdateData: (FiscalReceiptImportData) -> Unit,
    onAddItem: (FiscalReceiptItemImport) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onImport: () -> Unit
) {
    var showAddItemDialog by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PantryColors.Primary)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Importar Cupom Fiscal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PantryColors.OnPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = PantryColors.OnPrimary
                        )
                    }
                }
                
                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dados do cupom
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PantryColors.Surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Dados do Cupom",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PantryColors.OnSurface
                            )
                            
                            OutlinedTextField(
                                value = importData.storeName,
                                onValueChange = { 
                                    onUpdateData(importData.copy(storeName = it)) 
                                },
                                label = { Text("Nome da Loja *") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PantryColors.Primary,
                                    focusedLabelColor = PantryColors.Primary
                                )
                            )
                            
                            OutlinedTextField(
                                value = importData.receiptNumber,
                                onValueChange = { 
                                    onUpdateData(importData.copy(receiptNumber = it)) 
                                },
                                label = { Text("Número do Cupom") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PantryColors.Primary,
                                    focusedLabelColor = PantryColors.Primary
                                )
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = importData.totalAmount,
                                    onValueChange = { 
                                        onUpdateData(importData.copy(totalAmount = it)) 
                                    },
                                    label = { Text("Valor Total") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    prefix = { Text("R$ ") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PantryColors.Primary,
                                        focusedLabelColor = PantryColors.Primary
                                    )
                                )
                                
                                OutlinedTextField(
                                    value = importData.purchaseDate,
                                    onValueChange = { 
                                        onUpdateData(importData.copy(purchaseDate = it)) 
                                    },
                                    label = { Text("Data da Compra") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("dd/mm/aaaa") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PantryColors.Primary,
                                        focusedLabelColor = PantryColors.Primary
                                    )
                                )
                            }
                        }
                    }
                    
                    // Lista de itens
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PantryColors.Surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Itens do Cupom (${importData.items.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PantryColors.OnSurface
                                )
                                
                                IconButton(
                                    onClick = { showAddItemDialog = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Adicionar Item",
                                        tint = PantryColors.Primary
                                    )
                                }
                            }
                            
                            if (importData.items.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nenhum item adicionado",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PantryColors.OnSurface.copy(alpha = 0.6f)
                                    )
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    importData.items.forEachIndexed { index, item ->
                                        FiscalReceiptImportItemCard(
                                            item = item,
                                            onRemove = { onRemoveItem(index) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Botões de ação
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isImporting
                    ) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = onImport,
                        modifier = Modifier.weight(1f),
                        enabled = !isImporting && importData.storeName.isNotBlank() && importData.items.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PantryColors.Primary
                        )
                    ) {
                        if (isImporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = PantryColors.OnPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Importar")
                    }
                }
            }
        }
    }
    
    // Dialog para adicionar item
    if (showAddItemDialog) {
        AddFiscalReceiptItemDialog(
            onDismiss = { showAddItemDialog = false },
            onAddItem = { item ->
                onAddItem(item)
                showAddItemDialog = false
            }
        )
    }
}

@Composable
fun FiscalReceiptImportItemCard(
    item: FiscalReceiptItemImport,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PantryColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = PantryColors.OnSurface
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (item.ean.isNotBlank()) {
                        Text(
                            text = "EAN: ${item.ean}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PantryColors.Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Text(
                        text = "${item.quantity} ${item.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PantryColors.OnSurface.copy(alpha = 0.7f)
                    )
                    
                    Text(
                        text = "R$ ${item.unitPrice}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PantryColors.OnSurface.copy(alpha = 0.7f)
                    )
                }
                
                if (item.category.isNotBlank() || item.brand.isNotBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (item.category.isNotBlank()) {
                            Text(
                                text = "Cat: ${item.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = PantryColors.OnSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (item.brand.isNotBlank()) {
                            Text(
                                text = "Marca: ${item.brand}",
                                style = MaterialTheme.typography.bodySmall,
                                color = PantryColors.OnSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
            
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover Item",
                    tint = PantryColors.Error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFiscalReceiptItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (FiscalReceiptItemImport) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ean by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("un") }
    var category by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PantryColors.Primary)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Adicionar Item",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PantryColors.OnPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = PantryColors.OnPrimary
                        )
                    }
                }
                
                // Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do Produto *") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PantryColors.Primary,
                            focusedLabelColor = PantryColors.Primary
                        )
                    )
                    
                    OutlinedTextField(
                        value = ean,
                        onValueChange = { ean = it },
                        label = { Text("Código EAN") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PantryColors.Primary,
                            focusedLabelColor = PantryColors.Primary
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantidade *") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PantryColors.Primary,
                                focusedLabelColor = PantryColors.Primary
                            )
                        )
                        
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("Unidade") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PantryColors.Primary,
                                focusedLabelColor = PantryColors.Primary
                            )
                        )
                    }
                    
                    OutlinedTextField(
                        value = unitPrice,
                        onValueChange = { unitPrice = it },
                        label = { Text("Preço Unitário *") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        prefix = { Text("R$ ") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PantryColors.Primary,
                            focusedLabelColor = PantryColors.Primary
                        )
                    )
                    
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Categoria") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PantryColors.Primary,
                            focusedLabelColor = PantryColors.Primary
                        )
                    )
                    
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Marca") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PantryColors.Primary,
                            focusedLabelColor = PantryColors.Primary
                        )
                    )
                }
                
                // Botões
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = {
                            if (name.isNotBlank() && quantity.isNotBlank() && unitPrice.isNotBlank()) {
                                onAddItem(
                                    FiscalReceiptItemImport(
                                        name = name.trim(),
                                        ean = ean.trim(),
                                        quantity = quantity.trim(),
                                        unitPrice = unitPrice.trim(),
                                        unit = unit.trim(),
                                        category = category.trim(),
                                        brand = brand.trim()
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank() && quantity.isNotBlank() && unitPrice.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PantryColors.Primary
                        )
                    ) {
                        Text("Adicionar")
                    }
                }
            }
        }
    }
}
