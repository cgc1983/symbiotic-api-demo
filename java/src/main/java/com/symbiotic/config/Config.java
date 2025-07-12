package com.symbiotic.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration constants and paths for the Symbiotic API demo
 */
public class Config {
    
    // API Configuration
    public static final String API_HOST = "http://192.168.1.107:8000";//"http://49.233.159.47:9999";
    
    // Secret credentials
    public static final String SECRET_ID = "t1";
    public static final String SECRET_KEY = "test_secret_key_001";
    
    // S3 bucket base URL
    public static final String S3_BUCKET_BASE_URL = "https://comfyprod.s3.ap-northeast-1.amazonaws.com/temp/";
    
    // Paths
    public static final String ROOT_PATH;
    public static final String PARENT_PATH;
    public static final String IMAGE_PATH;
    public static final String VIDEO_PATH;
    
    static {
        // Get current working directory
        String workingDir = System.getProperty("user.dir");
        ROOT_PATH = workingDir;
        PARENT_PATH = new File(workingDir).getParent();
        IMAGE_PATH = Paths.get(PARENT_PATH, "images").toString();
        VIDEO_PATH = Paths.get(PARENT_PATH, "videos").toString();
    }
} 