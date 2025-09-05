package com.pantrymanager.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pantrymanager.R
import com.pantrymanager.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onNavigateToProductRegister: () -> Unit,
    onNavigateToCategoryRegister: () -> Unit,
    onNavigateToUnitRegister: () -> Unit,
    onNavigateToNFeImport: () -> Unit,
    onNavigateToShoppingLists: () -> Unit,
    onNavigateToRecipes: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToNearbyStores: () -> Unit,
    onNavigateToPromotions: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    
    val menuItems = listOf(
        HomeMenuItem(
            title = stringResource(R.string.register_products),
            icon = Icons.Default.Add,
            description = "Cadastrar novos produtos",
            onClick = onNavigateToProductRegister
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_categories),
            icon = Icons.Default.Category,
            description = "Gerenciar categorias",
            onClick = onNavigateToCategoryRegister
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_units),
            icon = Icons.Default.Scale,
            description = "Gerenciar unidades de medida",
            onClick = onNavigateToUnitRegister
        ),
        HomeMenuItem(
            title = stringResource(R.string.import_nfe),
            icon = Icons.Default.Receipt,
            description = "Importar via NFe",
            onClick = onNavigateToNFeImport
        ),
        HomeMenuItem(
            title = stringResource(R.string.shopping_lists),
            icon = Icons.Default.List,
            description = "Gerenciar listas de compras",
            onClick = onNavigateToShoppingLists
        ),
        HomeMenuItem(
            title = stringResource(R.string.recipes),
            icon = Icons.Default.MenuBook,
            description = "Gerenciar receitas",
            onClick = onNavigateToRecipes
        ),
        HomeMenuItem(
            title = stringResource(R.string.dashboards),
            icon = Icons.Default.Dashboard,
            description = "Visualizar dashboards",
            onClick = onNavigateToDashboard
        ),
        HomeMenuItem(
            title = stringResource(R.string.nearby_stores),
            icon = Icons.Default.Store,
            description = "Encontrar lojas próximas",
            onClick = onNavigateToNearbyStores
        ),
        HomeMenuItem(
            title = stringResource(R.string.promotions),
            icon = Icons.Default.LocalOffer,
            description = "Ver promoções",
            onClick = onNavigateToPromotions
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: Navigate to profile
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
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
            // Welcome message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = stringResource(R.string.user_logged_success),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    currentUser?.let { user ->
                        Text(
                            text = stringResource(R.string.welcome_user, user.nome),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Menu grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menuItems) { menuItem ->
                    MenuItemCard(
                        title = menuItem.title,
                        icon = menuItem.icon,
                        onClick = menuItem.onClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
