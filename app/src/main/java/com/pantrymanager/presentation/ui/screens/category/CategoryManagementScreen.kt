package com.pantrymanager.presentation.ui.screens.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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

    // Sucesso
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) viewModel.clearSuccess()
    }

    // Erros
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    // Diálogo de edição
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

    // Diálogo de exclusão múltipla
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
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    } else {
                        IconButton(onClick = { /* TODO: ação para adicionar categoria */ }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Grid com categorias (padrão + salvas)
            val allCategories = remember(state.categories) {
                DefaultCategories.defaultCategories + state.categories
            }

            CategoriesGridSection(
                categories = allCategories,
                selectedCategories = state.selectedCategories,
                isSelectionMode = state.isSelectionMode,
                onCategoryClick = { category ->
                    if (state.isSelectionMode) {
                        category.id?.let { viewModel.toggleCategorySelection(it) }
                    } else {
                        // Aqui você pode navegar para edição/visualização, se quiser.
                        // No momento, vamos iniciar seleção com long-press apenas.
                    }
                },
                onCategoryLongClick = { category ->
                    category.id?.let { viewModel.toggleCategorySelection(it) }
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
                items(
                    items = categories,
                    key = { cat -> cat.id ?: -cat.name.hashCode().toLong() } // chave estável p/ nulos
                ) { category ->
                    CategoryGridItem(
                        category = category,
                        isSelected = category.id?.let { selectedCategories.contains(it) } ?: false,
                        isSelectionMode = isSelectionMode,
                        onClick = { onCategoryClick(category) },
                        onLongClick = { onCategoryLongClick(category) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryGridItem(
    category: Category,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val fallbackColor = MaterialTheme.colorScheme.primaryContainer
    val backgroundColor = remember(category.color, fallbackColor) {
        val raw = category.color ?: return@remember fallbackColor
        try {
            Color(android.graphics.Color.parseColor(raw))
        } catch (_: Exception) {
            fallbackColor
        }
    }

    // Categorias de sistema: id negativo (se id for nulo, considera -1L => sistema)
    val isDefaultCategory = (category.id ?: -1L) < 0L

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        onClick() // alterna seleção
                    } else {
                        // clique simples fora do modo seleção: pode abrir detalhes/edição (aqui mantido neutro)
                    }
                },
                onLongClick = {
                    onLongClick() // inicia/alternar seleção
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            else
                backgroundColor.copy(alpha = 0.1f)
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
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
                // Ícone/indicador colorido
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

            // Indicador de seleção
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

@Composable
fun EditCategoryDialog(
    name: String,
    nameError: String?,
    onNameChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Editar Categoria") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    label = { Text("Nome da categoria") },
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading && name.isNotBlank() && nameError == null
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
