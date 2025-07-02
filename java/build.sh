#!/bin/bash

echo "Symbiotic API Demo - Java Version"
echo "================================="

if [ $# -eq 0 ]; then
    echo "Usage: ./build.sh [upload|inpainting|outpainting|action-pose|fix-hand|pose-video|build|clean]"
    echo ""
    echo "Commands:"
    echo "  upload       - Run file upload demo"
    echo "  inpainting   - Run inpainting demo"
    echo "  outpainting  - Run outpainting demo"
    echo "  action-pose  - Run make action pose demo"
    echo "  fix-hand     - Run fix hand demo"
    echo "  pose-video   - Run generate pose video demo"
    echo "  build        - Build the project"
    echo "  clean        - Clean the project"
    exit 1
fi

case "$1" in
    "build")
        echo "Building project..."
        mvn clean compile
        ;;
    "clean")
        echo "Cleaning project..."
        mvn clean
        ;;
    "upload")
        echo "Running file upload demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoUploadFile"
        ;;
    "inpainting")
        echo "Running inpainting demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoInpainting"
        ;;
    "outpainting")
        echo "Running outpainting demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoOutpainting"
        ;;
    "action-pose")
        echo "Running make action pose demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoMakeActionPose"
        ;;
    "fix-hand")
        echo "Running fix hand demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoFixHand"
        ;;
    "pose-video")
        echo "Running generate pose video demo..."
        mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoGeneratePoseVideo"
        ;;
    *)
        echo "Unknown command: $1"
        echo "Use './build.sh' without arguments to see available commands."
        exit 1
        ;;
esac 