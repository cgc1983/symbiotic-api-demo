package com.symbiotic.demo;

import com.symbiotic.config.Config;
import com.symbiotic.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demo for uploading files to S3 using pre-signed URLs
 */
public class DemoUploadFile {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoUploadFile.class);
    
    public static void main(String[] args) {
        try {
            String filePath = Paths.get(Config.IMAGE_PATH, "pic_1.png").toString();
            
            logger.info("Starting file upload demo...");
            logger.info("File path: {}", filePath);
            
            // Upload file to S3 and get the filename
            String filename = Utils.simpleUploadFileToS3(filePath);
            
            logger.info("File upload demo completed successfully!");
            logger.info("Uploaded filename: {}", filename);
            
        } catch (Exception e) {
            logger.error("File upload demo failed", e);
            System.exit(1);
        }
    }
} 