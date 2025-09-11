package com.pantrymanager.presentation.ui.screens.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.presentation.viewmodel.UnitViewModel
import com.pantrymanager.data.defaults.DefaultMeasurementUnits

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: UnitViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success state
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
        }
    }

    // Handle error messages
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    // Edit Dialog
    if (state.showEditDialog) {
        EditUnitDialog(
            name = state.name,
            abbreviation = state.abbreviation,
            nameError = state.validationErrors.nameError,
            abbreviationError = state.validationErrors.abbreviationError,
            onNameChanged = viewModel::onNameChanged,
            onAbbreviationChanged = viewModel::onAbbreviationChanged,
            onConfirm = viewModel::updateUnit,
            onDismiss = viewModel::cancelEdit,
            isLoading = state.isLoading
        )
    }

    // Delete Multiple Dialog
    if (state.showMultiDeleteDialog) {
        DeleteMultipleUnitsDialog(
            selectedCount = state.selectedUnits.size,
            onConfirm = viewModel::deleteSelectedUnits,
            onDismiss = viewModel::cancelMultipleDelete,
            isLoading = state.isLoading
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Unidades de Medida") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (state.isSelectionMode) {
                            viewModel.clearSelection()
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (state.isSelectionMode) {
                        // Selection mode actions
                        Text(
                            text = "${state.selectedUnits.size}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = viewModel::selectAllUnits) {
                        Icon(Icons.Default.SelectAll, contentDescription = "Selecionar todas")
                    }
                    IconButton(
                        onClick = viewModel::confirmMultipleDelete,
                        enabled = state.selectedUnits.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = "Excluir selecionadas",
                            tint = if (state.selectedUnits.isNotEmpty()) 
                                MaterialTheme.colorScheme.error else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                } else {
                    IconButton(onClick = { /* TODO: Add unit */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar unidade")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            )
        }
    ) { paddingValues ->
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Units Grid Section - Show all units (default + saved) with visual grid
            val allUnits = DefaultMeasurementUnits.defaultUnits + state.units
            
            UnitsGridSection(
                units = allUnits,
                selectedUnits = state.selectedUnits,
                isSelectionMode = state.isSelectionMode,
                onUnitClick = { unit ->
                    if (state.isSelectionMode) {
                        viewModel.toggleUnitSelection(unit.id)
                    } else {
                        // Long press to start selection mode
                        viewModel.toggleUnitSelection(unit.id)
                    }
                },
                onUnitLongClick = { unit ->
                    viewModel.toggleUnitSelection(unit.id)
                }
            )
        }
    }
}

@Composable
fun UnitsGridSection(
    units: List<UnitEntity>,
    selectedUnits: Set<Long>,
    isSelectionMode: Boolean,
    onUnitClick: (UnitEntity) -> Unit,
    onUnitLongClick: (UnitEntity) -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unidades Disponíveis (${units.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (isSelectionMode) {
                    Text(
                        text = "${selectedUnits.size} selecionada(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(units) { unit ->
                    UnitGridItem(
                        unit = unit,
                        isSelected = selectedUnits.contains(unit.id),
                        isSelectionMode = isSelectionMode,
                        onClick = { onUnitClick(unit) },
                        onLongClick = { onUnitLongClick(unit) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitGridItem(
    unit: UnitEntity,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    // Determine if this is a default (system) unit or user-created
    val isDefaultUnit = unit.id < 0L
    val canBeDeleted = !isDefaultUnit
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .toggleable(
                value = isSelected,
                onValueChange = { onClick() }
            )
            .clickable { 
                if (isSelectionMode) onClick() else onLongClick() 
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                2.dp, 
                MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Unit icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Scale,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Unit abbreviation
                Text(
                    text = unit.abbreviation,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                // Unit name
                Text(
                    text = unit.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Indicator for default units
                if (isDefaultUnit) {
                    Text(
                        text = "Sistema",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Selection indicator
            if (isSelectionMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selecionada",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Multiply indicator
            if (unit.multiplyQuantityByPrice) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "×",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteMultipleUnitsDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Unidades") },
        text = {
            Text("Tem certeza que deseja excluir $selectedCount unidade(s) selecionada(s)?\n\nEsta ação não pode ser desfeita.")
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
                    Text("Excluir Todas")
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
fun EditUnitDialog(
    name: String,
    abbreviation: String,
    nameError: String?,
    abbreviationError: String?,
    onNameChanged: (String) -> Unit,
    onAbbreviationChanged: (String) -> Unit,
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
                    label = { Text("Nome da unidade") },
                    isError = nameError != null,
                    enabled = !isLoading,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (nameError != null) {
                    Text(
                        text = nameError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = abbreviation,
                    onValueChange = onAbbreviationChanged,
                    label = { Text("Abreviação") },
                    isError = abbreviationError != null,
                    enabled = !isLoading,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (abbreviationError != null) {
                    Text(
                        text = abbreviationError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading && name.isNotBlank() && abbreviation.isNotBlank() && 
                         nameError == null && abbreviationError == null
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
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
