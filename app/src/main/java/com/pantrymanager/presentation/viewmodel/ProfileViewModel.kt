package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.domain.entity.Address
import com.pantrymanager.domain.entity.User
import com.pantrymanager.domain.usecase.address.GetAddressByCepUseCase
import com.pantrymanager.domain.usecase.user.GetCurrentUserUseCase
import com.pantrymanager.domain.usecase.user.SaveUserUseCase
import com.pantrymanager.domain.usecase.user.ValidateUserCredentialsUseCase
import com.pantrymanager.utils.CepUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getAddressByCepUseCase: GetAddressByCepUseCase,
    private val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Estados individuais para facilitar o binding
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome.asStateFlow()

    private val _sobrenome = MutableStateFlow("")
    val sobrenome: StateFlow<String> = _sobrenome.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _cpf = MutableStateFlow("")
    val cpf: StateFlow<String> = _cpf.asStateFlow()

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _endereco = MutableStateFlow("")
    val endereco: StateFlow<String> = _endereco.asStateFlow()

    private val _numero = MutableStateFlow("")
    val numero: StateFlow<String> = _numero.asStateFlow()

    private val _complemento = MutableStateFlow("")
    val complemento: StateFlow<String> = _complemento.asStateFlow()

    private val _cep = MutableStateFlow("")
    val cep: StateFlow<String> = _cep.asStateFlow()

    private val _cidade = MutableStateFlow("")
    val cidade: StateFlow<String> = _cidade.asStateFlow()

    private val _estado = MutableStateFlow("")
    val estado: StateFlow<String> = _estado.asStateFlow()

    private val _nfePermissions = MutableStateFlow(false)
    val nfePermissions: StateFlow<Boolean> = _nfePermissions.asStateFlow()

    private val _searchRadius = MutableStateFlow("5.0")
    val searchRadius: StateFlow<String> = _searchRadius.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingCep = MutableStateFlow(false)
    val isLoadingCep: StateFlow<Boolean> = _isLoadingCep.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _cepError = MutableStateFlow<String?>(null)
    val cepError: StateFlow<String?> = _cepError.asStateFlow()

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    private var currentUser: User? = null

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getCurrentUserUseCase()
                .onSuccess { user ->
                    user?.let {
                        currentUser = it
                        populateFields(it)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isDataLoaded = true
                        )
                    } ?: run {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Usuário não encontrado"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar dados: ${error.message}"
                    )
                }
        }
    }

    private fun populateFields(user: User) {
        _nome.value = user.nome
        _sobrenome.value = user.sobrenome
        _email.value = user.email
        _cpf.value = user.cpf
        _login.value = user.login
        _endereco.value = user.endereco.endereco
        _numero.value = user.endereco.numero
        _complemento.value = user.endereco.complemento ?: ""
        _cep.value = user.endereco.cep
        _cidade.value = user.endereco.cidade
        _estado.value = user.endereco.estado
        _nfePermissions.value = user.nfePermissions
        _searchRadius.value = user.searchRadius.toString()
    }

    // Métodos para atualizar campos individuais
    fun updateNome(value: String) {
        _nome.value = value
        clearError()
    }

    fun updateSobrenome(value: String) {
        _sobrenome.value = value
        clearError()
    }

    fun updateEmail(value: String) {
        _email.value = value
        clearError()
    }

    fun updateEndereco(value: String) {
        _endereco.value = value
        clearError()
    }

    fun updateNumero(value: String) {
        _numero.value = value
        clearError()
    }

    fun updateComplemento(value: String) {
        _complemento.value = value
        clearError()
    }

    fun updateCep(value: String) {
        // Permite apenas números e limita a 8 dígitos
        val digitsOnly = value.filter { it.isDigit() }.take(8)
        _cep.value = digitsOnly
        _cepError.value = null
        
        // Busca automaticamente quando tiver 8 dígitos
        if (digitsOnly.length == 8) {
            searchAddressByCep(digitsOnly)
        } else if (digitsOnly.length < 8) {
            clearAddressData()
        }
    }

    fun updateCidade(value: String) {
        _cidade.value = value
        clearError()
    }

    fun updateEstado(value: String) {
        _estado.value = value
        clearError()
    }

    fun updateNfePermissions(value: Boolean) {
        _nfePermissions.value = value
    }

    fun updateSearchRadius(value: String) {
        _searchRadius.value = value
        clearError()
    }

    fun searchAddressByCep(cep: String) {
        viewModelScope.launch {
            _isLoadingCep.value = true
            _cepError.value = null
            
            try {
                val address = getAddressByCepUseCase.execute(cep)
                _isLoadingCep.value = false
                
                if (address != null) {
                    _endereco.value = address.endereco
                    _cidade.value = address.cidade
                    _estado.value = address.estado
                    _complemento.value = address.complemento ?: ""
                    _cep.value = CepUtils.formatCep(address.cep)
                } else {
                    _cepError.value = "CEP não encontrado"
                }
            } catch (error: Exception) {
                _isLoadingCep.value = false
                _cepError.value = "Erro ao buscar CEP: ${error.message}"
            }
        }
    }

    fun clearAddressData() {
        // Não limpa os dados automaticamente na tela de perfil
        // apenas limpa o erro de CEP
        _cepError.value = null
    }

    fun updateProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Valida apenas se o email foi alterado
                val emailChanged = _email.value != currentUser?.email
                
                if (emailChanged) {
                    // Valida apenas o email se foi alterado (CPF e login não podem ser alterados)
                    val emailResult = validateUserCredentialsUseCase.isEmailInUse(_email.value)
                    
                    emailResult.onSuccess { isEmailInUse ->
                        if (isEmailInUse) {
                            _isLoading.value = false
                            _errorMessage.value = "Este email já está em uso"
                            return@launch
                        }
                        // Se email está disponível, continua com a atualização
                        proceedWithUpdate()
                    }.onFailure { error ->
                        _isLoading.value = false
                        _errorMessage.value = "Erro ao validar email: ${error.message}"
                        return@launch
                    }
                } else {
                    // Se email não foi alterado, procede diretamente com a atualização
                    proceedWithUpdate()
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Erro inesperado: ${e.message}"
            }
        }
    }

    private suspend fun proceedWithUpdate() {
        // Cria o usuário atualizado
        val updatedUser = User(
            nome = _nome.value,
            sobrenome = _sobrenome.value,
            email = _email.value,
            cpf = _cpf.value, // CPF não pode ser alterado
            endereco = Address(
                endereco = _endereco.value,
                numero = _numero.value,
                complemento = _complemento.value.ifBlank { null },
                cep = CepUtils.cleanCep(_cep.value),
                cidade = _cidade.value,
                estado = _estado.value
            ),
            login = _login.value, // Login não pode ser alterado
            nfePermissions = _nfePermissions.value,
            searchRadius = _searchRadius.value.toDoubleOrNull() ?: 5.0
        )

        saveUserUseCase(updatedUser)
            .onSuccess {
                currentUser = updatedUser
                _isLoading.value = false
                _updateSuccess.value = true
            }
            .onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = "Erro ao atualizar perfil: ${error.message}"
            }
    }

    fun clearError() {
        _errorMessage.value = null
        _cepError.value = null
    }

    fun clearUpdateSuccess() {
        _updateSuccess.value = false
    }

    fun isFormValid(): Boolean {
        return _nome.value.isNotBlank() &&
                _sobrenome.value.isNotBlank() &&
                _email.value.isNotBlank() &&
                _endereco.value.isNotBlank() &&
                _numero.value.isNotBlank() &&
                CepUtils.cleanCep(_cep.value).length == 8 &&
                _cidade.value.isNotBlank() &&
                _estado.value.isNotBlank() &&
                _searchRadius.value.toDoubleOrNull() != null
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isLoadingCep: Boolean = false,
    val isDataLoaded: Boolean = false,
    val errorMessage: String? = null,
    val cepError: String? = null,
    val updateSuccess: Boolean = false
)
