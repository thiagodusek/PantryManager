package com.pantrymanager.presentation.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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

data class DashboardCard(
    val title: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateBack: () -> Unit
) {
    val dashboardCards = listOf(
        DashboardCard(
            title = stringResource(R.string.expiring_products),
            icon = Icons.Default.Warning
        ) {
            DashboardCardContent("Produtos próximos do vencimento", "3 produtos")
        },
        DashboardCard(
            title = stringResource(R.string.most_consumed),
            icon = Icons.Default.TrendingUp
        ) {
            DashboardCardContent("Produtos mais consumidos", "Arroz, Feijão, Açúcar")
        },
        DashboardCard(
            title = stringResource(R.string.current_stock),
            icon = Icons.Default.Inventory
        ) {
            DashboardCardContent("Estoque atual", "45 itens")
        },
        DashboardCard(
            title = stringResource(R.string.best_price_stores),
            icon = Icons.Default.Store
        ) {
            DashboardCardContent("Melhor custo/benefício", "Supermercado ABC")
        },
        DashboardCard(
            title = stringResource(R.string.price_comparison),
            icon = Icons.Default.CompareArrows
        ) {
            DashboardCardContent("Comparação de preços", "Ver lista completa")
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dashboards),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
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
            items(dashboardCards) { dashboardCard ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = dashboardCard.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 12.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Text(
                                text = dashboardCard.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        dashboardCard.content()
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCardContent(
    subtitle: String,
    value: String
) {
    Column {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
