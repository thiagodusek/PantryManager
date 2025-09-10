package com.pantrymanager.presentation.ui.screens.fiscalreceipt

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantrymanager.domain.entity.FiscalReceipt
import com.pantrymanager.domain.entity.FiscalReceiptItem
import com.pantrymanager.presentation.ui.theme.PantryColors
import com.pantrymanager.presentation.viewmodel.FiscalReceiptState
import com.pantrymanager.presentation.viewmodel.FiscalReceiptViewModel
import com.pantrymanager.presentation.viewmodel.FiscalReceiptImportData
import com.pantrymanager.presentation.viewmodel.FiscalReceiptItemImport
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiscalReceiptScreen(
    onNavigateBack: () -> Unit,
    viewModel: FiscalReceiptViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Mostrar mensagens
    LaunchedEffect(state.errorMessage, state.successMessage) {
        // As mensagens serão mostradas via Snackbar
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryColors.Background)
    ) {
        // TopBar
        TopAppBar(
            title = {
                Text(
                    text = "Cupons Fiscais",
                    color = PantryColors.OnPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = PantryColors.OnPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.showImportDialog() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Importar Cupom",
                        tint = PantryColors.OnPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = PantryColors.Primary
            )
        )
        
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PantryColors.Primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Seção de cupons não processados
                if (state.unprocessedReceipts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Cupons Não Processados",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PantryColors.Error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(state.unprocessedReceipts) { receipt ->
                        FiscalReceiptCard(
                            fiscalReceipt = receipt,
                            isSelected = state.selectedReceipt?.id == receipt.id,
                            onSelect = { viewModel.selectReceipt(receipt) },
                            showUnprocessedBadge = true
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = PantryColors.OnSurface.copy(alpha = 0.12f))
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                // Seção de todos os cupons
                item {
                    Text(
                        text = "Todos os Cupons",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PantryColors.OnBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(state.fiscalReceipts) { receipt ->
                    FiscalReceiptCard(
                        fiscalReceipt = receipt,
                        isSelected = state.selectedReceipt?.id == receipt.id,
                        onSelect = { viewModel.selectReceipt(receipt) }
                    )
                }
            }
        }
        
        // Mostrar detalhes do cupom selecionado
        if (state.selectedReceipt != null) {
            FiscalReceiptDetailsDialog(
                fiscalReceipt = state.selectedReceipt,
                selectedItems = state.selectedItems,
                isImporting = state.isImporting,
                onDismiss = { viewModel.selectReceipt(FiscalReceipt()) },
                onToggleItem = { viewModel.toggleItemSelection(it) },
                onSelectAll = { viewModel.selectAllItems() },
                onClearSelection = { viewModel.clearItemSelection() },
                onImportSelected = { viewModel.importSelectedItems() }
            )
        }
        
        // Dialog de importação manual
        if (state.showImportDialog) {
            FiscalReceiptImportDialog(
                importData = state.importData,
                isImporting = state.isImporting,
                onDismiss = { viewModel.hideImportDialog() },
                onUpdateData = { viewModel.updateImportData(it) },
                onAddItem = { viewModel.addImportItem(it) },
                onRemoveItem = { viewModel.removeImportItem(it) },
                onImport = { viewModel.importManualReceipt() }
            )
        }
    }
    
    // Mostrar mensagens de erro/sucesso
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Aqui você pode usar um SnackbarHost se disponível
            viewModel.clearMessages()
        }
    }
    
    state.successMessage?.let { message ->
        LaunchedEffect(message) {
            // Aqui você pode usar um SnackbarHost se disponível
            viewModel.clearMessages()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiscalReceiptCard(
    fiscalReceipt: FiscalReceipt,
    isSelected: Boolean = false,
    onSelect: () -> Unit,
    showUnprocessedBadge: Boolean = false
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PantryColors.PrimaryContainer else PantryColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fiscalReceipt.storeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PantryColors.OnSurface
                    )
                    if (fiscalReceipt.receiptNumber.isNotBlank()) {
                        Text(
                            text = "Cupom: ${fiscalReceipt.receiptNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PantryColors.OnSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                if (showUnprocessedBadge || !fiscalReceipt.isProcessed) {
                    Badge(
                        containerColor = PantryColors.Warning,
                        contentColor = PantryColors.OnWarning
                    ) {
                        Text("Não Processado")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = numberFormat.format(fiscalReceipt.totalAmount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PantryColors.Primary
                )
                
                Text(
                    text = fiscalReceipt.purchaseDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = PantryColors.OnSurface.copy(alpha = 0.7f)
                )
            }
            
            if (fiscalReceipt.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${fiscalReceipt.items.size} itens",
                    style = MaterialTheme.typography.bodySmall,
                    color = PantryColors.OnSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun FiscalReceiptDetailsDialog(
    fiscalReceipt: FiscalReceipt,
    selectedItems: Set<Long>,
    isImporting: Boolean,
    onDismiss: () -> Unit,
    onToggleItem: (Long) -> Unit,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onImportSelected: () -> Unit
) {
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fiscalReceipt.storeName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PantryColors.OnPrimary
                        )
                        if (fiscalReceipt.receiptNumber.isNotBlank()) {
                            Text(
                                text = "Cupom: ${fiscalReceipt.receiptNumber}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PantryColors.OnPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = PantryColors.OnPrimary
                        )
                    }
                }
                
                // Botões de ação
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val unimportedItems = fiscalReceipt.items.filter { !it.isImported }
                    val selectedCount = selectedItems.size
                    
                    if (unimportedItems.isNotEmpty()) {
                        TextButton(
                            onClick = onSelectAll,
                            enabled = !isImporting
                        ) {
                            Icon(Icons.Default.SelectAll, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Selecionar Todos")
                        }
                        
                        if (selectedItems.isNotEmpty()) {
                            TextButton(
                                onClick = onClearSelection,
                                enabled = !isImporting
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Limpar Seleção")
                            }
                        }
                    }
                }
                
                // Lista de itens
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(fiscalReceipt.items) { item ->
                        FiscalReceiptItemCard(
                            item = item,
                            isSelected = selectedItems.contains(item.id),
                            onToggleSelection = { onToggleItem(item.id) },
                            enabled = !item.isImported && !isImporting
                        )
                    }
                }
                
                // Botão de importar
                if (selectedItems.isNotEmpty()) {
                    Button(
                        onClick = onImportSelected,
                        enabled = !isImporting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                        Text("Importar ${selectedItems.size} itens selecionados")
                    }
                }
            }
        }
    }
}

