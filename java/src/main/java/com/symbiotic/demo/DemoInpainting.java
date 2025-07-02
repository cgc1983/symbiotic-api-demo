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
import java.util.HashMap;
import java.util.Map;

/**
 * Demo for inpainting functionality
 */
public class DemoInpainting {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoInpainting.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            logger.info("Starting inpainting demo...");
            
            // Upload required files
            String maskFilePath = Paths.get(Config.IMAGE_PATH, "inpainting_mask.png").toString();
            logger.info("Uploading mask file: {}", maskFilePath);
            String maskFilename = Utils.simpleUploadFileToS3(maskFilePath);
            
            // Create inpainting request
            Models.Inpainting inpaintingRequest = new Models.Inpainting(
                "Fill the masked area with a beautiful landscape",
                maskFilename
            );
            
            logger.info("Submitting inpainting request...");
            HttpResponse response = Utils.makeAuthenticatedRequest(
                "POST", "/api/v1/inpainting", inpaintingRequest, null
            );
            
            if (response.getStatusLine().getStatusCode() != 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to submit inpainting request, status code: {}", response.getStatusLine().getStatusCode());
                logger.error("Response content: {}", responseBody);
                throw new RuntimeException("Failed to submit inpainting request");
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            Models.PromptResponse promptResponse = objectMapper.readValue(responseBody, Models.PromptResponse.class);
            
            if (promptResponse.code != 0) {
                logger.error("API returned error: {}", promptResponse.msg);
                throw new RuntimeException("API returned error: " + promptResponse.msg);
            }
            
            String promptId = promptResponse.data.promptId;
            logger.info("Inpainting task submitted successfully, prompt ID: {}", promptId);
            
            // Wait for task completion
            logger.info("Waiting for task completion...");
            Models.StatusTaskResponseData completedTask = Utils.waitForTaskCompletion(promptId, 130, 5000);

            Models.HistoryResponse history_model = Utils.getTaskHistory("4");

            if (history_model.code != 0) {
                logger.error("Failed to get history, error message: {}", history_model.msg);
                return;
            }

            for (var item : history_model.data.items) {
                logger.info("History ID: {}", item.id);
                for (var task : item.tasks) {
                    logger.info("Task ID: {}", task.id);
                    logger.info("Task status: {}", task.complete);
                    logger.info("Task start time: {}", task.executionStart);
                    logger.info("Task update time: {}", task.updateAt);
                    logger.info("Task creation time: {}", task.createAt);
                    for (var output : task.outPuts) {
                        logger.info("Download link: {}{}", Config.S3_BUCKET_BASE_URL, output);
                    }
                }
            }

            logger.info("Inpainting task completed successfully!");
            logger.info("Task ID: {}", completedTask.promptId);
            logger.info("Execution time: {} ms", completedTask.executionStart);
            
        } catch (Exception e) {
            logger.error("Inpainting demo failed", e);
            System.exit(1);
        }
    }
} 