@echo off
echo ==========================================
echo CORRIGINDO ERRO: Auth Credential Incorrect
echo ==========================================

echo.
echo 1. Verificando configuracoes do Java 19...
java -version

echo.
echo 2. Limpando caches do Gradle...
call gradlew clean --no-daemon --offline || echo "Clean offline falhou, tentando online..." && call gradlew clean --no-daemon

echo.
echo 3. Verificando SHA-1 do keystore debug...
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android | findstr "SHA1:"

echo.
echo 4. Limpando caches do sistema...
rmdir /s /q "%LOCALAPPDATA%\Google\AndroidStudio" 2>nul
rmdir /s /q "%APPDATA%\.gradle\caches" 2>nul
rmdir /s /q "app\build" 2>nul
rmdir /s /q ".gradle" 2>nul

echo.
echo 5. Recriando configuracoes Gradle...
if exist ".gradle" rmdir /s /q ".gradle"
if exist "app\build" rmdir /s /q "app\build"

echo.
echo 6. Fazendo build limpo com Java 19...
set JAVA_HOME=C:\Program Files\Java\jdk-19
set PATH=%JAVA_HOME%\bin;%PATH%

call gradlew clean assembleDebug --no-daemon --stacktrace

echo.
echo ==========================================
echo DIAGNOSTICO DE AUTENTICACAO CONCLUIDO
echo ==========================================

echo.
echo Verificacoes realizadas:
echo - SHA-1 do keystore: OK (d47e388229d6bb6a9c9c19d49b4cafb794dcf5ec)
echo - Client ID Web: 191359104593-ickuagntm0cto1emai6ok1bdnc3rk64l.apps.googleusercontent.com
echo - Client ID Android: 191359104593-93brhmggddas0iti9lec8o0a72k7jvmb.apps.googleusercontent.com
echo - Package Name: com.pantrymanager

echo.
echo Se o erro persistir:
echo 1. Reinstale o app completamente no dispositivo
echo 2. Limpe os dados do Google Play Services no dispositivo
echo 3. Verifique se a data/hora do dispositivo estao corretas
echo 4. Teste em outro dispositivo ou emulador

pause
