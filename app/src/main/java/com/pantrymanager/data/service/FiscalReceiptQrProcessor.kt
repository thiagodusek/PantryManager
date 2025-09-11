package com.pantrymanager.data.service

import com.pantrymanager.data.dto.SefazResponseDto
import com.pantrymanager.data.dto.SefazFiscalReceiptDto
import com.pantrymanager.data.dto.toDomain
import com.pantrymanager.domain.entity.FiscalReceipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resultado do processamento do QR Code
 */
sealed class QrCodeProcessResult {
    data class Success(val fiscalReceipt: FiscalReceipt) : QrCodeProcessResult()
    data class Error(val message: String) : QrCodeProcessResult()
    object InvalidQrCode : QrCodeProcessResult()
}

/**
 * Serviço para processamento de QR Codes de cupons fiscais
 */
@Singleton
class FiscalReceiptQrProcessor @Inject constructor(
    private val sefazService: SefazService
) {
    
    companion object {
        // Padrão para chave de acesso NFC-e (44 dígitos)
        private val NFCE_ACCESS_KEY_PATTERN = Pattern.compile("\\d{44}")
        
        // Padrão para URL de NFC-e
        private val NFCE_URL_PATTERN = Pattern.compile("https?://.*\\.fazenda\\.[a-z]{2}\\.gov\\.br.*")
        
        // Padrão para CF-e SAT (começa com números específicos)
        private val CFE_SAT_PATTERN = Pattern.compile("^[0-9]{6}\\|[0-9]{14}\\|[0-9]{14}\\|.*")
    }
    
    /**
     * Processa um QR Code e retorna os dados do cupom fiscal
     */
    suspend fun processQrCode(qrCodeData: String): QrCodeProcessResult = withContext(Dispatchers.IO) {
        try {
            when (detectQrCodeType(qrCodeData)) {
                QrCodeType.NFCE_ACCESS_KEY -> processNfceAccessKey(qrCodeData)
                QrCodeType.NFCE_URL -> processNfceUrl(qrCodeData)
                QrCodeType.CFE_SAT -> processCfeSat(qrCodeData)
                QrCodeType.UNKNOWN -> QrCodeProcessResult.InvalidQrCode
            }
        } catch (e: Exception) {
            QrCodeProcessResult.Error("Erro ao processar QR Code: ${e.message}")
        }
    }
    
    /**
     * Detecta o tipo de QR Code do cupom fiscal
     */
    private fun detectQrCodeType(qrCodeData: String): QrCodeType {
        return when {
            // NFC-e por chave de acesso (44 dígitos)
            NFCE_ACCESS_KEY_PATTERN.matcher(qrCodeData.replace("\\D".toRegex(), "")).matches() -> {
                QrCodeType.NFCE_ACCESS_KEY
            }
            
            // NFC-e por URL
            NFCE_URL_PATTERN.matcher(qrCodeData).matches() -> {
                QrCodeType.NFCE_URL
            }
            
            // CF-e SAT
            CFE_SAT_PATTERN.matcher(qrCodeData).matches() -> {
                QrCodeType.CFE_SAT
            }
            
            // Tentar detectar chave de acesso dentro de URL ou texto
            qrCodeData.contains("chNFe=") -> {
                QrCodeType.NFCE_URL
            }
            
            else -> QrCodeType.UNKNOWN
        }
    }
    
    /**
     * Processa NFC-e pela chave de acesso
     */
    private suspend fun processNfceAccessKey(qrCodeData: String): QrCodeProcessResult {
        val accessKey = qrCodeData.replace("\\D".toRegex(), "")
        
        if (accessKey.length != 44) {
            return QrCodeProcessResult.InvalidQrCode
        }
        
        return try {
            val response = sefazService.getNfceByAccessKey(accessKey)
            
            if (response.isSuccessful && response.body() != null) {
                val sefazResponse = response.body()!!
                
                if (sefazResponse.status == "success" && sefazResponse.data != null) {
                    val fiscalReceipt = sefazResponse.data.toDomain()
                    QrCodeProcessResult.Success(fiscalReceipt)
                } else {
                    QrCodeProcessResult.Error(sefazResponse.message.ifEmpty { "Cupom não encontrado" })
                }
            } else {
                QrCodeProcessResult.Error("Erro na consulta: ${response.code()}")
            }
        } catch (e: Exception) {
            QrCodeProcessResult.Error("Erro na comunicação com a Sefaz: ${e.message}")
        }
    }
    
    /**
     * Processa NFC-e pela URL do QR Code
     */
    private suspend fun processNfceUrl(qrCodeData: String): QrCodeProcessResult {
        return try {
            // Extrair chave de acesso da URL se possível
            val accessKey = extractAccessKeyFromUrl(qrCodeData)
            
            if (accessKey != null && accessKey.length == 44) {
                processNfceAccessKey(accessKey)
            } else {
                // Usar a URL completa
                val response = sefazService.getNfceByQrCodeUrl(qrCodeData)
                
                if (response.isSuccessful && response.body() != null) {
                    val sefazResponse = response.body()!!
                    
                    if (sefazResponse.status == "success" && sefazResponse.data != null) {
                        val fiscalReceipt = sefazResponse.data.toDomain()
                        QrCodeProcessResult.Success(fiscalReceipt)
                    } else {
                        QrCodeProcessResult.Error(sefazResponse.message.ifEmpty { "Cupom não encontrado" })
                    }
                } else {
                    QrCodeProcessResult.Error("Erro na consulta: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            QrCodeProcessResult.Error("Erro ao processar URL da NFC-e: ${e.message}")
        }
    }
    
    /**
     * Processa CF-e SAT
     */
    private suspend fun processCfeSat(qrCodeData: String): QrCodeProcessResult {
        return try {
            val response = sefazService.getCfeBySatCode(qrCodeData)
            
            if (response.isSuccessful && response.body() != null) {
                val sefazResponse = response.body()!!
                
                if (sefazResponse.status == "success" && sefazResponse.data != null) {
                    val fiscalReceipt = sefazResponse.data.toDomain()
                    QrCodeProcessResult.Success(fiscalReceipt)
                } else {
                    QrCodeProcessResult.Error(sefazResponse.message.ifEmpty { "Cupom SAT não encontrado" })
                }
            } else {
                QrCodeProcessResult.Error("Erro na consulta SAT: ${response.code()}")
            }
        } catch (e: Exception) {
            QrCodeProcessResult.Error("Erro ao processar CF-e SAT: ${e.message}")
        }
    }
    
    /**
     * Extrai chave de acesso de uma URL de NFC-e
     */
    private fun extractAccessKeyFromUrl(url: String): String? {
        return try {
            val urlObj = URL(url)
            val query = urlObj.query ?: return null
            
            // Procurar parâmetro chNFe
            val params = query.split("&").associate { param ->
                val (key, value) = param.split("=", limit = 2)
                key to value
            }
            
            params["chNFe"]?.takeIf { it.length == 44 }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Valida se um QR Code é potencialmente um cupom fiscal
     */
    fun isValidFiscalReceiptQrCode(qrCodeData: String): Boolean {
        return detectQrCodeType(qrCodeData) != QrCodeType.UNKNOWN
    }
    
    /**
     * Tipos de QR Code suportados
     */
    private enum class QrCodeType {
        NFCE_ACCESS_KEY,    // Chave de acesso NFC-e (44 dígitos)
        NFCE_URL,           // URL completa da NFC-e
        CFE_SAT,            // CF-e SAT
        UNKNOWN             // Tipo não identificado
    }
}
