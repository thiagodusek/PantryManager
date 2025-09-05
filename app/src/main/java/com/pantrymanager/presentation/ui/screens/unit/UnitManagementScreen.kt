package com.pantrymanager.presentation.ui.screens.unit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.presentation.viewmodel.UnitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: UnitViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle success state
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
        }
    }

    // Handle error messages
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            viewModel.clearError()
        }
    }

    // Edit Dialog
    if (state.showEditDialog) {
        EditUnitDialog(
            name = state.name,
            abbreviation = state.abbreviation,
            multiplyQuantityByPrice = state.multiplyQuantityByPrice,
            validationErrors = state.validationErrors,
            onNameChanged = viewModel::onNameChanged,
            onAbbreviationChanged = viewModel::onAbbreviationChanged,
            onMultiplyQuantityByPriceChanged = viewModel::onMultiplyQuantityByPriceChanged,
            onConfirm = viewModel::updateUnit,
            onDismiss = viewModel::cancelEdit,
            isLoading = state.isLoading
        )
    }

    // Delete Dialog
    if (state.showDeleteDialog) {
        DeleteUnitDialog(
            unit = state.unitToDelete,
            onConfirm = viewModel::deleteUnit,
            onDismiss = viewModel::cancelDelete,
            isLoading = state.isLoading
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Gerenciar Unidades de Medida") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Add Unit Section
            AddUnitSection(
                name = state.name,
                abbreviation = state.abbreviation,
                multiplyQuantityByPrice = state.multiplyQuantityByPrice,
                validationErrors = state.validationErrors,
                isLoading = state.isLoading,
                onNameChanged = viewModel::onNameChanged,
                onAbbreviationChanged = viewModel::onAbbreviationChanged,
                onMultiplyQuantityByPriceChanged = viewModel::onMultiplyQuantityByPriceChanged,
                onAddUnit = viewModel::addUnit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Units List Section
            UnitsListSection(
                units = state.units,
                onEditUnit = viewModel::startEditUnit,
                onDeleteUnit = viewModel::confirmDeleteUnit
            )
        }
    }
}

@Composable
fun AddUnitSection(
    name: String,
    abbreviation: String,
    multiplyQuantityByPrice: Boolean,
    validationErrors: com.pantrymanager.presentation.viewmodel.UnitValidationErrors,
    isLoading: Boolean,
    onNameChanged: (String) -> Unit,
    onAbbreviationChanged: (String) -> Unit,
    onMultiplyQuantityByPriceChanged: (Boolean) -> Unit,
    onAddUnit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Adicionar Nova Unidade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = onNameChanged,
                label = { Text("Nome da Unidade") },
                placeholder = { Text("Ex: Quilograma, Litro...") },
                isError = validationErrors.nameError != null,
                supportingText = validationErrors.nameError?.let { { Text(it) } },
                leadingIcon = {
                    Icon(Icons.Default.Scale, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = abbreviation,
                onValueChange = onAbbreviationChanged,
                label = { Text("Abreviação") },
                placeholder = { Text("Ex: kg, l, un...") },
                isError = validationErrors.abbreviationError != null,
                supportingText = validationErrors.abbreviationError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = multiplyQuantityByPrice,
                    onCheckedChange = onMultiplyQuantityByPriceChanged
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Multiplicar quantidade por preço",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddUnit,
                enabled = !isLoading && name.isNotBlank() && abbreviation.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Adicionar")
                }
            }
        }
    }
}

@Composable
fun UnitsListSection(
    units: List<UnitEntity>,
    onEditUnit: (UnitEntity) -> Unit,
    onDeleteUnit: (UnitEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Unidades Cadastradas (${units.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (units.isEmpty()) {
                Text(
                    text = "Nenhuma unidade cadastrada ainda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(units) { unit ->
                        UnitItem(
                            unit = unit,
                            onEdit = { onEditUnit(unit) },
                            onDelete = { onDeleteUnit(unit) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UnitItem(
    unit: UnitEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${unit.name} (${unit.abbreviation})",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row {
                    Text(
                        text = "ID: ${unit.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (unit.multiplyQuantityByPrice) {
                        Text(
                            text = " • Multiplica por preço",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar unidade",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Excluir unidade",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun EditUnitDialog(
    name: String,
    abbreviation: String,
    multiplyQuantityByPrice: Boolean,
    validationErrors: com.pantrymanager.presentation.viewmodel.UnitValidationErrors,
    onNameChanged: (String) -> Unit,
    onAbbreviationChanged: (String) -> Unit,
    onMultiplyQuantityByPriceChanged: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Editar Unidade") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    label = { Text("Nome da Unidade") },
                    isError = validationErrors.nameError != null,
                    supportingText = validationErrors.nameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = abbreviation,
                    onValueChange = onAbbreviationChanged,
                    label = { Text("Abreviação") },
                    isError = validationErrors.abbreviationError != null,
                    supportingText = validationErrors.abbreviationError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = multiplyQuantityByPrice,
                        onCheckedChange = onMultiplyQuantityByPriceChanged,
                        enabled = !isLoading
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Multiplicar quantidade por preço",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading && name.isNotBlank() && abbreviation.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Salvar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeleteUnitDialog(
    unit: UnitEntity?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    if (unit == null) return

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Unidade") },
        text = {
            Text("Tem certeza que deseja excluir a unidade \"${unit.name} (${unit.abbreviation})\"?\n\nEsta ação não pode ser desfeita.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text("Excluir")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    )
}
