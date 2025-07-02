const path = require("path");
const {
  logger,
  simpleUploadFileToS3,
  makeAuthenticatedRequest,
  waitForTaskCompletion,
  getTaskHistory,
} = require("../utils/utils");
const { Inpainting } = require("../models/models");
const config = require("../config/config");

async function main() {
  try {
    logger.info("Starting inpainting process");

    // Upload mask image
    const imagePath = path.join(config.IMAGE_PATH, "pic_1.png");
    const uploadResult = await simpleUploadFileToS3(imagePath);

    if (!uploadResult.success) {
      logger.error("Failed to upload image");
      return;
    }

    const imageName = uploadResult.filename;
    logger.info(`Image uploaded successfully, image name: ${imageName}`);

    // Execute inpainting
    const requestModel = new Inpainting("girl", imageName);
    const response = await makeAuthenticatedRequest(
      "POST",
      "/api/v1/inpainting",
      requestModel
    );

    if (response.status !== 200) {
      logger.error(
        `Inpainting execution failed, status code: ${response.status}`
      );
      return;
    }

    const promptModel = response.data;
    if (promptModel.code !== 0) {
      logger.error(
        `Inpainting execution failed, error message: ${promptModel.msg}`
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
    const history = await getTaskHistory("4");

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

    logger.info("Inpainting demo completed successfully!");
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
