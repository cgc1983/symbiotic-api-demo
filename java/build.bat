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
    echo   action-pose  - Run make action pose demo
    echo   fix-hand     - Run fix hand demo
    echo   pose-video   - Run generate pose video demo
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

if "%1"=="action-pose" (
    echo Running make action pose demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoMakeActionPose"
    goto :eof
)

if "%1"=="fix-hand" (
    echo Running fix hand demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoFixHand"
    goto :eof
)

if "%1"=="pose-video" (
    echo Running generate pose video demo...
    mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoGeneratePoseVideo"
    goto :eof
)

echo Unknown command: %1
echo Use 'build.bat' without arguments to see available commands. 