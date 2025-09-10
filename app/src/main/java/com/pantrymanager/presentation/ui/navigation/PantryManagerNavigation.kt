package com.pantrymanager.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pantrymanager.presentation.ui.screens.auth.LoginScreen
import com.pantrymanager.presentation.ui.screens.auth.RegisterScreen
import com.pantrymanager.presentation.ui.screens.auth.ForgotPasswordScreen
import com.pantrymanager.presentation.ui.screens.home.HomeScreenWithDrawer
import com.pantrymanager.presentation.ui.screens.dashboard.DashboardScreen
import com.pantrymanager.presentation.ui.screens.product.ProductRegisterScreen
import com.pantrymanager.presentation.ui.screens.product.ProductManagementScreen
import com.pantrymanager.presentation.ui.screens.category.CategoryRegisterScreen
import com.pantrymanager.presentation.ui.screens.category.CategoryManagementScreen
import com.pantrymanager.presentation.ui.screens.unit.UnitRegisterScreen
import com.pantrymanager.presentation.ui.screens.unit.UnitManagementScreen
import com.pantrymanager.presentation.ui.screens.pantry.PantryItemsScreen
import com.pantrymanager.presentation.ui.screens.profile.ProfileScreen
import com.pantrymanager.presentation.ui.screens.settings.SettingsScreen
import com.pantrymanager.presentation.ui.screens.placeholder.PlaceholderScreen
import com.pantrymanager.presentation.viewmodel.AuthViewModel

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
    authViewModel: AuthViewModel = hiltViewModel()
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
                HomeScreenWithDrawer(
                    navController = navController,
                    onNavigateToProductRegister = {
                        navController.navigate(Screen.ProductRegister.route)
                    },
                    onNavigateToProductManagement = {
                        navController.navigate(Screen.ProductManagement.route)
                    },
                    onNavigateToCategoryRegister = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToCategoryManagement = {
                        navController.navigate(Screen.CategoryManagement.route)
                    },
                    onNavigateToUnitRegister = {
                        navController.navigate(Screen.UnitRegister.route)
                    },
                    onNavigateToUnitManagement = {
                        navController.navigate(Screen.UnitManagement.route)
                    },
                    onNavigateToPantryItems = {
                        navController.navigate(Screen.PantryItems.route)
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
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
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
                    }
                )
            }
        }
        
        composable(Screen.ProductRegister.route) {
            ProtectedRoute(
                isLoggedIn = isLoggedIn,
                navController = navController
            ) {
                ProductRegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToAddUnit = {
                        navController.navigate(Screen.UnitRegister.route)
                    }
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
                    }
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
                ProductRegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.CategoryRegister.route)
                    },
                    onNavigateToAddUnit = {
                        navController.navigate(Screen.UnitRegister.route)
                    },
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
                    }
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
                    }
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
                    }
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
                    }
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
                    }
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
                    }
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
                    }
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
                    onNavigateToDetails = { receiptId ->
                        navController.navigate(Screen.FiscalReceiptDetails.createRoute(receiptId))
                    },
                    onNavigateToScanner = {
                        navController.navigate(Screen.FiscalReceiptScanner.route)
                    }
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
                    }
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
                    }
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
                    }
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
                    }
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
                    onNavigateToDetails = { receiptId ->
                        navController.navigate(Screen.FiscalReceiptDetails.createRoute(receiptId))
                    },
                    onNavigateToScanner = {
                        navController.navigate(Screen.FiscalReceiptScanner.route)
                    }
                )
            }
        }
        
        // Additional screens will be added here
    }
}
