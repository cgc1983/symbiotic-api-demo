@echo off
echo Symbiotic API Demo - Java Version
echo =================================

if "%1"=="" (
    echo Usage: build.bat [upload^|inpainting^|outpainting^|action-pose^|fix-hand^|pose-video^|build^|clean]
    echo.
    echo Commands:
    echo   upload       - Run file upload demo
    echo   inpainting   - Run inpainting demo
    echo   outpainting  - Run outpainting demo
    echo   build        - Build the project
    echo   clean        - Clean the project
    exit /b 1
)

if "%1"=="build" (
    echo Building project...
    mvn clean compile
    goto :eof
)

if "%1"=="clean" (
    echo Cleaning project...
    mvn clean
    goto :eof
)

if "%1"=="upload" (
    echo Running file upload demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoUploadFile"
    goto :eof
)

if "%1"=="inpainting" (
    echo Running inpainting demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoInpainting"
    goto :eof
)

if "%1"=="outpainting" (
    echo Running outpainting demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoOutpainting"
    goto :eof
)

echo Unknown command: %1
echo Use 'build.bat' without arguments to see available commands. 