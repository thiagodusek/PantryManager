package com.pantrymanager.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Paleta de cores personalizadas para PantryManager
object PantryColors {
    // Cores primárias
    val Primary = Color(0xFF2E7D32) // Verde principal (tema despensa/comida)
    val PrimaryVariant = Color(0xFF1B5E20)
    val Secondary = Color(0xFFFF8F00) // Laranja (energia/comida)
    val SecondaryVariant = Color(0xFFEF6C00)
    
    // Cores de superfície
    val Surface = Color(0xFFF8F9FA)
    val SurfaceVariant = Color(0xFFE8F5E8)
    val Background = Color(0xFFFFFFFF)
    
    // Cores de estado
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Error = Color(0xFFE53935)
    val Info = Color(0xFF2196F3)
    
    // Cores de texto
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF1C1B1F)
    val OnBackground = Color(0xFF1C1B1F)
    val OnSurfaceVariant = Color(0xFF49454F)
    
    // Cores específicas para categorias de produtos
    val FruitsVegetables = Color(0xFF4CAF50)
    val Meat = Color(0xFFE53935)
    val Dairy = Color(0xFF2196F3)
    val Bakery = Color(0xFFFF8F00)
    val Beverages = Color(0xFF9C27B0)
    val Pantry = Color(0xFF795548)
    val Frozen = Color(0xFF607D8B)
    val Cleaning = Color(0xFF00BCD4)
}

// Gradientes personalizados
object PantryGradients {
    val PrimaryGradient = listOf(
        PantryColors.Primary,
        PantryColors.Primary.copy(alpha = 0.8f)
    )
    
    val SecondaryGradient = listOf(
        PantryColors.Secondary,
        PantryColors.SecondaryVariant
    )
    
    val AccentGradient = listOf(
        PantryColors.Info,
        PantryColors.Info.copy(alpha = 0.8f)
    )
    
    val SuccessGradient = listOf(
        PantryColors.Success,
        PantryColors.Success.copy(alpha = 0.8f)
    )
    
    val CategoryGradients = mapOf(
        "fruits" to listOf(PantryColors.FruitsVegetables, PantryColors.FruitsVegetables.copy(alpha = 0.7f)),
        "meat" to listOf(PantryColors.Meat, PantryColors.Meat.copy(alpha = 0.7f)),
        "dairy" to listOf(PantryColors.Dairy, PantryColors.Dairy.copy(alpha = 0.7f)),
        "bakery" to listOf(PantryColors.Bakery, PantryColors.Bakery.copy(alpha = 0.7f)),
        "beverages" to listOf(PantryColors.Beverages, PantryColors.Beverages.copy(alpha = 0.7f)),
        "pantry" to listOf(PantryColors.Pantry, PantryColors.Pantry.copy(alpha = 0.7f)),
        "frozen" to listOf(PantryColors.Frozen, PantryColors.Frozen.copy(alpha = 0.7f)),
        "cleaning" to listOf(PantryColors.Cleaning, PantryColors.Cleaning.copy(alpha = 0.7f))
    )
}

val PantryLightColorScheme = lightColorScheme(
    primary = PantryColors.Primary,
    onPrimary = PantryColors.OnPrimary,
    primaryContainer = PantryColors.SurfaceVariant,
    onPrimaryContainer = PantryColors.OnSurface,
    
    secondary = PantryColors.Secondary,
    onSecondary = PantryColors.OnSecondary,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF8A2E00),
    
    tertiary = Color(0xFF6750A4),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEADDFF),
    onTertiaryContainer = Color(0xFF21005D),
    
    error = PantryColors.Error,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = PantryColors.Background,
    onBackground = PantryColors.OnBackground,
    
    surface = PantryColors.Surface,
    onSurface = PantryColors.OnSurface,
    surfaceVariant = PantryColors.SurfaceVariant,
    onSurfaceVariant = PantryColors.OnSurfaceVariant,
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0)
)

val PantryDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF003910),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFA5D6A7),
    
    secondary = Color(0xFFFFB74D),
    onSecondary = Color(0xFF5A2800),
    secondaryContainer = Color(0xFF8A4000),
    onSecondaryContainer = Color(0xFFFFDCC2),
    
    tertiary = Color(0xFFCFBDFE),
    onTertiary = Color(0xFF381E72),
    tertiaryContainer = Color(0xFF4F378B),
    onTertiaryContainer = Color(0xFFEADDFF),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
)
