@echo off
echo ================================
echo   PantryManager - Test Google Sign-In
echo ================================
echo.

cd /d "C:\Projetos\Faculdade\TCC1\PantryManager"

echo 1. Building APK...
call gradlew assembleDebug
if %errorlevel% neq 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo 2. APK Generated Successfully!
echo Location: app\build\outputs\apk\debug\app-debug.apk

echo.
echo 3. Current Configuration:
echo - CLIENT_ID: 513859885488-7vqhetijef1amff36nuhquvribj61krt.apps.googleusercontent.com
echo - Package: com.pantrymanager
echo - SHA-1: 36:4C:C4:F8:B3:B9:8E:2B:0C:6D:1A:23:08:BB:91:C0:D0:80:FA:F5

echo.
echo 4. Next Steps to Fix Google Sign-In:
echo    a) Configure SHA-1 in Firebase Console
echo    b) Download updated google-services.json
echo    c) Test on device

echo.
echo 5. Firebase Console Links:
echo    General: https://console.firebase.google.com/project/pantrymanager-79f09/settings/general
echo    Auth: https://console.firebase.google.com/project/pantrymanager-79f09/authentication/providers

echo.
echo READY TO TEST: Install the APK and test Google Sign-In!
pause
