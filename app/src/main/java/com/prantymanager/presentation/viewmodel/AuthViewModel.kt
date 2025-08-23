package com.prantymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prantymanager.auth.GoogleSignInHelper
import com.prantymanager.domain.entity.User
import com.prantymanager.domain.usecase.auth.GetCurrentUserUseCase
import com.prantymanager.domain.usecase.auth.LoginUseCase
import com.prantymanager.domain.usecase.auth.GoogleSignInUseCase
import com.prantymanager.domain.usecase.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
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

            val result = loginUseCase(login, password)
            
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _loginSuccess.value = true
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "Erro desconhecido"
                }
            )
            
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
                    _errorMessage.value = exception.message ?: "Erro no login com Google"
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
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    _loginSuccess.value = true
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "Erro no login com Google"
                }
            )
            
            _isLoading.value = false
        }
    }

    fun register(user: User, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = registerUserUseCase(user, password, confirmPassword)
            
            result.fold(
                onSuccess = { registeredUser ->
                    _currentUser.value = registeredUser
                    _isLoggedIn.value = true
                    _loginSuccess.value = true
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "Erro no cadastro"
                }
            )
            
            _isLoading.value = false
        }
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

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                // TODO: Implementar reset password com Firebase Auth
                // Por enquanto, simulamos o envio do e-mail
                kotlinx.coroutines.delay(2000) // Simula chamada à API
                _successMessage.value = "E-mail de recuperação enviado com sucesso! Verifique sua caixa de entrada."
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao enviar e-mail de recuperação. Tente novamente."
            }

            _isLoading.value = false
        }
    }
}
