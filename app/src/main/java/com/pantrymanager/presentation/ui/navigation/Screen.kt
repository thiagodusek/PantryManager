package com.pantrymanager.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object ProductRegister : Screen("product_register")
    object ProductManagement : Screen("product_management")
    object ProductEdit : Screen("product_edit/{productId}") {
        fun createRoute(productId: Long) = "product_edit/$productId"
    }
    object CategoryRegister : Screen("category_register")
    object CategoryManagement : Screen("category_management")
    object UnitRegister : Screen("unit_register")
    object UnitManagement : Screen("unit_management")
    object PantryItems : Screen("pantry_items")
    object NFeImport : Screen("nfe_import")
    object FiscalReceiptScanner : Screen("fiscal_receipt_scanner")
    object FiscalReceiptDetails : Screen("fiscal_receipt_details/{receiptId}") {
        fun createRoute(receiptId: Long) = "fiscal_receipt_details/$receiptId"
    }
    object FiscalReceiptList : Screen("fiscal_receipt_list")
    object ShoppingLists : Screen("shopping_lists")
    object Recipes : Screen("recipes")
    object Dashboard : Screen("dashboard")
    object NearbyStores : Screen("nearby_stores")
    object Promotions : Screen("promotions")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
