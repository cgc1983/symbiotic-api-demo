const path = require("path");
const {
  logger,
  simpleUploadFileToS3,
  makeAuthenticatedRequest,
  waitForTaskCompletion,
  getTaskHistory,
} = require("../utils/utils");
const { FixHand } = require("../models/models");
const config = require("../config/config");

async function main() {
  try {
    logger.info("Starting fix-hand process");

    // Upload character image
    const characterImagePath = path.join(config.IMAGE_PATH, "pic_1.png");
    const characterUploadResult = await simpleUploadFileToS3(
      characterImagePath
    );

    if (!characterUploadResult.success) {
      logger.error("Failed to upload character image");
      return;
    }

    const characterImageName = characterUploadResult.filename;
    logger.info(
      `Character image uploaded successfully, image name: ${characterImageName}`
    );

    // Execute fix-hand
    const requestModel = new FixHand(
      characterImageName,
      "1", // 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;
      "A beautiful character with fixed hands",
      "0" // 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;
    );

    const response = await makeAuthenticatedRequest(
      "POST",
      "/api/v1/fix-hand",
      requestModel
    );

    if (response.status !== 200) {
      logger.error(
        `Fix-hand execution failed, status code: ${response.status}`
      );
      return;
    }

    const promptModel = response.data;
    if (promptModel.code !== 0) {
      logger.error(
        `Fix-hand execution failed, error message: ${promptModel.msg}`
      );
      return;
    }

    const promptId = promptModel.data.promptId;
    logger.info(`Task submitted with prompt ID: ${promptId}`);

    // Wait for task completion
    logger.info("Waiting for task completion...");
    const taskResult = await waitForTaskCompletion(promptId);

    if (!taskResult.success) {
      logger.error("Task failed to complete");
      return;
    }

    logger.info("Task completed successfully!");

    // Get history
    logger.info("Getting task history...");
    const history = await getTaskHistory("6"); // Assuming tool_id for fix-hand is 6

    logger.info("Task history:");
    for (const item of history.items) {
      logger.info(`History ID: ${item.id}`);
      const tasks = item.tasks;
      for (const task of tasks) {
        logger.info(`Task ID: ${task.id}`);
        logger.info(`Task status: ${task.complete}`);
        logger.info(`Task start time: ${task.executionStart}`);
        logger.info(`Task update time: ${task.updateAt}`);
        logger.info(`Task creation time: ${task.createAt}`);

        if (task.outPuts && task.outPuts.length > 0) {
          for (const output of task.outPuts) {
            const downloadUrl = `${config.S3_BUCKET_BASE_URL}${output}`;
            logger.info(`Download link: ${downloadUrl}`);
          }
        }
      }
    }

    logger.info("Fix hand demo completed successfully!");
  } catch (error) {
    logger.error(`Demo failed with error: ${error.message}`);
    process.exit(1);
  }
}

// Run the demo if this file is executed directly
if (require.main === module) {
  main();
}

module.exports = { main };
