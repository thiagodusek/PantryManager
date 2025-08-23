package com.prantymanager.presentation.ui.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prantymanager.domain.entity.Product
import com.prantymanager.domain.entity.MeasurementUnit as UnitEntity
import com.prantymanager.presentation.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToEditProduct: (Long) -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Delete Dialog
    if (state.showDeleteDialog) {
        DeleteProductDialog(
            product = state.productToDelete,
            onConfirm = viewModel::deleteProduct,
            onDismiss = viewModel::cancelDelete,
            isLoading = state.isLoading
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Gerenciar Produtos") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            },
            actions = {
                IconButton(onClick = onNavigateToAddProduct) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar produto")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                ProductsListSection(
                    products = state.products,
                    categories = state.categories,
                    units = state.units,
                    onEditProduct = { product ->
                        onNavigateToEditProduct(product.id)
                    },
                    onDeleteProduct = viewModel::confirmDeleteProduct,
                    onAddProduct = onNavigateToAddProduct
                )
            }
        }
    }
}

@Composable
fun ProductsListSection(
    products: List<Product>,
    categories: List<com.prantymanager.domain.entity.Category>,
    units: List<UnitEntity>,
    onEditProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onAddProduct: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Header with count and add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Produtos Cadastrados (${products.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (products.isEmpty()) {
            EmptyProductsState(onAddProduct = onAddProduct)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        category = categories.find { it.id == product.categoryId },
                        unit = units.find { it.id == product.unitId },
                        onEdit = { onEditProduct(product) },
                        onDelete = { onDeleteProduct(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProductsState(
    onAddProduct: () -> Unit
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Nenhum produto cadastrado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Comece adicionando seu primeiro produto",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddProduct
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adicionar Produto")
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    category: com.prantymanager.domain.entity.Category?,
    unit: UnitEntity?,
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
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (product.ean != null) {
                    Text(
                        text = "EAN: ${product.ean}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    category?.let { cat ->
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    unit?.let { u ->
                        Text(
                            text = if (category != null) " • ${u.abbreviation}" else u.abbreviation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (!product.description.isNullOrBlank()) {
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar produto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Excluir produto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteProductDialog(
    product: Product?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    if (product == null) return

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Excluir Produto") },
        text = {
            Text("Tem certeza que deseja excluir o produto \"${product.name}\"?\n\nEsta ação não pode ser desfeita.")
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
