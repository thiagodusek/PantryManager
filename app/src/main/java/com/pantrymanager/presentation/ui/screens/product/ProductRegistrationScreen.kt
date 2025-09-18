package com.pantrymanager.presentation.ui.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.presentation.ui.components.BarcodeScannerDialog
import com.pantrymanager.presentation.ui.components.StandardTopAppBarWithMenu
import com.pantrymanager.presentation.viewmodel.ProductRegistrationState
import com.pantrymanager.presentation.viewmodel.ProductRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@androidx.camera.core.ExperimentalGetImage
@Composable
fun ProductRegistrationScreen(
    onNavigateBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: ProductRegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Efeitos para mensagens
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            // Navegar de volta após sucesso
            kotlinx.coroutines.delay(2000)
            onNavigateBack()
        }
    }
    
    Scaffold(
        topBar = {
            StandardTopAppBarWithMenu(
                title = "Cadastrar Produto",
                onMenuClick = onOpenDrawer,
                actions = {
                    // Botão Scanner
                    FilledTonalButton(
                        onClick = viewModel::showScanner,
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scanner")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
        // Conteúdo principal
        ProductRegistrationContent(
            state = state,
            onBarcodeChange = viewModel::updateBarcode,
            onNameChange = viewModel::updateName,
            onDescriptionChange = viewModel::updateDescription,
            onBrandChange = viewModel::updateBrand,
            onObservationChange = viewModel::updateObservation,
            onPriceChange = viewModel::updateAveragePrice,
            onCategoryChange = viewModel::updateCategory,
            onUnitChange = viewModel::updateUnit,
            onBatchNumberChange = viewModel::updateBatchNumber,
            onQuantityChange = viewModel::updateQuantity,
            onExpiryDateChange = viewModel::updateExpiryDate,
            onPurchaseDateChange = viewModel::updatePurchaseDate,
            onPurchasePriceChange = viewModel::updatePurchasePrice,
            onPurchaseLocationChange = viewModel::updatePurchaseLocation,
            onAddBatch = viewModel::addBatch,
            onEditBatch = viewModel::editBatch,
            onRemoveBatch = viewModel::removeBatch,
            onCancelBatchEdit = viewModel::cancelBatchEdit,
            onSave = viewModel::saveProduct,
            onClear = viewModel::clearForm,
            onClearMessages = viewModel::clearMessages,
            onScanBarcode = viewModel::showScanner,
            onAddNewCategory = viewModel::addNewCategory,
            onAddNewUnit = viewModel::addNewUnit
        )
        }
    }
    
    // Scanner Dialog
    BarcodeScannerDialog(
        isVisible = state.isScannerVisible,
        onBarcodeDetected = viewModel::onBarcodeScanned,
        onDismiss = viewModel::hideScanner
    )
}

@Composable
private fun ProductRegistrationContent(
    state: ProductRegistrationState,
    onBarcodeChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onObservationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCategoryChange: (Long) -> Unit,
    onUnitChange: (Long) -> Unit,
    onBatchNumberChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onPurchaseDateChange: (String) -> Unit,
    onPurchasePriceChange: (String) -> Unit,
    onPurchaseLocationChange: (String) -> Unit,
    onAddBatch: () -> Unit,
    onEditBatch: (Int) -> Unit,
    onRemoveBatch: (Int) -> Unit,
    onCancelBatchEdit: () -> Unit,
    onSave: () -> Unit,
    onClear: () -> Unit,
    onClearMessages: () -> Unit,
    onScanBarcode: () -> Unit,
    onAddNewCategory: (String) -> Unit,
    onAddNewUnit: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Messages
        MessageCards(
            state = state,
            onClearMessages = onClearMessages
        )
        
        // Search Result Card
        if (state.productSearchResult?.found == true) {
            ProductSearchResultCard(state = state)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Form Fields
        ProductFormFields(
            state = state,
            onBarcodeChange = onBarcodeChange,
            onNameChange = onNameChange,
            onDescriptionChange = onDescriptionChange,
            onBrandChange = onBrandChange,
            onObservationChange = onObservationChange,
            onPriceChange = onPriceChange,
            onCategoryChange = onCategoryChange,
            onUnitChange = onUnitChange,
            onScanBarcode = onScanBarcode,
            onAddNewCategory = onAddNewCategory,
            onAddNewUnit = onAddNewUnit
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Batch Management Section
        BatchManagementSection(
            state = state,
            onBatchNumberChange = onBatchNumberChange,
            onQuantityChange = onQuantityChange,
            onExpiryDateChange = onExpiryDateChange,
            onPurchaseDateChange = onPurchaseDateChange,
            onPurchasePriceChange = onPurchasePriceChange,
            onPurchaseLocationChange = onPurchaseLocationChange,
            onAddBatch = onAddBatch,
            onEditBatch = onEditBatch,
            onRemoveBatch = onRemoveBatch,
            onCancelBatchEdit = onCancelBatchEdit
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Action Buttons
        ActionButtons(
            state = state,
            onSave = onSave,
            onClear = onClear
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MessageCards(
    state: ProductRegistrationState,
    onClearMessages: () -> Unit
) {
    // Success Message
    state.successMessage?.let { message ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClearMessages) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    
    // Error Message
    state.errorMessage?.let { message ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClearMessages) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    
    // Loading indicator for search
    if (state.isSearching) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Buscando produto por código de barras...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
