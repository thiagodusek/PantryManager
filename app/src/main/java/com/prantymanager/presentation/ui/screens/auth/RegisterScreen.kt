package com.prantymanager.presentation.ui.screens.auth

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
import com.prantymanager.R
import com.prantymanager.domain.entity.Address
import com.prantymanager.domain.entity.User
import com.prantymanager.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    
    // Form fields state
    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    // Handle registration success
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onRegisterSuccess()
            viewModel.clearLoginSuccess()
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
                    onValueChange = { cpf = it },
                    label = { Text(stringResource(R.string.cpf) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
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

                OutlinedTextField(
                    value = endereco,
                    onValueChange = { endereco = it },
                    label = { Text(stringResource(R.string.endereco) + " *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
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

                OutlinedTextField(
                    value = cep,
                    onValueChange = { cep = it },
                    label = { Text(stringResource(R.string.cep) + " *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = cidade,
                        onValueChange = { cidade = it },
                        label = { Text(stringResource(R.string.cidade) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, bottom = 8.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = estado,
                        onValueChange = { estado = it },
                        label = { Text(stringResource(R.string.estado) + " *") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp),
                        singleLine = true
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
                val user = User(
                    nome = nome,
                    sobrenome = sobrenome,
                    email = email,
                    cpf = cpf,
                    endereco = Address(
                        endereco = endereco,
                        numero = numero,
                        complemento = complemento.ifBlank { null },
                        cep = cep,
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
                    cpf.isNotBlank() && 
                    endereco.isNotBlank() && 
                    numero.isNotBlank() && 
                    cep.isNotBlank() && 
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
