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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.R
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.presentation.viewmodel.ProductViewModel
import com.pantrymanager.presentation.ui.components.StandardTopAppBarWithMenu
import com.pantrymanager.presentation.ui.components.BarcodeScannerDialog

@OptIn(ExperimentalMaterial3Api::class)
@androidx.camera.core.ExperimentalGetImage
@Composable
fun ProductRegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddCategory: () -> Unit,
    onNavigateToAddUnit: () -> Unit,
    onOpenDrawer: () -> Unit,
    productIdToEdit: Long? = null,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showBarcodeScanner by remember { mutableStateOf(false) }

    // Load product for editing if productIdToEdit is provided
    LaunchedEffect(productIdToEdit) {
        productIdToEdit?.let { id ->
            if (id > 0) {
                viewModel.loadProductForEdit(id)
            }
        }
    }

    // Handle success state
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
            onNavigateBack()
        }
    }

    // Handle error messages
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // You can show a snackbar or toast here
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        StandardTopAppBarWithMenu(
            title = stringResource(R.string.product_registration_title),
            onMenuClick = onOpenDrawer
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Código de Barras Field (Required) with Scanner
            OutlinedTextField(
                value = state.ean,
                onValueChange = viewModel::onEanChanged,
                label = { Text(stringResource(R.string.ean_code_required)) },
                placeholder = { Text(stringResource(R.string.ean_code_hint)) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.QrCode, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingIcon = {
                    Row {
                        // Botão para abrir scanner
                        IconButton(
                            onClick = { showBarcodeScanner = true }
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Escanear código de barras",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        // Botão para limpar campo
                        if (state.ean.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.onEanChanged("") }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Limpar",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.validationErrors.eanError != null,
                supportingText = state.validationErrors.eanError?.let { error ->
                    { Text(error, color = MaterialTheme.colorScheme.error) }
                }
            )

            // Name Field (Required)
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text(stringResource(R.string.product_name)) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Inventory, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.validationErrors.nameError != null,
                supportingText = state.validationErrors.nameError?.let { error ->
                    { Text(error, color = MaterialTheme.colorScheme.error) }
                }
            )

            // Description Field (Optional)
            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChanged,
                label = { Text(stringResource(R.string.product_description)) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Description, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Category Dropdown (Required)
            CategoryDropdown(
                categories = state.categories,
                selectedCategoryId = state.categoryId,
                onCategorySelected = viewModel::onCategorySelected,
                onAddNewCategory = onNavigateToAddCategory,
                isError = state.validationErrors.categoryError != null,
                errorMessage = state.validationErrors.categoryError
            )

            // Unit Dropdown (Required)
            UnitDropdown(
                units = state.units,
                selectedUnitId = state.unitId,
                onUnitSelected = viewModel::onUnitSelected,
                onAddNewUnit = onNavigateToAddUnit,
                isError = state.validationErrors.unitError != null,
                errorMessage = state.validationErrors.unitError
            )

            // Observation Field (Optional)
            OutlinedTextField(
                value = state.observation,
                onValueChange = viewModel::onObservationChanged,
                label = { Text(stringResource(R.string.product_observation)) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Note, 
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Image Section (Optional)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "Foto do produto",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Adicionar foto do produto",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "(Opcional)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* TODO: Implement image selection */ }
                    ) {
                        Text("Selecionar Imagem")
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.resetForm()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.clear_form))
                }

                Button(
                    onClick = viewModel::addProduct,
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.add_product))
                    }
                }
            }

            // Required fields note
            Text(
                stringResource(R.string.required_fields),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    // Barcode Scanner Dialog
    BarcodeScannerDialog(
        isVisible = showBarcodeScanner,
        onBarcodeDetected = { barcode ->
            viewModel.onEanChanged(barcode)
            showBarcodeScanner = false
        },
        onDismiss = {
            showBarcodeScanner = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit,
    onAddNewCategory: () -> Unit,
    isError: Boolean,
    errorMessage: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.product_category)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = isError
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.id)
                            expanded = false
                        }
                    )
                }
                
                HorizontalDivider()
                
                DropdownMenuItem(
                    text = { 
                        Text(
                            "Adicionar nova categoria",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    onClick = {
                        expanded = false
                        onAddNewCategory()
                    }
                )
            }
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitDropdown(
    units: List<UnitEntity>,
    selectedUnitId: Long?,
    onUnitSelected: (Long) -> Unit,
    onAddNewUnit: () -> Unit,
    isError: Boolean,
    errorMessage: String?
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedUnit = units.find { it.id == selectedUnitId }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedUnit?.let { "${it.name} (${it.abbreviation})" } ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.product_unit)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                isError = isError
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { 
                            Column {
                                Text(unit.name)
                                Text(
                                    unit.abbreviation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onUnitSelected(unit.id)
                            expanded = false
                        }
                    )
                }
                
                HorizontalDivider()
                
                DropdownMenuItem(
                    text = { 
                        Text(
                            "Adicionar nova unidade",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    onClick = {
                        expanded = false
                        onAddNewUnit()
                    }
                )
            }
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