@Composable
fun FiscalReceiptItemCard(
    item: FiscalReceiptItem,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    enabled: Boolean = true
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onToggleSelection() }
            .then(
                if (isSelected) Modifier.border(
                    2.dp, 
                    PantryColors.Primary, 
                    RoundedCornerShape(8.dp)
                ) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = when {
                item.isImported -> PantryColors.SurfaceVariant.copy(alpha = 0.5f)
                isSelected -> PantryColors.PrimaryContainer
                else -> PantryColors.Surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            if (enabled) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggleSelection() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PantryColors.Primary
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Importado",
                    tint = PantryColors.Success,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Informações do item
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) PantryColors.OnSurface else PantryColors.OnSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (!item.ean.isNullOrBlank()) {
                    Text(
                        text = "EAN: ${item.ean}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PantryColors.Primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${item.quantity} ${item.unit ?: "un"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (enabled) PantryColors.OnSurface.copy(alpha = 0.7f) 
                               else PantryColors.OnSurface.copy(alpha = 0.4f)
                    )
                    
                    Text(
                        text = "×",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (enabled) PantryColors.OnSurface.copy(alpha = 0.5f) 
                               else PantryColors.OnSurface.copy(alpha = 0.3f)
                    )
                    
                    Text(
                        text = numberFormat.format(item.unitPrice),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (enabled) PantryColors.OnSurface.copy(alpha = 0.7f) 
                               else PantryColors.OnSurface.copy(alpha = 0.4f)
                    )
                }
                
                if (item.isImported) {
                    Text(
                        text = "✓ Já importado",
                        style = MaterialTheme.typography.bodySmall,
                        color = PantryColors.Success,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Preço total
            Text(
                text = numberFormat.format(item.totalPrice),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (enabled) PantryColors.Primary else PantryColors.Primary.copy(alpha = 0.6f)
            )
        }
    }
}
