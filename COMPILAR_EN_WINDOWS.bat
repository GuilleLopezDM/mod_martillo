@echo off
setlocal
cd /d "%~dp0"
echo.
echo Martillo Reparador 26.3 - Fabric Loader 0.19.5
echo Para compilar necesitas JDK 25 y Gradle 9.7.1 instalados.
echo.
where java >nul 2>&1
if errorlevel 1 (
  echo ERROR: Java no esta instalado en PATH.
  pause
  exit /b 1
)
java -version
echo.
where gradle >nul 2>&1
if errorlevel 1 (
  echo ERROR: Gradle no esta instalado en PATH.
  echo Usa el metodo GitHub Actions del archivo LEEME_COMPILACION.txt.
  pause
  exit /b 1
)
gradle --no-daemon build
if errorlevel 1 (
  echo ERROR: No se pudo compilar. Revisa los mensajes de error.
  pause
  exit /b 1
)
echo.
echo Compilacion terminada. Busca el JAR en build\libspause
