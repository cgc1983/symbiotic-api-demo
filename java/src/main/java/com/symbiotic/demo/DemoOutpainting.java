package com.symbiotic.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symbiotic.config.Config;
import com.symbiotic.models.Models;
import com.symbiotic.utils.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Demo for outpainting functionality
 */
public class DemoOutpainting {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoOutpainting.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            logger.info("Starting outpainting demo...");
            
            // Upload required files
            String imageFilePath = Paths.get(Config.IMAGE_PATH, "pic_1.png").toString();
            logger.info("Uploading image file: {}", imageFilePath);
            String imageFilename = Utils.simpleUploadFileToS3(imageFilePath);
            
            // Create outpainting request
            Models.Outpainting outpaintingRequest = new Models.Outpainting(
                "Extend the image with a beautiful landscape",
                imageFilename,
                100,  // left
                50,   // top
                100,  // right
                50    // bottom
            );
            
            logger.info("Submitting outpainting request...");
            HttpResponse response = Utils.makeAuthenticatedRequest(
                "POST", "/api/v1/outpainting", outpaintingRequest, null
            );
            
            if (response.getStatusLine().getStatusCode() != 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to submit outpainting request, status code: {}", response.getStatusLine().getStatusCode());
                logger.error("Response content: {}", responseBody);
                throw new RuntimeException("Failed to submit outpainting request");
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            Models.PromptResponse promptResponse = objectMapper.readValue(responseBody, Models.PromptResponse.class);
            
            if (promptResponse.code != 0) {
                logger.error("API returned error: {}", promptResponse.msg);
                throw new RuntimeException("API returned error: " + promptResponse.msg);
            }
            
            String promptId = promptResponse.data.promptId;
            logger.info("Outpainting task submitted successfully, prompt ID: {}", promptId);
            
            // Wait for task completion
            logger.info("Waiting for task completion...");
            Models.StatusTaskResponseData completedTask = Utils.waitForTaskCompletion(promptId, 30, 5000);
            
            logger.info("Outpainting task completed successfully!");
            logger.info("Task ID: {}", completedTask.promptId);
            logger.info("Execution time: {} ms", completedTask.executionStart);
            
        } catch (Exception e) {
            logger.error("Outpainting demo failed", e);
            System.exit(1);
        }
    }
} 