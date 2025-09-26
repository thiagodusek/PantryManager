package com.pantrymanager.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import com.pantrymanager.presentation.ui.theme.PantryGradients

data class MenuCategory(
    val title: String,
    val icon: ImageVector,
    val items: List<HomeMenuItem>
)

data class HomeMenuItem(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithMenu(
    navController: NavHostController,
    onNavigateToProductRegister: () -> Unit,
    onNavigateToCategoryRegister: () -> Unit,
    onNavigateToBrandRegister: () -> Unit,
    onNavigateToBrandManagement: () -> Unit,
    onNavigateToUnitRegister: () -> Unit,
    onNavigateToUnitManagement: () -> Unit,
    onNavigateToPantryItems: () -> Unit,
    onNavigateToNFeImport: () -> Unit,
    onNavigateToShoppingLists: () -> Unit,
    onNavigateToRecipes: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToNearbyStores: () -> Unit,
    onNavigateToPromotions: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    
    val menuCategories = listOf(
        MenuCategory(
            title = stringResource(R.string.catalog_management),
            icon = Icons.Default.Inventory,
            items = listOf(
                HomeMenuItem(
                    title = stringResource(R.string.register_products),
                    icon = Icons.Default.Add,
                    description = "Cadastrar novos produtos com EAN, nome, categoria e unidade",
                    onClick = onNavigateToProductRegister
                ),
                HomeMenuItem(
                    title = stringResource(R.string.manage_categories),
                    icon = Icons.Default.Category,
                    description = "Gerenciar categorias de produtos",
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
                )
            )
        ),
        MenuCategory(
            title = stringResource(R.string.pantry_management),
            icon = Icons.Default.Kitchen,
            items = listOf(
                HomeMenuItem(
                    title = stringResource(R.string.pantry_items),
                    icon = Icons.Default.Inventory2,
                    description = "Visualizar e gerenciar itens da sua despensa",
                    onClick = onNavigateToPantryItems
                ),
                HomeMenuItem(
                    title = stringResource(R.string.import_nfe),
                    icon = Icons.Default.Receipt,
                    description = "Importar produtos via Nota Fiscal Eletrônica",
                    onClick = onNavigateToNFeImport
                )
            )
        ),
        MenuCategory(
            title = stringResource(R.string.shopping_planning),
            icon = Icons.Default.ShoppingCart,
            items = listOf(
                HomeMenuItem(
                    title = stringResource(R.string.shopping_lists),
                    icon = Icons.AutoMirrored.Filled.List,
                    description = "Criar e gerenciar suas listas de compras",
                    onClick = onNavigateToShoppingLists
                ),
                HomeMenuItem(
                    title = stringResource(R.string.recipes),
                    icon = Icons.Default.MenuBook,
                    description = "Gerenciar suas receitas favoritas",
                    onClick = onNavigateToRecipes
                ),
                HomeMenuItem(
                    title = stringResource(R.string.nearby_stores),
                    icon = Icons.Default.Store,
                    description = "Encontrar supermercados próximos",
                    onClick = onNavigateToNearbyStores
                ),
                HomeMenuItem(
                    title = stringResource(R.string.promotions),
                    icon = Icons.Default.LocalOffer,
                    description = "Visualizar promoções e ofertas",
                    onClick = onNavigateToPromotions
                )
            )
        ),
        MenuCategory(
            title = stringResource(R.string.analytics_reports),
            icon = Icons.Default.Analytics,
            items = listOf(
                HomeMenuItem(
                    title = stringResource(R.string.dashboards),
                    icon = Icons.Default.Dashboard,
                    description = "Visualizar relatórios e estatísticas",
                    onClick = onNavigateToDashboard
                )
            )
        ),
        MenuCategory(
            title = stringResource(R.string.user_settings),
            icon = Icons.Default.Person,
            items = listOf(
                HomeMenuItem(
                    title = stringResource(R.string.profile),
                    icon = Icons.Default.AccountCircle,
                    description = "Editar informações do perfil",
                    onClick = onNavigateToProfile
                ),
                HomeMenuItem(
                    title = stringResource(R.string.settings),
                    icon = Icons.Default.Settings,
                    description = "Configurações do aplicativo",
                    onClick = onNavigateToSettings
                )
            )
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
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome message
            item {
                WelcomeCard(currentUser?.nome)
            }
            
            // Menu categories
            items(menuCategories) { category ->
                MenuCategoryCard(category = category)
            }
            
            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun WelcomeCard(userName: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Pantry Manager",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            userName?.let { name ->
                Text(
                    text = stringResource(R.string.welcome_user, name),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuCategoryCard(category: MenuCategory) {
    var expanded by remember { mutableStateOf(true) } // Expandido por padrão
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Category Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expanded = !expanded },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Recolher" else "Expandir",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Category Items
            if (expanded) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    category.items.forEach { item ->
                        MenuItemCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItemCard(item: HomeMenuItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = item.onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ir para ${item.title}",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
