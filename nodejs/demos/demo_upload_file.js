const path = require("path");
const { logger, simpleUploadFileToS3 } = require("../utils/utils");
const config = require("../config/config");

async function main() {
  try {
    const filePath = path.join(config.IMAGE_PATH, "pic_1.png");

    logger.info("Starting file upload demo...");
    logger.info(`File path: ${filePath}`);

    const result = await simpleUploadFileToS3(filePath);

    if (result.success) {
      logger.info("File upload demo completed successfully!");
      logger.info(`Uploaded filename: ${result.filename}`);
      logger.info(`Download URL: ${result.downloadUrl}`);
    } else {
      logger.error("File upload demo failed!");
      logger.error(`Error: ${result.error}`);
    }
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
