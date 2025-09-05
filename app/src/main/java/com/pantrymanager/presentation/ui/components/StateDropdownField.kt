package com.pantrymanager.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pantrymanager.utils.BrazilianStates

/**
 * Componente de seleção de estados brasileiros
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdownField(
    selectedState: String,
    onStateSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Encontra o nome do estado pela sigla para exibição
    val displayText = selectedState // Apenas a sigla (AC, SP, RJ, etc.)
    
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it }
        ) {
            OutlinedTextField(
                value = displayText,
                onValueChange = { },
                readOnly = true,
                label = { Text(label) },
                placeholder = { 
                    if (displayText.isBlank()) {
                        Text(
                            text = "Selecione...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                },
                trailingIcon = { 
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = enabled,
                singleLine = true
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                BrazilianStates.states.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = code, // Apenas a sigla
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onStateSelected(code) // Passa apenas a sigla
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
