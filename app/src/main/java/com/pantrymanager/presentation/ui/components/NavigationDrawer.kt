package com.pantrymanager.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.pantrymanager.R

data class DrawerMenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isSelected: Boolean = false
)

data class DrawerSection(
    val title: String,
    val items: List<DrawerMenuItem>
)

@Composable
fun NavigationDrawerContent(
    currentRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToProductRegister: () -> Unit,
    onNavigateToProductManagement: () -> Unit,
    onNavigateToCategoryRegister: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit,
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
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit,
    userName: String? = null
) {
    val drawerSections = listOf(
        DrawerSection(
            title = stringResource(R.string.catalog_management),
            items = listOf(
                DrawerMenuItem(
                    title = stringResource(R.string.register_products),
                    icon = Icons.Default.Add,
                    onClick = {
                        onNavigateToProductRegister()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "product_register"
                ),
                DrawerMenuItem(
                    title = "Gerenciar Produtos",
                    icon = Icons.Default.ShoppingCart,
                    onClick = {
                        onNavigateToProductManagement()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "product_management"
                ),
                DrawerMenuItem(
                    title = "Cadastrar Categoria",
                    icon = Icons.Default.Category,
                    onClick = {
                        onNavigateToCategoryRegister()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "category_register"
                ),
                DrawerMenuItem(
                    title = "Gerenciar Categorias",
                    icon = Icons.Default.ManageHistory,
                    onClick = {
                        onNavigateToCategoryManagement()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "category_management"
                ),
                DrawerMenuItem(
                    title = "Cadastrar Marca",
                    icon = Icons.Default.Storefront,
                    onClick = {
                        onNavigateToBrandRegister()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "brand_register"
                ),
                DrawerMenuItem(
                    title = "Gerenciar Marcas",
                    icon = Icons.Default.ManageHistory,
                    onClick = {
                        onNavigateToBrandManagement()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "brand_management"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.register_units),
                    icon = Icons.Default.Add,
                    onClick = {
                        onNavigateToUnitRegister()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "unit_register"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.manage_units),
                    icon = Icons.Default.Scale,
                    onClick = {
                        onNavigateToUnitManagement()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "unit_management"
                )
            )
        ),
        DrawerSection(
            title = stringResource(R.string.pantry_management),
            items = listOf(
                DrawerMenuItem(
                    title = stringResource(R.string.pantry_items),
                    icon = Icons.Default.Inventory2,
                    onClick = {
                        onNavigateToPantryItems()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "pantry_items"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.import_nfe),
                    icon = Icons.Default.Receipt,
                    onClick = {
                        onNavigateToNFeImport()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "nfe_import"
                )
            )
        ),
        DrawerSection(
            title = stringResource(R.string.shopping_planning),
            items = listOf(
                DrawerMenuItem(
                    title = stringResource(R.string.shopping_lists),
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = {
                        onNavigateToShoppingLists()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "shopping_lists"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.recipes),
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    onClick = {
                        onNavigateToRecipes()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "recipes"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.nearby_stores),
                    icon = Icons.Default.Store,
                    onClick = {
                        onNavigateToNearbyStores()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "nearby_stores"
                ),
                DrawerMenuItem(
                    title = stringResource(R.string.promotions),
                    icon = Icons.Default.LocalOffer,
                    onClick = {
                        onNavigateToPromotions()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "promotions"
                )
            )
        ),
        DrawerSection(
            title = stringResource(R.string.analytics_reports),
            items = listOf(
                DrawerMenuItem(
                    title = stringResource(R.string.dashboards),
                    icon = Icons.Default.Dashboard,
                    onClick = {
                        onNavigateToDashboard()
                        onCloseDrawer()
                    },
                    isSelected = currentRoute == "dashboard"
                )
            )
        )
    )

    ModalDrawerSheet {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                DrawerHeader(
                    userName = userName,
                    onNavigateToHome = {
                        onNavigateToHome()
                        onCloseDrawer()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sections
            items(drawerSections) { section ->
                DrawerSectionContent(section = section)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // User Settings Section
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = stringResource(R.string.user_settings),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    label = { Text(stringResource(R.string.profile)) },
                    selected = currentRoute == "profile",
                    onClick = {
                        onNavigateToProfile()
                        onCloseDrawer()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.settings)) },
                    selected = currentRoute == "settings",
                    onClick = {
                        onNavigateToSettings()
                        onCloseDrawer()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                    label = { Text("Sair") },
                    selected = false,
                    onClick = {
                        onLogout()
                        onCloseDrawer()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedTextColor = MaterialTheme.colorScheme.error,
                        unselectedIconColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    userName: String?,
    onNavigateToHome: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pantry Manager",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            userName?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun DrawerSectionContent(section: DrawerSection) {
    Text(
        text = section.title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    
    section.items.forEach { item ->
        NavigationDrawerItem(
            icon = { Icon(item.icon, contentDescription = null) },
            label = { Text(item.title) },
            selected = item.isSelected,
            onClick = item.onClick,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
