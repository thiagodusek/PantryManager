package com.pantrymanager.data.service

import com.pantrymanager.data.dto.SefazResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface para comunicação com APIs da Sefaz e serviços de consulta de cupons fiscais
 */
interface SefazService {
    
    /**
     * Consulta NFC-e pela chave de acesso
     * @param accessKey Chave de acesso da NFC-e (44 dígitos)
     */
    @GET("nfce/{accessKey}")
    suspend fun getNfceByAccessKey(
        @Path("accessKey") accessKey: String
    ): Response<SefazResponseDto>
    
    /**
     * Consulta CF-e SAT pelo código QR
     * @param qrCode Código QR completo do CF-e SAT
     */
    @GET("cfce/consulta")
    suspend fun getCfeBySatCode(
        @Query("qrCode") qrCode: String
    ): Response<SefazResponseDto>
    
    /**
     * Consulta NFC-e por URL do QR Code
     * @param qrCodeUrl URL completa do QR Code da NFC-e
     */
    @GET("nfce/url")
    suspend fun getNfceByQrCodeUrl(
        @Query("url") qrCodeUrl: String
    ): Response<SefazResponseDto>
}
