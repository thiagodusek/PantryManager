package com.pantrymanager.presentation.ui.screens.unit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pantrymanager.domain.entity.MeasurementUnit as UnitEntity
import com.pantrymanager.presentation.viewmodel.UnitViewModel
import com.pantrymanager.presentation.ui.components.StandardTopAppBarWithMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitRegisterScreen(
    onNavigateBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: UnitViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Handle success state
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.clearSuccess()
            // Show success message or do nothing since form is already cleared
        }
    }

    // Handle error messages
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // You can show a snackbar or toast here
            viewModel.clearError()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        StandardTopAppBarWithMenu(
            title = "Cadastrar Unidades de Medida",
            onMenuClick = onOpenDrawer
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Add Unit Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Adicionar Nova Unidade de Medida",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = state.name,
                        onValueChange = viewModel::onNameChanged,
                        label = { Text("Nome da Unidade *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.validationErrors.nameError != null,
                        supportingText = state.validationErrors.nameError?.let { error ->
                            { Text(error, color = MaterialTheme.colorScheme.error) }
                        },
                        placeholder = { Text("Ex: Quilograma, Litro, Unidade") }
                    )

                    OutlinedTextField(
                        value = state.abbreviation,
                        onValueChange = viewModel::onAbbreviationChanged,
                        label = { Text("Abreviação *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.validationErrors.abbreviationError != null,
                        supportingText = state.validationErrors.abbreviationError?.let { error ->
                            { Text(error, color = MaterialTheme.colorScheme.error) }
                        },
                        placeholder = { Text("Ex: kg, l, un") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.multiplyQuantityByPrice,
                            onCheckedChange = viewModel::onMultiplyQuantityByPriceChanged
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "Multiplicar quantidade pelo preço",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Para produtos vendidos por peso/volume",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = viewModel::addUnit,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Adicionar Unidade")
                    }

                    Text(
                        "* Campos obrigatórios",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Units List Section
            Text(
                "Unidades Cadastradas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (state.units.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Scale,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Nenhuma unidade cadastrada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Adicione a primeira unidade acima",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.units) { unit ->
                        UnitItem(unit = unit)
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitItem(unit: UnitEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Scale,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = unit.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Abreviação: ${unit.abbreviation}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (unit.multiplyQuantityByPrice) {
                        Text(
                            text = "• Por peso/volume",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
