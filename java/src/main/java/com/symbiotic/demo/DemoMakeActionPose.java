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
 * Demo for making action poses
 */
public class DemoMakeActionPose {
    
    private static final Logger logger = LoggerFactory.getLogger(DemoMakeActionPose.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        try {
            logger.info("Starting make action pose demo...");
            
            // Upload required files
            String characterImagePath = Paths.get(Config.IMAGE_PATH, "pic_1.png").toString();
            String maskImagePath = Paths.get(Config.IMAGE_PATH, "pose_mask_0.png").toString();
            
            logger.info("Uploading character image: {}", characterImagePath);
            String characterImageFilename = Utils.simpleUploadFileToS3(characterImagePath);
            
            logger.info("Uploading mask image: {}", maskImagePath);
            String maskImageFilename = Utils.simpleUploadFileToS3(maskImagePath);
            
            // Create make action pose request
            Models.MakeActionPose makeActionPoseRequest = new Models.MakeActionPose(
                characterImageFilename,
                "3",  // lora_type: 3:Anime
                "A cute anime character in a walking pose",
                "2",  // pose_type: 2:Walk to the left
                maskImageFilename
            );
            
            logger.info("Submitting make action pose request...");
            HttpResponse response = Utils.makeAuthenticatedRequest(
                "POST", "/api/v1/make-action-pose", makeActionPoseRequest, null
            );
            
            if (response.getStatusLine().getStatusCode() != 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("Failed to submit make action pose request, status code: {}", response.getStatusLine().getStatusCode());
                logger.error("Response content: {}", responseBody);
                throw new RuntimeException("Failed to submit make action pose request");
            }
            
            String responseBody = EntityUtils.toString(response.getEntity());
            Models.PromptResponse promptResponse = objectMapper.readValue(responseBody, Models.PromptResponse.class);
            
            if (promptResponse.code != 0) {
                logger.error("API returned error: {}", promptResponse.msg);
                throw new RuntimeException("API returned error: " + promptResponse.msg);
            }
            
            String promptId = promptResponse.data.promptId;
            logger.info("Make action pose task submitted successfully, prompt ID: {}", promptId);
            
            // Wait for task completion
            logger.info("Waiting for task completion...");
            Models.StatusTaskResponseData completedTask = Utils.waitForTaskCompletion(promptId, 130, 5000);

            Models.HistoryResponse history_model = Utils.getTaskHistory("6");
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

            logger.info("Make action pose task completed successfully!");
            logger.info("Task ID: {}", completedTask.promptId);
            logger.info("Execution time: {} ms", completedTask.executionStart);
            
        } catch (Exception e) {
            logger.error("Make action pose demo failed", e);
            System.exit(1);
        }
    }
} 