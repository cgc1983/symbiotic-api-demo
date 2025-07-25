const path = require("path");
const {
  logger,
  simpleUploadFileToS3,
  makeAuthenticatedRequest,
  waitForTaskCompletion,
  getTaskHistory,
} = require("../utils/utils");
const { Outpainting } = require("../models/models");
const config = require("../config/config");

async function main() {
  try {
    logger.info("Starting outpainting process");

    // Upload image
    const imagePath = path.join(config.IMAGE_PATH, "pic_1.png");
    const uploadResult = await simpleUploadFileToS3(imagePath);

    if (!uploadResult.success) {
      logger.error("Failed to upload image");
      return;
    }

    const imageName = uploadResult.filename;
    logger.info(`Image uploaded successfully, image name: ${imageName}`);

    // Execute outpainting
    const requestModel = new Outpainting("girl", imageName, 100, 100, 100, 100);
    const response = await makeAuthenticatedRequest(
      "POST",
      "/api/v1/outpainting",
      requestModel
    );

    if (response.status !== 200) {
      logger.error(
        `Outpainting execution failed, status code: ${response.status}`
      );
      return;
    }

    const promptModel = response.data;
    if (promptModel.code !== 0) {
      logger.error(
        `Outpainting execution failed, error message: ${promptModel.msg}`
      );
      return;
    }

    const workflow_id = promptModel.data.workflow_id;
    logger.info(`Task submitted with workflow ID: ${workflow_id}`);

    // Wait for task completion
    logger.info("Waiting for task completion...");
    const taskResult = await waitForTaskCompletion(workflow_id);

    if (!taskResult.success) {
      logger.error("Task failed to complete");
      return;
    }

    logger.info("Task completed successfully!");

    // Get history
    logger.info("Getting task history...");
    const history = await getTaskHistory("5");

    logger.info("Task history:");
    for (const item of history.items) {
      logger.info(`History ID: ${item.id}`);
      const tasks = item.tasks;
      for (const task of tasks) {
        logger.info(`Task ID: ${task.id}`);
        logger.info(`Task status: ${task.complete}`);
        logger.info(`Task start time: ${task.execution_start}`);
        logger.info(`Task update time: ${task.update_at}`);
        logger.info(`Task creation time: ${task.create_at}`);

        if (task.out_puts && task.out_puts.length > 0) {
          for (const output of task.out_puts) {
            const downloadUrl = `${config.S3_BUCKET_BASE_URL}${output}`;
            logger.info(`Download link: ${downloadUrl}`);
          }
        }
      }
    }

    logger.info("Outpainting demo completed successfully!");
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
