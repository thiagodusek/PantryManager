@echo off
REM Gradle wrapper script for Windows

setlocal

REM Set JAVA_HOME if not already set
if not defined JAVA_HOME (
    set JAVA_HOME=C:\Program Files\Java\jdk-19
)

REM Set the path to java executable
set JAVACMD=%JAVA_HOME%\bin\java.exe

REM Check if java exists
if not exist "%JAVACMD%" (
    echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
    echo Please set the JAVA_HOME variable in your environment to match the
    echo location of your Java installation.
    exit /b 1
)

REM Set Gradle options
set DEFAULT_JVM_OPTS=-Xmx2048m -Xms512m
set GRADLE_OPTS=%GRADLE_OPTS% %DEFAULT_JVM_OPTS%

REM Set classpath
set CLASSPATH=%~dp0gradle\wrapper\gradle-wrapper.jar

REM Execute Gradle
"%JAVACMD%" %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
