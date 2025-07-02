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
 * Demo for fixing hand positioning
 */
public class DemoFixHand {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoFixHand.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            logger.info("Starting fix hand demo...");
            
            // Upload required files
            String characterImagePath = Paths.get(Config.IMAGE_PATH, "pic_1.png").toString();
            logger.info("Uploading character image: {}", characterImagePath);
            String characterImageFilename = Utils.simpleUploadFileToS3(characterImagePath);
            
            // Create fix hand request
            Models.FixHand fixHandRequest = new Models.FixHand(
                characterImageFilename,
                "3",  // lora_type: 3:Anime
                "A cute anime character with properly positioned hands",
                "2"   // pose_type: 2:Walk to the left
            );
            
            logger.info("Submitting fix hand request...");
            HttpResponse response = Utils.makeAuthenticatedRequest(
                "POST", "/api/v1/fix-hand", fixHandRequest, null
            );
            
            if (response.getStatusLine().getStatusCode() != 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to submit fix hand request, status code: {}", response.getStatusLine().getStatusCode());
                logger.error("Response content: {}", responseBody);
                throw new RuntimeException("Failed to submit fix hand request");
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            Models.PromptResponse promptResponse = objectMapper.readValue(responseBody, Models.PromptResponse.class);
            
            if (promptResponse.code != 0) {
                logger.error("API returned error: {}", promptResponse.msg);
                throw new RuntimeException("API returned error: " + promptResponse.msg);
            }
            
            String promptId = promptResponse.data.promptId;
            logger.info("Fix hand task submitted successfully, prompt ID: {}", promptId);
            
            // Wait for task completion
            logger.info("Waiting for task completion...");
            Models.StatusTaskResponseData completedTask = Utils.waitForTaskCompletion(promptId, 30, 5000);
            
            logger.info("Fix hand task completed successfully!");
            logger.info("Task ID: {}", completedTask.promptId);
            logger.info("Execution time: {} ms", completedTask.executionStart);
            
        } catch (Exception e) {
            logger.error("Fix hand demo failed", e);
            System.exit(1);
        }
    }
} 