package com.pantrymanager.presentation.ui.navigation

// Adione esta rota às suas rotas existentes
object ProductRoutes {
    const val PRODUCT_REGISTRATION = "product_registration"
    const val PRODUCT_LIST = "product_list"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    
    fun productDetail(productId: Long) = "product_detail/$productId"
}

// Exemplo de como integrar a nova tela na navegação
/*
In your NavGraph:

composable(ProductRoutes.PRODUCT_REGISTRATION) {
    ProductRegistrationScreen(
        onNavigateBack = { navController.popBackStack() }
    )
}

// Para navegar para a tela de cadastro:
navController.navigate(ProductRoutes.PRODUCT_REGISTRATION)
*/
