package com.pantrymanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pantrymanager.domain.entity.Address
import com.pantrymanager.domain.usecase.address.GetAddressByCepUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getAddressByCepUseCase: GetAddressByCepUseCase
) : ViewModel() {

    private val _isLoadingCep = MutableStateFlow(false)
    val isLoadingCep: StateFlow<Boolean> = _isLoadingCep

    private val _addressData = MutableStateFlow<Address?>(null)
    val addressData: StateFlow<Address?> = _addressData

    private val _cepError = MutableStateFlow<String?>(null)
    val cepError: StateFlow<String?> = _cepError

    fun searchAddressByCep(cep: String) {
        if (cep.length < 8) return

        viewModelScope.launch {
            _isLoadingCep.value = true
            _cepError.value = null

            try {
                val address = getAddressByCepUseCase.execute(cep)
                if (address != null) {
                    _addressData.value = address
                } else {
                    _cepError.value = "CEP não encontrado"
                }
            } catch (e: Exception) {
                _cepError.value = "Erro ao buscar CEP"
            } finally {
                _isLoadingCep.value = false
            }
        }
    }

    fun clearAddressData() {
        _addressData.value = null
        _cepError.value = null
    }
}
