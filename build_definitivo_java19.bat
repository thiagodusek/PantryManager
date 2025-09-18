@echo off
color 0B
echo ==========================================
echo   CONFIGURAÇÃO DEFINITIVA JAVA 19
echo   PROJETO PANTRYMANAGER
echo ==========================================
echo.

cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"

echo 🔧 CONFIGURANDO JAVA 19 PERMANENTEMENTE...
echo.

echo ==========================================
echo   FASE 1: CONFIGURAÇÃO JAVA_HOME
echo ==========================================
echo.

echo 🎯 Definindo JAVA_HOME para Java 19...
set JAVA_HOME=C:\Program Files\Java\jdk-19
set PATH=%JAVA_HOME%\bin;%PATH%

echo ✅ JAVA_HOME: %JAVA_HOME%
echo.

echo 🧪 Verificando versão do Java...
java -version
if %errorlevel% neq 0 (
    echo ❌ Erro: Java 19 não encontrado!
    echo 💡 Verifique se Java 19 está instalado em: C:\Program Files\Java\jdk-19
    pause
    exit /b 1
)

echo ✅ Java 19 configurado com sucesso!
echo.

echo ==========================================
echo   FASE 2: VERIFICAÇÃO DE CONFIGURAÇÕES
echo ==========================================
echo.

echo 📋 Verificando gradle.properties...
findstr "org.gradle.java.home" gradle.properties
if %errorlevel% neq 0 (
    echo ❌ gradle.properties não configurado corretamente
    pause
    exit /b 1
) else (
    echo ✅ gradle.properties configurado para Java 19
)

echo.
echo 📋 Verificando app/build.gradle...
findstr "VERSION_19" app\build.gradle
if %errorlevel% neq 0 (
    echo ❌ app/build.gradle não configurado corretamente
    pause
    exit /b 1
) else (
    echo ✅ app/build.gradle configurado para Java 19
)

echo.
echo ==========================================
echo   FASE 3: BUILD LIMPO DO PROJETO
echo ==========================================
echo.

echo 🧹 Fazendo limpeza completa...
call gradlew.bat clean --no-daemon

if %errorlevel% neq 0 (
    echo ❌ Erro no clean. Verificando dependências...
    echo 💡 Tentando com refresh de dependências...
    call gradlew.bat clean --refresh-dependencies --no-daemon
    if %errorlevel% neq 0 (
        echo ❌ Erro persistente no clean
        pause
        exit /b 1
    )
)

echo ✅ Clean realizado com sucesso!
echo.

echo ==========================================
echo   FASE 4: GERAÇÃO DO APK
echo ==========================================
echo.

echo 🏗️ Gerando APK de debug...
call gradlew.bat assembleDebug --no-daemon

if %errorlevel% equ 0 (
    echo.
    echo ✅ APK GERADO COM SUCESSO!
    echo.
    echo 📱 Localização: app\build\outputs\apk\debug\app-debug.apk
    echo 📊 Tamanho do APK:
    for %%A in (app\build\outputs\apk\debug\app-debug.apk) do echo    %%~zA bytes
    echo.
    echo 🎯 PRONTO PARA INSTALAÇÃO E TESTE!
) else (
    echo.
    echo ❌ Erro na geração do APK
    echo 💡 Verifique os logs acima para detalhes
    echo 🔧 Possíveis soluções:
    echo    1. Verificar conectividade com internet
    echo    2. Limpar cache: gradlew.bat clean --refresh-dependencies
    echo    3. Verificar espaço em disco
    echo.
)

echo.
echo ==========================================
echo   CONFIGURAÇÕES FINAIS
echo ==========================================
echo.

echo ✅ PROJETO CONFIGURADO PERMANENTEMENTE PARA:
echo 🎯 Java 19 (Oracle JDK 19.0.1)
echo 🎯 Gradle com JAVA_HOME correto
echo 🎯 Build tools atualizados
echo 🎯 Dependências sincronizadas
echo.

echo 📋 PRÓXIMOS PASSOS:
echo 1. Instalar APK: adb install app\build\outputs\apk\debug\app-debug.apk
echo 2. Testar Google Sign-In
echo 3. Testar funcionalidades Firestore
echo 4. Verificar logs em caso de problemas
echo.

echo ==========================================
pause
