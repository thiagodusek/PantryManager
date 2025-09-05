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
import com.pantrymanager.presentation.ui.theme.PantryGradients
import com.pantrymanager.presentation.ui.components.CompactVersionFooter

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
        // Gestão de Catálogo
        HomeMenuItem(
            title = stringResource(R.string.register_products),
            icon = Icons.Default.Add,
            description = "Cadastrar novos produtos no sistema",
            onClick = onNavigateToProductRegister,
            gradientColors = PantryGradients.CategoryGradients["fruits"] ?: PantryGradients.PrimaryGradient
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_categories),
            icon = Icons.Default.Category,
            description = "Organizar categorias de produtos",
            onClick = onNavigateToCategoryRegister,
            gradientColors = PantryGradients.CategoryGradients["bakery"] ?: PantryGradients.PrimaryGradient
        ),
        HomeMenuItem(
            title = stringResource(R.string.manage_units),
            icon = Icons.Default.Scale,
            description = "Configurar unidades de medida",
            onClick = onNavigateToUnitRegister,
            gradientColors = PantryGradients.CategoryGradients["pantry"] ?: PantryGradients.PrimaryGradient
        ),
        
        // Gestão da Despensa
        HomeMenuItem(
            title = stringResource(R.string.pantry_items),
            icon = Icons.Default.Inventory,
            description = "Visualizar itens da despensa",
            onClick = {}, // TODO: Implementar navegação
            gradientColors = PantryGradients.CategoryGradients["frozen"] ?: PantryGradients.PrimaryGradient
        ),
        
        // Importação e Compras
        HomeMenuItem(
            title = stringResource(R.string.import_nfe),
            icon = Icons.Default.Receipt,
            description = "Importar compras via NFe",
            onClick = onNavigateToNFeImport,
            gradientColors = PantryGradients.CategoryGradients["cleaning"] ?: PantryGradients.PrimaryGradient
        ),
        HomeMenuItem(
            title = stringResource(R.string.shopping_lists),
            icon = Icons.Default.ShoppingCart,
            description = "Criar e gerenciar listas",
            onClick = onNavigateToShoppingLists,
            gradientColors = PantryGradients.CategoryGradients["beverages"] ?: PantryGradients.PrimaryGradient
        ),
        
        // Planejamento
        HomeMenuItem(
            title = stringResource(R.string.recipes),
            icon = Icons.Default.MenuBook,
            description = "Descobrir receitas incríveis",
            onClick = onNavigateToRecipes,
            gradientColors = PantryGradients.CategoryGradients["meat"] ?: PantryGradients.PrimaryGradient
        ),
        HomeMenuItem(
            title = stringResource(R.string.nearby_stores),
            icon = Icons.Default.Store,
            description = "Encontrar supermercados próximos",
            onClick = onNavigateToNearbyStores,
            gradientColors = PantryGradients.CategoryGradients["dairy"] ?: PantryGradients.PrimaryGradient
        ),
        
        // Análises
        HomeMenuItem(
            title = stringResource(R.string.dashboards),
            icon = Icons.Default.Analytics,
            description = "Acompanhar estatísticas",
            onClick = onNavigateToDashboard,
            gradientColors = PantryGradients.SecondaryGradient
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
                            onClick = menuItem.onClick,
                            gradientColors = menuItem.gradientColors
                        )
                    }
                }
                
                // Compact Version Footer
                CompactVersionFooter()
            }
        }
}