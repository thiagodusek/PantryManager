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
import com.pantrymanager.presentation.ui.components.ModernMenuCard
import com.pantrymanager.presentation.ui.components.ModernWelcomeCard
import com.pantrymanager.presentation.ui.components.StandardTopAppBarWithMenu
import com.pantrymanager.presentation.ui.components.CompactVersionFooter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onOpenDrawer: () -> Unit,
    onNavigateToProductRegister: () -> Unit,
    onNavigateToCategoryRegister: () -> Unit,
    onNavigateToBrandRegister: () -> Unit,
    onNavigateToBrandManagement: () -> Unit,
    onNavigateToUnitRegister: () -> Unit,
    onNavigateToUnitManagement: () -> Unit,
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
        // Gestão de Catálogo
        HomeMenuItem(
            title = stringResource(R.string.register_products),
            icon = Icons.Default.Add,
            description = "Cadastrar novos produtos no sistema",
            onClick = onNavigateToProductRegister
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_categories),
            icon = Icons.Default.Category,
            description = "Organizar categorias de produtos",
            onClick = onNavigateToCategoryRegister
        ),
        HomeMenuItem(
            title = "Cadastrar Marcas",
            icon = Icons.Default.Storefront,
            description = "Cadastrar novas marcas de produtos",
            onClick = onNavigateToBrandRegister
        ),
        HomeMenuItem(
            title = "Gerenciar Marcas",
            icon = Icons.Default.ManageHistory,
            description = "Gerenciar marcas existentes",
            onClick = onNavigateToBrandManagement
        ),
        HomeMenuItem(
            title = stringResource(R.string.register_units),
            icon = Icons.Default.Add,
            description = "Cadastrar novas unidades de medida",
            onClick = onNavigateToUnitRegister
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_units),
            icon = Icons.Default.Scale,
            description = "Gerenciar unidades de medida existentes",
            onClick = onNavigateToUnitManagement
        ),
        
        // Gestão da Despensa
        HomeMenuItem(
            title = stringResource(R.string.pantry_items),
            icon = Icons.Default.Inventory,
            description = "Visualizar itens da despensa",
            onClick = {} // TODO: Implementar navegação
        ),
        
        // Importação e Compras
        HomeMenuItem(
            title = stringResource(R.string.import_nfe),
            icon = Icons.Default.Receipt,
            description = "Importar compras via NFe",
            onClick = onNavigateToNFeImport
        ),
        HomeMenuItem(
            title = stringResource(R.string.shopping_lists),
            icon = Icons.Default.ShoppingCart,
            description = "Criar e gerenciar listas",
            onClick = onNavigateToShoppingLists
        ),
        
        // Planejamento
        HomeMenuItem(
            title = stringResource(R.string.recipes),
            icon = Icons.Default.MenuBook,
            description = "Descobrir receitas incríveis",
            onClick = onNavigateToRecipes
        ),
        HomeMenuItem(
            title = stringResource(R.string.nearby_stores),
            icon = Icons.Default.Store,
            description = "Encontrar supermercados próximos",
            onClick = onNavigateToNearbyStores
        ),
        
        // Análises
        HomeMenuItem(
            title = stringResource(R.string.dashboards),
            icon = Icons.Default.Analytics,
            description = "Acompanhar estatísticas",
            onClick = onNavigateToDashboard
        )
    )

    Scaffold(
        topBar = {
            StandardTopAppBarWithMenu(
                title = stringResource(R.string.home_title),
                onMenuClick = onOpenDrawer
            )
        }        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Welcome message usando o novo componente
                currentUser?.let { user ->
                    ModernWelcomeCard(
                        userName = user.nome,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                // Menu grid com novos cards
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(menuItems) { menuItem ->
                        ModernMenuCard(
                            title = menuItem.title,
                            description = menuItem.description,
                            icon = menuItem.icon,
                            onClick = menuItem.onClick
                        )
                    }
                }
                
                // Compact Version Footer
                CompactVersionFooter()
            }
        }
}