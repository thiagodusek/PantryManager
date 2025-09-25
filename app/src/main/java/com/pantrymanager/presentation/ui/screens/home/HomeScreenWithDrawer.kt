package com.pantrymanager.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pantrymanager.R
import com.pantrymanager.presentation.ui.components.NavigationDrawerContent
import com.pantrymanager.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenWithDrawer(
    navController: NavHostController,
    onNavigateToProductRegister: () -> Unit,
    onNavigateToProductManagement: () -> Unit,
    onNavigateToCategoryRegister: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit,
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser by viewModel.currentUser.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                currentRoute = currentRoute,
                onNavigateToHome = {
                    // Já estamos na tela home, apenas fechamos o drawer
                    scope.launch {
                        drawerState.close()
                    }
                },
                onNavigateToProductRegister = onNavigateToProductRegister,
                onNavigateToProductManagement = onNavigateToProductManagement,
                onNavigateToCategoryRegister = onNavigateToCategoryRegister,
                onNavigateToCategoryManagement = onNavigateToCategoryManagement,
                onNavigateToUnitRegister = onNavigateToUnitRegister,
                onNavigateToUnitManagement = onNavigateToUnitManagement,
                onNavigateToPantryItems = onNavigateToPantryItems,
                onNavigateToNFeImport = onNavigateToNFeImport,
                onNavigateToShoppingLists = onNavigateToShoppingLists,
                onNavigateToRecipes = onNavigateToRecipes,
                onNavigateToDashboard = onNavigateToDashboard,
                onNavigateToNearbyStores = onNavigateToNearbyStores,
                onNavigateToPromotions = onNavigateToPromotions,
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToSettings = onNavigateToSettings,
                onLogout = {
                    viewModel.logout()
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                userName = currentUser?.nome
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.home_title),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        // Removido ícone de perfil conforme solicitado
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            // Apply padding to avoid content being obscured by drawer
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                // Reuse the content from HomeScreenWithMenu
                HomeScreenWithMenu(
                    navController = navController,
                    onNavigateToProductRegister = onNavigateToProductRegister,
                    onNavigateToCategoryRegister = onNavigateToCategoryRegister,
                    onNavigateToUnitRegister = onNavigateToUnitRegister,
                    onNavigateToUnitManagement = onNavigateToUnitManagement,
                    onNavigateToPantryItems = onNavigateToPantryItems,
                    onNavigateToNFeImport = onNavigateToNFeImport,
                    onNavigateToShoppingLists = onNavigateToShoppingLists,
                    onNavigateToRecipes = onNavigateToRecipes,
                    onNavigateToDashboard = onNavigateToDashboard,
                    onNavigateToNearbyStores = onNavigateToNearbyStores,
                    onNavigateToPromotions = onNavigateToPromotions,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToSettings = onNavigateToSettings,
                    viewModel = viewModel
                )
            }
        }
    }
}
