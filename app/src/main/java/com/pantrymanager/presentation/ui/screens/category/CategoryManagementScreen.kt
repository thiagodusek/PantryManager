package com.pantrymanager.presentation.ui.screens.category

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.domain.entity.Category
import com.pantrymanager.presentation.viewmodel.CategoryViewModel
import com.pantrymanager.data.defaults.DefaultCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
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
        EditCategoryDialog(
            name = state.name,
            nameError = state.nameError,
            onNameChanged = viewModel::onNameChanged,
            onConfirm = viewModel::updateCategory,
            onDismiss = viewModel::cancelEdit,
            isLoading = state.isLoading
        )
    }

    // Delete Multiple Dialog
    if (state.showMultiDeleteDialog) {
        DeleteMultipleCategoriesDialog(
            selectedCount = state.selectedCategories.size,
            onConfirm = viewModel::deleteSelectedCategories,
            onDismiss = viewModel::cancelMultipleDelete,
            isLoading = state.isLoading
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Categorias") },
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
                        text = "${state.selectedCategories.size}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = viewModel::selectAllCategories) {
                        Icon(Icons.Default.SelectAll, contentDescription = "Selecionar todas")
                    }
                    IconButton(
                        onClick = viewModel::confirmMultipleDelete,
                        enabled = state.selectedCategories.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = "Excluir selecionadas",
                            tint = if (state.selectedCategories.isNotEmpty()) 
                                MaterialTheme.colorScheme.error else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                } else {
                    IconButton(onClick = { /* TODO: Add category */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar categoria")
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
            // Categories Grid Section - Show all categories (default + saved) with visual grid
            val allCategories = DefaultCategories.defaultCategories + state.categories
            
            CategoriesGridSection(
                categories = allCategories,
                selectedCategories = state.selectedCategories,
                isSelectionMode = state.isSelectionMode,
                onCategoryClick = { category ->
                    if (state.isSelectionMode) {
                        viewModel.toggleCategorySelection(category.id)
                    } else {
                        // Long press to start selection mode
                        viewModel.toggleCategorySelection(category.id)
                    }
                },
                onCategoryLongClick = { category ->
                    viewModel.toggleCategorySelection(category.id)
                }
            )
        }
    }
}

@Composable
fun CategoriesGridSection(
    categories: List<Category>,
    selectedCategories: Set<Long>,
    isSelectionMode: Boolean,
    onCategoryClick: (Category) -> Unit,
    onCategoryLongClick: (Category) -> Unit
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
                    text = "Categorias Disponíveis (${categories.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (isSelectionMode) {
                    Text(
                        text = "${selectedCategories.size} selecionada(s)",
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
                items(categories) { category ->
                    CategoryGridItem(
                        category = category,
                        isSelected = selectedCategories.contains(category.id),
                        isSelectionMode = isSelectionMode,
                        onClick = { onCategoryClick(category) },
                        onLongClick = { onCategoryLongClick(category) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryGridItem(
    category: Category,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(category.color))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primaryContainer
    }
    
    // Determine if this is a default (system) category or user-created
    val isDefaultCategory = category.id < 0L
    val canBeDeleted = !isDefaultCategory

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
                backgroundColor.copy(alpha = 0.1f)
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
                // Category icon or colored circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(backgroundColor.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = backgroundColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Indicator for default categories
                if (isDefaultCategory) {
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
        }
    }
}

@Composable
fun DeleteMultipleCategoriesDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Categorias") },
        text = {
            Text("Tem certeza que deseja excluir $selectedCount categoria(s) selecionada(s)?\n\nEsta ação não pode ser desfeita.")
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
