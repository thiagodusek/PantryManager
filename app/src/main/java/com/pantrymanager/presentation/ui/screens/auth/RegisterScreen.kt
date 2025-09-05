package com.pantrymanager.presentation.ui.screens.auth

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.pantrymanager.R
import com.pantrymanager.domain.entity.Address
import com.pantrymanager.domain.entity.User
import com.pantrymanager.presentation.ui.components.StateDropdownField
import com.pantrymanager.presentation.viewmodel.AuthViewModel
import com.pantrymanager.presentation.viewmodel.RegisterViewModel
import com.pantrymanager.utils.CepUtils
import com.pantrymanager.utils.CpfUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    registerViewModel: RegisterViewModel = hiltViewModel(),
    prefilledNome: String = "",
    prefilledSobrenome: String = "",
    prefilledEmail: String = ""
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    
    // Form fields state
    var nome by remember { mutableStateOf(prefilledNome) }
    var sobrenome by remember { mutableStateOf(prefilledSobrenome) }
    var email by remember { mutableStateOf(prefilledEmail) }
    var cpf by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nfePermissions by remember { mutableStateOf(false) }
    var searchRadius by remember { mutableStateOf("5.0") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Auth ViewModel states
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    // Register ViewModel states
    val isLoadingCep by registerViewModel.isLoadingCep.collectAsState()
    val addressData by registerViewModel.addressData.collectAsState()
    val cepError by registerViewModel.cepError.collectAsState()

    // Handle registration success
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onRegisterSuccess()
            viewModel.clearLoginSuccess()
        }
    }

    // Handle address data from CEP
    LaunchedEffect(addressData) {
        addressData?.let { address ->
            endereco = address.endereco
            cidade = address.cidade
            estado = address.estado
            complemento = address.complemento ?: ""
        }
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
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Google Data Info (if prefilled)
        if (prefilledEmail.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Complete seu cadastro com as informações do Google",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                    onValueChange = { nome = it },
                    label = { Text(stringResource(R.string.nome) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = sobrenome,
                    onValueChange = { sobrenome = it },
                    label = { Text(stringResource(R.string.sobrenome) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cpf,
                    onValueChange = { newCpf ->
                        // Permite apenas números e limita a 11 dígitos
                        val digitsOnly = newCpf.filter { it.isDigit() }.take(11)
                        cpf = digitsOnly
                    },
                    label = { Text(stringResource(R.string.cpf) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    supportingText = {
                        when {
                            cpf.isBlank() -> {
                                Text(
                                    text = "Digite 11 números do CPF",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            cpf.length < 11 -> {
                                Text(
                                    text = "Digite mais ${11 - cpf.length} números",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            cpf.length == 11 -> {
                                if (CpfUtils.isValidCpf(cpf)) {
                                    Text(
                                        text = "CPF: ${CpfUtils.formatCpf(cpf)}",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Text(
                                        text = "CPF inválido",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    },
                    placeholder = { Text("12345678901") }
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

                // CEP Field (primeiro campo)
                OutlinedTextField(
                    value = cep,
                    onValueChange = { newCep ->
                        // Permite apenas números e limita a 8 dígitos
                        val digitsOnly = newCep.filter { it.isDigit() }.take(8)
                        cep = digitsOnly
                        
                        // Busca automaticamente quando tiver 8 dígitos
                        if (digitsOnly.length == 8) {
                            registerViewModel.searchAddressByCep(digitsOnly)
                        } else if (digitsOnly.length < 8) {
                            // Limpa dados se CEP estiver incompleto
                            registerViewModel.clearAddressData()
                        }
                    },
                    label = { Text(stringResource(R.string.cep) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
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
                            // Mostra dica de formato quando não há erro
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
                    onValueChange = { endereco = it },
                    label = { Text(stringResource(R.string.endereco) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    enabled = !isLoadingCep
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text(stringResource(R.string.numero) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, bottom = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = complemento,
                        onValueChange = { complemento = it },
                        label = { Text(stringResource(R.string.complemento)) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = cidade,
                        onValueChange = { cidade = it },
                        label = { Text(stringResource(R.string.cidade) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, bottom = 8.dp),
                        singleLine = true,
                        enabled = !isLoadingCep
                    )

                    // ComboBox de Estados
                    StateDropdownField(
                        selectedState = estado,
                        onStateSelected = { estado = it },
                        label = stringResource(R.string.estado) + " *",
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        enabled = !isLoadingCep
                    )
                }
            }
        }

        // Account Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Conta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text(stringResource(R.string.login) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password) + " *") },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.confirm_password) + " *") },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true
                )

                // Permissions and Settings
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = nfePermissions,
                        onCheckedChange = { nfePermissions = it }
                    )
                    Text(
                        text = stringResource(R.string.nfe_permissions),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = searchRadius,
                    onValueChange = { searchRadius = it },
                    label = { Text(stringResource(R.string.search_radius)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
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

        // Register Button
        Button(
            onClick = {
                viewModel.clearError()
                val cleanCep = CepUtils.cleanCep(cep)
                val cleanCpf = CpfUtils.cleanCpf(cpf)
                val user = User(
                    nome = nome,
                    sobrenome = sobrenome,
                    email = email,
                    cpf = cleanCpf,
                    endereco = Address(
                        endereco = endereco,
                        numero = numero,
                        complemento = complemento.ifBlank { null },
                        cep = cleanCep,
                        cidade = cidade,
                        estado = estado
                    ),
                    login = login,
                    nfePermissions = nfePermissions,
                    searchRadius = searchRadius.toDoubleOrNull() ?: 5.0
                )
                viewModel.register(user, password, confirmPassword)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading && 
                    nome.isNotBlank() && 
                    sobrenome.isNotBlank() && 
                    email.isNotBlank() && 
                    CpfUtils.isValidCpf(cpf) && 
                    endereco.isNotBlank() && 
                    numero.isNotBlank() && 
                    CepUtils.cleanCep(cep).length == 8 && 
                    cidade.isNotBlank() && 
                    estado.isNotBlank() && 
                    login.isNotBlank() && 
                    password.isNotBlank() && 
                    confirmPassword.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.register))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cancel Button
        OutlinedButton(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.cancel))
        }

        // Login Link
        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(R.string.already_have_account))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
