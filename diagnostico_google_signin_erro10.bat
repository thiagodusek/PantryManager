@echo off
color 0C
echo ========================================
echo   DIAGNOSTICO GOOGLE SIGN-IN ERRO 10
echo ========================================
echo.

echo 🔍 PROBLEMA IDENTIFICADO: SHA-1 DESATUALIZADO
echo.

echo ❌ SHA-1 atual do keystore: D4:7E:38:82:29:D6:BB:6A:9C:9C:19:D4:9B:4C:AF:B7:94:DC:F5:EC
echo ❌ SHA-1 no Firebase: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo.

echo 🚨 OS HASHES NÃO COINCIDEM - POR ISSO O ERRO 10!
echo.

echo ========================================
echo   SOLUÇÕES DISPONÍVEIS
echo ========================================
echo.

echo 🔧 OPÇÃO 1: ATUALIZAR SHA-1 NO FIREBASE (RECOMENDADO)
echo    1. Acesse: https://console.firebase.google.com/
echo    2. Selecione projeto: pantrymanager-79f09
echo    3. Vá em: Configurações do Projeto ^> Seus Apps ^> Android
echo    4. Adicione novo SHA-1: D4:7E:38:82:29:D6:BB:6A:9C:9C:19:D4:9B:4C:AF:B7:94:DC:F5:EC
echo    5. Baixe novo google-services.json
echo    6. Substitua o arquivo atual
echo.

echo 🔧 OPÇÃO 2: USAR KEYSTORE ANTERIOR (SE DISPONÍVEL)
echo    1. Localizar keystore com SHA-1: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo    2. Copiar para: %%USERPROFILE%%\.android\debug.keystore
echo    3. Fazer rebuild do projeto
echo.

echo ========================================
echo   INFORMAÇÕES TÉCNICAS
echo ========================================
echo.

echo 📋 PROJECT ID: pantrymanager-79f09
echo 📋 PACKAGE NAME: com.pantrymanager
echo 📋 CLIENT ID: 191359104593-9ma87em4m41ltfv6jbdhpb50gkr60hfh.apps.googleusercontent.com
echo 📋 SHA-1 ATUAL: D4:7E:38:82:29:D6:BB:6A:9C:9C:19:D4:9B:4C:AF:B7:94:DC:F5:EC
echo 📋 SHA-1 CONFIGURADO: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5
echo.

echo ⚠️ CAUSA DO ERRO: Keystore foi regenerado/alterado
echo ⚠️ ERRO GOOGLE SIGN-IN 10: SHA-1 fingerprint mismatch
echo.

echo ========================================
echo   PRÓXIMOS PASSOS
echo ========================================
echo.

echo 1️⃣ ATUALIZE o SHA-1 no Firebase Console
echo 2️⃣ BAIXE o novo google-services.json
echo 3️⃣ SUBSTITUA o arquivo no projeto
echo 4️⃣ FAÇA rebuild: gradlew clean assembleDebug
echo 5️⃣ TESTE o Google Sign-In novamente
echo.

echo 🎯 APÓS CORREÇÃO: Google Sign-In funcionará perfeitamente!
echo.

pause
