@echo off
setlocal

echo ==========================================
echo GERADOR DE APK - PANTRYMANAGER
echo ==========================================

echo.
echo 1. Configurando ambiente Java 19...
set JAVA_HOME=C:\Program Files\Java\jdk-19
set PATH=%JAVA_HOME%\bin;%PATH%

echo Verificando versao do Java:
java -version

echo.
echo 2. Configurando variaveis do Gradle...
set GRADLE_OPTS=-Xmx4g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError

echo.
echo 3. Limpando projeto...
call gradlew clean --no-daemon --quiet

echo.
echo 4. Verificando configuracoes...
echo - Java Home: %JAVA_HOME%
echo - Gradle Opts: %GRADLE_OPTS%

echo.
echo 5. Gerando APK de Debug...
echo Por favor, aguarde. Este processo pode demorar alguns minutos...

call gradlew assembleDebug --no-daemon --stacktrace --info

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ==========================================
    echo APK GERADO COM SUCESSO!
    echo ==========================================
    echo.
    echo Localizacao: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Verificando arquivo...
    if exist "app\build\outputs\apk\debug\app-debug.apk" (
        echo ✓ APK encontrado!
        echo Tamanho:
        dir "app\build\outputs\apk\debug\app-debug.apk" | findstr "app-debug.apk"
    ) else (
        echo ✗ APK nao encontrado no local esperado
        echo Verificando outros locais...
        dir /s app\build\outputs\*.apk 2>nul
    )
) else (
    echo.
    echo ==========================================
    echo ERRO NA GERACAO DO APK!
    echo ==========================================
    echo.
    echo Verifique os logs acima para detalhes do erro.
    echo Codigo de erro: %ERRORLEVEL%
)

echo.
echo Pressione qualquer tecla para continuar...
pause >nul
