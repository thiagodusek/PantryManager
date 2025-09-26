package com.pantrymanager.presentation.ui.screens.brand

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
import com.pantrymanager.domain.entity.Brand
import com.pantrymanager.presentation.viewmodel.BrandViewModel
import com.pantrymanager.presentation.ui.components.StandardTopAppBarWithMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandManagementScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBrandRegister: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: BrandViewModel = hiltViewModel()
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
        EditBrandDialog(
            name = state.name,
            nameError = state.nameError,
            onNameChanged = viewModel::onNameChanged,
            onConfirm = viewModel::updateBrand,
            onDismiss = viewModel::cancelEdit,
            isLoading = state.isLoading
        )
    }

    // Delete Individual Dialog
    if (state.showDeleteDialog) {
        DeleteBrandDialog(
            brand = state.brandToDelete,
            onConfirm = viewModel::deleteBrand,
            onDismiss = viewModel::cancelDelete,
            isLoading = state.isLoading
        )
    }

    // Delete Multiple Dialog
    if (state.showMultiDeleteDialog) {
        DeleteMultipleBrandsDialog(
            selectedCount = state.selectedBrands.size,
            onConfirm = viewModel::deleteSelectedBrands,
            onDismiss = viewModel::cancelMultipleDelete,
            isLoading = state.isLoading
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (state.isSelectionMode) {
                TopAppBar(
                    title = { Text("Gerenciar Marcas") },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.clearSelection()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                        }
                    },
                    actions = {
                        // Selection mode actions
                        Text(
                            text = "${state.selectedBrands.size}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = viewModel::selectAllBrands) {
                            Icon(Icons.Default.SelectAll, contentDescription = "Selecionar todas")
                        }
                        IconButton(
                            onClick = { viewModel.confirmMultipleDelete() },
                            enabled = state.selectedBrands.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir selecionadas",
                                tint = if (state.selectedBrands.isNotEmpty())
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            } else {
                StandardTopAppBarWithMenu(
                    title = "Gerenciar Marcas",
                    onMenuClick = onOpenDrawer
                )
            }
        },
        floatingActionButton = {
            if (!state.isSelectionMode) {
                FloatingActionButton(
                    onClick = { onNavigateToBrandRegister() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar marca")
                }
            }
        }
    ) { paddingValues ->
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Brands Grid Section
            BrandsGridSection(
                brands = state.brands,
                selectedBrands = state.selectedBrands,
                isSelectionMode = state.isSelectionMode,
                onBrandClick = { brand ->
                    if (state.isSelectionMode) {
                        viewModel.toggleBrandSelection(brand.id)
                    } else {
                        // Long press to start selection mode
                        viewModel.toggleBrandSelection(brand.id)
                    }
                },
                onBrandLongClick = { brand ->
                    viewModel.toggleBrandSelection(brand.id)
                }
            )
        }

        // Delete confirmation dialog
        if (state.showMultiDeleteDialog) {
            DeleteMultipleBrandsDialog(
                selectedCount = state.selectedBrands.size,
                onConfirm = viewModel::deleteSelectedBrands,
                onDismiss = viewModel::cancelMultipleDelete,
                isLoading = state.isLoading
            )
        }
    }
}

@Composable
fun BrandsGridSection(
    brands: List<Brand>,
    selectedBrands: Set<Long>,
    isSelectionMode: Boolean,
    onBrandClick: (Brand) -> Unit,
    onBrandLongClick: (Brand) -> Unit
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
                    text = "Marcas Disponíveis (${brands.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (isSelectionMode) {
                    Text(
                        text = "${selectedBrands.size} selecionada(s)",
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
                items(brands) { brand ->
                    BrandGridItem(
                        brand = brand,
                        isSelected = selectedBrands.contains(brand.id),
                        isSelectionMode = isSelectionMode,
                        onClick = { onBrandClick(brand) },
                        onLongClick = { onBrandLongClick(brand) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandGridItem(
    brand: Brand,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
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
                // Brand icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Brand name
                Text(
                    text = brand.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EditBrandDialog(
    name: String,
    nameError: String?,
    onNameChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Editar Marca") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    label = { Text("Nome da Marca") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
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
fun DeleteMultipleBrandsDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Marcas") },
        text = {
            Text("Tem certeza que deseja excluir $selectedCount marca(s) selecionada(s)?\n\nEsta ação não pode ser desfeita.")
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
fun DeleteBrandDialog(
    brand: Brand?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    if (brand == null) return
    
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Marca") },
        text = {
            Text("Tem certeza que deseja excluir a marca \"${brand.name}\"?\n\nEsta ação não pode ser desfeita.")
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
