package com.pantrymanager.presentation.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pantrymanager.presentation.ui.screens.auth.LoginScreen
import com.pantrymanager.presentation.ui.screens.auth.RegisterScreen
import com.pantrymanager.presentation.ui.screens.auth.ForgotPasswordScreen
import com.pantrymanager.presentation.ui.screens.home.HomeScreen
import com.pantrymanager.presentation.ui.screens.dashboard.DashboardScreen
import com.pantrymanager.presentation.ui.screens.product.ProductRegistrationScreen
import com.pantrymanager.presentation.ui.screens.product.ProductManagementScreen
import com.pantrymanager.presentation.ui.screens.category.CategoryRegisterScreen
import com.pantrymanager.presentation.ui.screens.category.CategoryManagementScreen
import com.pantrymanager.presentation.ui.screens.brand.BrandRegisterScreen
import com.pantrymanager.presentation.ui.screens.brand.BrandManagementScreen
import com.pantrymanager.presentation.ui.screens.unit.UnitRegisterScreen
import com.pantrymanager.presentation.ui.screens.unit.UnitManagementScreen
import com.pantrymanager.presentation.ui.screens.pantry.PantryItemsScreen
import com.pantrymanager.presentation.ui.screens.profile.ProfileScreen
import com.pantrymanager.presentation.ui.screens.settings.SettingsScreen
import com.pantrymanager.presentation.ui.screens.placeholder.PlaceholderScreen
import com.pantrymanager.presentation.ui.screens.qr.QRScannerScreen
import com.pantrymanager.presentation.ui.components.NavigationDrawerContent
import com.pantrymanager.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryManagerAppWithDrawer(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser by authViewModel.currentUser.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLoading by authViewModel.isLoading.collectAsState()
    
    // Verificar estado de autenticação ao iniciar o app
    LaunchedEffect(Unit) {
        println("DEBUG - App started, checking authentication state...")
        authViewModel.refreshAuthenticationState()
    }
    
    // Função para abrir o drawer
    val onOpenDrawer: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }
    
    // Monitorar logout para redirecionar ao login
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn && 
            navController.currentDestination?.route != Screen.Login.route && 
            navController.currentDestination?.route != Screen.Register.route &&
            navController.currentDestination?.route != Screen.ForgotPassword.route) {
            println("DEBUG - User not logged in, redirecting to login...")
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        } else if (isLoggedIn && navController.currentDestination?.route == Screen.Login.route) {
            println("DEBUG - User is logged in, redirecting to home...")
            navController.navigate(Screen.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Mostrar tela de carregamento durante verificação inicial de autenticação
    if (isLoading && currentRoute == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Verificando autenticação...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        return
    }
    
    // Mostrar drawer apenas para usuários logados e não na tela de login/register
    val showDrawer = isLoggedIn && 
        currentRoute != Screen.Login.route && 
        currentRoute != Screen.Register.route &&
        currentRoute != Screen.ForgotPassword.route
    
    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawerContent(
                    currentRoute = currentRoute,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToProductRegister = {
                        navController.navigate(Screen.ProductRegister.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToProductManagement = {
                        navController.navigate(Screen.ProductManagement.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToCategoryRegister = {
                        navController.navigate(Screen.CategoryRegister.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToCategoryManagement = {
                        navController.navigate(Screen.CategoryManagement.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToBrandRegister = {
                        navController.navigate(Screen.BrandRegister.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToBrandManagement = {
                        navController.navigate(Screen.BrandManagement.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToUnitRegister = {
                        navController.navigate(Screen.UnitRegister.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToUnitManagement = {
                        navController.navigate(Screen.UnitManagement.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToPantryItems = {
                        navController.navigate(Screen.PantryItems.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToNFeImport = {
                        navController.navigate(Screen.NFeImport.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToShoppingLists = {
                        navController.navigate(Screen.ShoppingLists.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToRecipes = {
                        navController.navigate(Screen.Recipes.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToNearbyStores = {
                        navController.navigate(Screen.NearbyStores.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToPromotions = {
                        navController.navigate(Screen.Promotions.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                        scope.launch { drawerState.close() }
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        authViewModel.logout()
                        scope.launch { drawerState.close() }
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
            PantryManagerNavigation(
                navController = navController,
                authViewModel = authViewModel,
                onOpenDrawer = onOpenDrawer
            )
        }
    } else {
        PantryManagerNavigation(
            navController = navController,
            authViewModel = authViewModel,
            onOpenDrawer = null
        )
    }
}

@Composable
private fun ProtectedRoute(
    isLoggedIn: Boolean,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    if (isLoggedIn) {
        content()
    } else {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}

@Composable
fun PantryManagerNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onOpenDrawer: (() -> Unit)? = null
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val loginSuccess by authViewModel.loginSuccess.collectAsState()
    
    // Monitorar sucesso do login para navegar
    LaunchedEffect(loginSuccess, isLoggedIn) {
        if (loginSuccess && isLoggedIn) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                popUpTo(Screen.Register.route) { inclusive = true }
            }
            authViewModel.clearLoginSuccess()
        }
    }
    
    // Monitorar logout para redirecionar ao login
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn && 
            navController.currentDestination?.route != Screen.Login.route && 
            navController.currentDestination?.route != Screen.Register.route &&
            navController.currentDestination?.route != Screen.ForgotPassword.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    val startDestination = if (isLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    // Navegação gerenciada pelo LaunchedEffect
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    // Navegação gerenciada pelo LaunchedEffect
                }
            )
        }
        
        composable(Screen.Home.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                HomeScreen(
                    navController = navController,
                    onOpenDrawer = onOpenDrawer ?: {},
                    onNavigateToProductRegister = {
                        navController.navigate(Screen.ProductRegister.route)
                    },
                    onNavigateToCategoryRegister = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToBrandRegister = {
                        navController.navigate(Screen.BrandRegister.route)
                    },
                    onNavigateToBrandManagement = {
                        navController.navigate(Screen.BrandManagement.route)
                    },
                    onNavigateToUnitRegister = {
                        navController.navigate(Screen.UnitRegister.route)
                    },
                    onNavigateToUnitManagement = {
                        navController.navigate(Screen.UnitManagement.route)
                    },
                    onNavigateToNFeImport = {
                        navController.navigate(Screen.NFeImport.route)
                    },
                    onNavigateToShoppingLists = {
                        navController.navigate(Screen.ShoppingLists.route)
                    },
                    onNavigateToRecipes = {
                        navController.navigate(Screen.Recipes.route)
                    },
                    onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route)
                    },
                    onNavigateToNearbyStores = {
                        navController.navigate(Screen.NearbyStores.route)
                    },
                    onNavigateToPromotions = {
                        navController.navigate(Screen.Promotions.route)
                    }
                )
            }
        }
        
        composable(Screen.Dashboard.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                DashboardScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.ProductRegister.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                ProductRegistrationScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToAddUnit = {
                        navController.navigate(Screen.UnitRegister.route)
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(Screen.ProductManagement.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                ProductManagementScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAddProduct = {
                        navController.navigate(Screen.ProductRegister.route)
                    },
                    onNavigateToEditProduct = { productId ->
                        navController.navigate(Screen.ProductEdit.createRoute(productId))
                    },
                    onNavigateToQRScanner = {
                        navController.navigate(Screen.QRScanner.route)
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(
            route = Screen.ProductEdit.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                ProductRegistrationScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToAddUnit = {
                        navController.navigate(Screen.UnitRegister.route)
                    },
                    onOpenDrawer = onOpenDrawer ?: {},
                    productIdToEdit = productId
                )
            }
        }
        
        composable(Screen.CategoryRegister.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                CategoryRegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(Screen.CategoryManagement.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                CategoryManagementScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(Screen.BrandRegister.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                BrandRegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(Screen.BrandManagement.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                BrandManagementScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToBrandRegister = {
                        navController.navigate(Screen.BrandRegister.route)
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.UnitRegister.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                UnitRegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }

        composable(Screen.UnitManagement.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                UnitManagementScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.PantryItems.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                PantryItemsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.Profile.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.Settings.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                SettingsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.NFeImport.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                com.pantrymanager.presentation.ui.screens.fiscalreceipt.FiscalReceiptScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.ShoppingLists.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                PlaceholderScreen(
                    title = "Listas de Compras",
                    description = "Criar e gerenciar suas listas de compras",
                    icon = Icons.Default.List,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.Recipes.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                PlaceholderScreen(
                    title = "Receitas",
                    description = "Gerenciar suas receitas favoritas",
                    icon = Icons.Default.MenuBook,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.NearbyStores.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                PlaceholderScreen(
                    title = "Supermercados Próximos",
                    description = "Encontrar supermercados próximos",
                    icon = Icons.Default.Store,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        composable(Screen.Promotions.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                PlaceholderScreen(
                    title = "Promoções",
                    description = "Visualizar promoções e ofertas",
                    icon = Icons.Default.LocalOffer,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        // Scanner de Cupom Fiscal
        composable(Screen.FiscalReceiptScanner.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                com.pantrymanager.presentation.ui.screens.fiscalreceipt.FiscalReceiptScannerScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onScanSuccess = { receiptId, importedCount ->
                        navController.navigate(Screen.FiscalReceiptDetails.createRoute(receiptId)) {
                            popUpTo(Screen.FiscalReceiptScanner.route) { inclusive = true }
                        }
                    }
                )
            }
        }
        
        // Detalhes do Cupom Fiscal
        composable(
            route = Screen.FiscalReceiptDetails.route,
            arguments = listOf(navArgument("receiptId") { type = NavType.LongType })
        ) { backStackEntry ->
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                val receiptId = backStackEntry.arguments?.getLong("receiptId") ?: 0L
                com.pantrymanager.presentation.ui.screens.fiscalreceipt.FiscalReceiptDetailsScreen(
                    fiscalReceiptId = receiptId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
        
        // Lista de Cupons Fiscais
        composable(Screen.FiscalReceiptList.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                com.pantrymanager.presentation.ui.screens.fiscalreceipt.FiscalReceiptScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        // QR Scanner
        composable(Screen.QRScanner.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                QRScannerScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToProductRegistration = { eanCode ->
                        navController.navigate(Screen.ProductRegister.route + "?ean=$eanCode")
                    },
                    onOpenDrawer = onOpenDrawer ?: {}
                )
            }
        }
        
        // Additional screens will be added here
    }
}
