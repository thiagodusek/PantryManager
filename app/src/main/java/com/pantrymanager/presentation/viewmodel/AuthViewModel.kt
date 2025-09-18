package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.auth.GoogleSignInHelper
import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.usecase.auth.GetCurrentUserUseCase
import com.pantrymanager.domain.usecase.auth.LoginUseCase
import com.pantrymanager.domain.usecase.auth.GoogleSignInUseCase
import com.pantrymanager.domain.usecase.auth.RegisterUserUseCase
import com.pantrymanager.domain.usecase.user.SaveUserUseCase
import com.pantrymanager.domain.usecase.user.ValidateUserCredentialsUseCase
import com.pantrymanager.domain.usecase.user.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase,
    private val googleSignInHelper: GoogleSignInHelper
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _needsRegistration = MutableStateFlow(false)
    val needsRegistration: StateFlow<Boolean> = _needsRegistration.asStateFlow()

    private val _googleUserData = MutableStateFlow<GoogleUserData?>(null)
    val googleUserData: StateFlow<GoogleUserData?> = _googleUserData.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUser.value = user
                _isLoggedIn.value = user != null
            }
        }
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Força logout completo antes de tentar login para limpar todo cache
                signOutCompletely()
                
                // Aguarda um momento para garantir que o logout foi processado
                delay(1000)
                
                val result = loginUseCase(login, password)
                
                result.fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        _isLoggedIn.value = true
                        _loginSuccess.value = true
                    },
                    onFailure = { exception ->
                        // Trata diferentes tipos de erro de autenticação
                        val errorMsg = when {
                            exception.message?.contains("invalid-email", ignoreCase = true) == true -> "Email inválido"
                            exception.message?.contains("user-not-found", ignoreCase = true) == true -> "Email ou senha inválidos"
                            exception.message?.contains("wrong-password", ignoreCase = true) == true -> 
                                "Senha incorreta. Se você redefiniu a senha recentemente, aguarde alguns minutos para sincronização."
                            exception.message?.contains("invalid-credential", ignoreCase = true) == true -> 
                                "Credenciais inválidas. Se você redefiniu a senha recentemente, aguarde alguns minutos para sincronização."
                            exception.message?.contains("too-many-requests", ignoreCase = true) == true -> "Muitas tentativas. Tente novamente mais tarde"
                            exception.message?.contains("network", ignoreCase = true) == true -> "Erro de conexão. Verifique sua internet"
                            else -> "Email ou senha inválidos"
                        }
                        _errorMessage.value = errorMsg
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Erro no login. Tente novamente."
            }
            
            _isLoading.value = false
        }
    }

    fun getGoogleSignInIntent() = googleSignInHelper.getSignInIntent()

    fun handleGoogleSignInResult(task: com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = googleSignInHelper.handleSignInResult(task)
            
            result.fold(
                onSuccess = { idToken ->
                    signInWithGoogle(idToken)
                },
                onFailure = { exception ->
                    // Se for erro de configuração, adicionar diagnóstico
                    val errorMessage = if (exception.message?.contains("configuração", ignoreCase = true) == true) {
                        val diagnosis = diagnoseGoogleSignIn()
                        "${exception.message}\n\nDiagnóstico:\n$diagnosis"
                    } else {
                        exception.message ?: "Erro no login com Google"
                    }
                    
                    _errorMessage.value = errorMessage
                    _isLoading.value = false
                }
            )
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = googleSignInUseCase(idToken)
            
            result.fold(
                onSuccess = { user ->
                    // Verifica se o usuário tem dados completos no Firestore
                    checkUserCompleteness(user)
                },
                onFailure = { exception ->
                    when {
                        exception.message?.contains("account-exists-with-different-credential") == true -> {
                            _errorMessage.value = "Esta conta já está associada a outro método de login"
                        }
                        exception.message?.contains("user-not-found") == true -> {
                            // Usuário autenticou com Google mas não tem perfil completo
                            // Extrair dados do Google e redirecionar para cadastro
                            extractGoogleUserData(idToken)
                        }
                        exception.message?.contains("network", ignoreCase = true) == true -> {
                            _errorMessage.value = "Erro de conexão. Verifique sua internet"
                        }
                        else -> {
                            _errorMessage.value = "Erro no login com Google: ${exception.message}"
                        }
                    }
                }
            )
            
            _isLoading.value = false
        }
    }

    private suspend fun checkUserCompleteness(user: User) {
        // Verifica se o usuário tem dados completos (endereço, CPF, etc.)
        if (user.cpf.isBlank() || user.endereco.endereco.isBlank()) {
            // Usuário precisa completar cadastro
            _googleUserData.value = GoogleUserData(
                nome = user.nome,
                sobrenome = user.sobrenome,
                email = user.email
            )
            _needsRegistration.value = true
        } else {
            // Usuário tem dados completos, pode logar
            _currentUser.value = user
            _isLoggedIn.value = true
            _loginSuccess.value = true
        }
    }

    private suspend fun extractGoogleUserData(idToken: String) {
        try {
            // Aqui você extrairia dados do token do Google
            // Por enquanto, vamos simular que precisamos do cadastro
            _needsRegistration.value = true
            _googleUserData.value = GoogleUserData(
                nome = "",
                sobrenome = "",
                email = ""
            )
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao processar dados do Google"
        }
    }

    fun register(user: User, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Valida todos os campos únicos (email, CPF, login) de uma vez
            val validationResult = validateUserCredentialsUseCase.validateUniqueFields(
                email = user.email,
                cpf = user.cpf,
                login = user.login
            )

            when (validationResult) {
                is ValidationResult.AllUnique -> {
                    // Todos os campos são únicos, procede com o registro
                    proceedWithRegistration(user, password, confirmPassword)
                }
                is ValidationResult.EmailInUse -> {
                    _errorMessage.value = "Este email já está em uso"
                    _isLoading.value = false
                }
                is ValidationResult.CpfInUse -> {
                    _errorMessage.value = "Este CPF já está cadastrado"
                    _isLoading.value = false
                }
                is ValidationResult.LoginInUse -> {
                    _errorMessage.value = "Este login já está em uso"
                    _isLoading.value = false
                }
                is ValidationResult.Error -> {
                    _errorMessage.value = validationResult.message
                    _isLoading.value = false
                }
            }
        }
    }

    private suspend fun proceedWithRegistration(user: User, password: String, confirmPassword: String) {
        val result = registerUserUseCase(user, password, confirmPassword)
        
        result.fold(
            onSuccess = { registeredUser ->
                // Salva os dados completos do usuário no Firestore
                saveUserToFirestore(registeredUser)
            },
            onFailure = { exception ->
                _errorMessage.value = exception.message ?: "Erro no cadastro"
                _isLoading.value = false
            }
        )
    }

    private suspend fun saveUserToFirestore(user: User) {
        val saveResult = saveUserUseCase(user)
        
        saveResult.fold(
            onSuccess = { savedUser ->
                _currentUser.value = savedUser
                _isLoggedIn.value = true
                _loginSuccess.value = true
            },
            onFailure = { exception ->
                _errorMessage.value = "Usuário criado, mas erro ao salvar dados: ${exception.message}"
            }
        )
        
        _isLoading.value = false
    }

    fun logout() {
        viewModelScope.launch {
            try {
                googleSignInHelper.signOut()
                _currentUser.value = null
                _isLoggedIn.value = false
                _loginSuccess.value = false
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao fazer logout"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }

    fun clearLoginSuccess() {
        _loginSuccess.value = false
    }

    fun clearNeedsRegistration() {
        _needsRegistration.value = false
    }

    fun clearGoogleUserData() {
        _googleUserData.value = null
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Força logout completo para limpar todos os tokens em cache
                signOutCompletely()
                
                // Integração com Firebase Auth para envio de email de recuperação
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        _isLoading.value = false
                        if (task.isSuccessful) {
                            // Limpa qualquer cache relacionado ao usuário
                            clearUserCache()
                            onResult(true)
                        } else {
                            val errorMsg = when {
                                task.exception?.message?.contains("no user record", ignoreCase = true) == true -> 
                                    "Email não encontrado. Verifique se o email está correto."
                                task.exception?.message?.contains("badly formatted", ignoreCase = true) == true -> 
                                    "Email inválido. Verifique o formato do email."
                                task.exception?.message?.contains("network", ignoreCase = true) == true -> 
                                    "Erro de conexão. Verifique sua internet."
                                else -> 
                                    "Erro ao enviar email de recuperação. Tente novamente."
                            }
                            _errorMessage.value = errorMsg
                            onResult(false)
                        }
                    }
                    .addOnFailureListener { exception ->
                        _isLoading.value = false
                        _errorMessage.value = "Erro ao enviar email de recuperação. Verifique sua conexão."
                        onResult(false)
                    }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Erro ao enviar email de recuperação. Tente novamente."
                onResult(false)
            }
        }
    }

    /**
     * Faz logout silencioso para limpar cache de autenticação
     */
    private fun signOutSilently() {
        try {
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
            auth.signOut()
            // Executa signOut do Google de forma não bloqueante
            viewModelScope.launch {
                try {
                    googleSignInHelper.signOut()
                } catch (e: Exception) {
                    // Ignora erros no logout do Google
                }
            }
        } catch (e: Exception) {
            // Ignora erros no logout silencioso
        }
    }

    /**
     * Limpa cache do usuário
     */
    private fun clearUserCache() {
        _currentUser.value = null
        _isLoggedIn.value = false
        _loginSuccess.value = false
        _googleUserData.value = null
    }

    // Método legado - manter compatibilidade
    fun resetPassword(email: String) {
        sendPasswordResetEmail(email) { success ->
            if (success) {
                _successMessage.value = "Email de recuperação enviado com sucesso!"
            }
        }
    }

    /**
     * Verifica e atualiza o estado de autenticação atual
     */
    fun checkAuthenticationState() {
        viewModelScope.launch {
            try {
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                val currentFirebaseUser = auth.currentUser
                
                if (currentFirebaseUser != null) {
                    // Força reload do usuário para garantir dados atualizados
                    currentFirebaseUser.reload().await()
                    
                    // Verifica se o token ainda é válido
                    currentFirebaseUser.getIdToken(true).await()
                    
                    // Se chegou até aqui, o usuário está autenticado
                    // Busca dados do usuário no Firestore
                    try {
                        getCurrentUserUseCase().collect { user ->
                            if (user != null) {
                                _currentUser.value = user
                                _isLoggedIn.value = true
                            } else {
                                signOutSilently()
                            }
                        }
                    } catch (e: Exception) {
                        // Se falhou ao buscar dados, faz logout
                        signOutSilently()
                    }
                } else {
                    // Usuário não está autenticado
                    clearUserCache()
                }
            } catch (e: Exception) {
                // Se houver erro, considera como não autenticado
                signOutSilently()
            }
        }
    }

    /**
     * Testa se a nova senha está funcionando após reset
     */
    fun testPasswordAfterReset(email: String, newPassword: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                // Força logout completo primeiro
                signOutCompletely()
                
                // Aguarda um momento para garantir que o logout foi processado
                delay(1000)
                
                // Tenta login com a nova senha
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Senha nova está funcionando, faz logout novamente
                            auth.signOut()
                            onResult(true, "Nova senha confirmada! Você pode fazer login normalmente.")
                        } else {
                            val errorMsg = when {
                                task.exception?.message?.contains("wrong-password", ignoreCase = true) == true -> 
                                    "A nova senha ainda não foi sincronizada. Aguarde alguns minutos e tente novamente."
                                task.exception?.message?.contains("invalid-credential", ignoreCase = true) == true -> 
                                    "Credenciais inválidas. Verifique se você está usando a nova senha definida no email."
                                task.exception?.message?.contains("too-many-requests", ignoreCase = true) == true -> 
                                    "Muitas tentativas. Aguarde alguns minutos antes de tentar novamente."
                                else -> 
                                    "Erro ao testar nova senha: ${task.exception?.message}"
                            }
                            onResult(false, errorMsg)
                        }
                    }
            } catch (e: Exception) {
                onResult(false, "Erro ao testar nova senha: ${e.message}")
            }
        }
    }

    /**
     * Faz logout completo incluindo clear de todas as preferências
     */
    private suspend fun signOutCompletely() {
        try {
            // Firebase Auth logout
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
            auth.signOut()
            
            // Google Sign-in logout
            viewModelScope.launch {
                try {
                    googleSignInHelper.signOut()
                } catch (e: Exception) {
                    // Ignora erros no logout do Google
                }
            }
            
            // Limpa completamente o cache local
            clearUserCache()
            
            // Aguarda para garantir que tudo foi limpo
            delay(500)
            
        } catch (e: Exception) {
            // Ignora erros no logout completo
        }
    }

    /**
     * Diagnostica problemas de configuração do Google Sign-In
     */
    fun diagnoseGoogleSignIn(): String {
        return try {
            googleSignInHelper.checkConfiguration()
        } catch (e: Exception) {
            "Erro no diagnóstico: ${e.message}"
        }
    }
}

/**
 * Data class para armazenar dados básicos do usuário do Google
 */
data class GoogleUserData(
    val nome: String,
    val sobrenome: String,
    val email: String
)
