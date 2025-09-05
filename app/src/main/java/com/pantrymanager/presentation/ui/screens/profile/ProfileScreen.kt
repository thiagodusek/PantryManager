package com.pantrymanager.presentation.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pantrymanager.R
import com.pantrymanager.presentation.ui.components.StateDropdownField
import com.pantrymanager.presentation.viewmodel.ProfileViewModel
import com.pantrymanager.utils.CepUtils
import com.pantrymanager.utils.CpfUtils
import com.pantrymanager.presentation.ui.components.CompactVersionFooter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onUpdateSuccess: (() -> Unit)? = null,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    
    // ViewModel states
    val nome by viewModel.nome.collectAsState()
    val sobrenome by viewModel.sobrenome.collectAsState()
    val email by viewModel.email.collectAsState()
    val cpf by viewModel.cpf.collectAsState()
    val login by viewModel.login.collectAsState()
    val endereco by viewModel.endereco.collectAsState()
    val numero by viewModel.numero.collectAsState()
    val complemento by viewModel.complemento.collectAsState()
    val cep by viewModel.cep.collectAsState()
    val cidade by viewModel.cidade.collectAsState()
    val estado by viewModel.estado.collectAsState()
    val nfePermissions by viewModel.nfePermissions.collectAsState()
    val searchRadius by viewModel.searchRadius.collectAsState()
    
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingCep by viewModel.isLoadingCep.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val cepError by viewModel.cepError.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    // Handle update success
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            onUpdateSuccess?.invoke()
        }
    }

    // Show loading indicator while loading user data
    if (isLoading && nome.isBlank()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Carregando perfil...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Meu Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Personal Information Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Informações Pessoais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nome,
                    onValueChange = { viewModel.updateNome(it) },
                    label = { Text(stringResource(R.string.nome) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = sobrenome,
                    onValueChange = { viewModel.updateSobrenome(it) },
                    label = { Text(stringResource(R.string.sobrenome) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text(stringResource(R.string.email) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading
                )

                // CPF Field (Read-only - não pode ser editado)
                OutlinedTextField(
                    value = if (cpf.isNotBlank()) CpfUtils.formatCpf(cpf) else "",
                    onValueChange = { }, // Não permite mudanças
                    label = { Text(stringResource(R.string.cpf) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = false, // Campo desabilitado
                    supportingText = {
                        Text(
                            text = "CPF não pode ser alterado",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                // Login Field (Read-only - não pode ser editado)
                OutlinedTextField(
                    value = login,
                    onValueChange = { }, // Não permite mudanças
                    label = { Text(stringResource(R.string.login) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = false, // Campo desabilitado
                    supportingText = {
                        Text(
                            text = "Login não pode ser alterado",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Address Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Endereço",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // CEP Field
                OutlinedTextField(
                    value = cep,
                    onValueChange = { viewModel.updateCep(it) },
                    label = { Text(stringResource(R.string.cep) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    trailingIcon = {
                        if (isLoadingCep) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    supportingText = {
                        cepError?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error
                            )
                        } ?: run {
                            when {
                                cep.isBlank() -> {
                                    Text(
                                        text = "Digite 8 números do CEP",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                cep.length < 8 -> {
                                    Text(
                                        text = "Digite mais ${8 - cep.length} números",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                cep.length == 8 -> {
                                    Text(
                                        text = "CEP: ${CepUtils.formatCep(cep)}", 
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    },
                    placeholder = { Text("12345678") }
                )

                OutlinedTextField(
                    value = endereco,
                    onValueChange = { viewModel.updateEndereco(it) },
                    label = { Text(stringResource(R.string.endereco) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading && !isLoadingCep
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { viewModel.updateNumero(it) },
                        label = { Text(stringResource(R.string.numero) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, bottom = 8.dp),
                        singleLine = true,
                        enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = complemento,
                        onValueChange = { viewModel.updateComplemento(it) },
                        label = { Text(stringResource(R.string.complemento)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        singleLine = true,
                        enabled = !isLoading
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = cidade,
                        onValueChange = { viewModel.updateCidade(it) },
                        label = { Text(stringResource(R.string.cidade) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, bottom = 8.dp),
                        singleLine = true,
                        enabled = !isLoading && !isLoadingCep
                    )

                    // ComboBox de Estados
                    StateDropdownField(
                        selectedState = estado,
                        onStateSelected = { viewModel.updateEstado(it) },
                        label = stringResource(R.string.estado) + " *",
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        enabled = !isLoading && !isLoadingCep
                    )
                }
            }
        }

        // Settings Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Configurações",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // NFe Permissions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = nfePermissions,
                        onCheckedChange = { viewModel.updateNfePermissions(it) },
                        enabled = !isLoading
                    )
                    Text(
                        text = stringResource(R.string.nfe_permissions),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = searchRadius,
                    onValueChange = { viewModel.updateSearchRadius(it) },
                    label = { Text(stringResource(R.string.search_radius)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    supportingText = {
                        Text(
                            text = "Raio de busca em km para lojas próximas",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Error Message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Success Message
        if (updateSuccess) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = "✅ Perfil atualizado com sucesso!",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Green,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Update Button
        Button(
            onClick = {
                viewModel.updateProfile()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading && viewModel.isFormValid()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Salvar Alterações")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cancel Button
        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading
        ) {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Cancelar")
        }

        // Compact Version Footer
        CompactVersionFooter(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
