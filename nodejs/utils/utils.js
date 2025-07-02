const crypto = require("crypto");
const fs = require("fs");
const path = require("path");
const axios = require("axios");
const mime = require("mime-types");
const winston = require("winston");
const config = require("../config/config");

// Configure logger
const logger = winston.createLogger({
  level: "info",
  format: winston.format.combine(
    winston.format.timestamp(),
    winston.format.errors({ stack: true }),
    winston.format.json()
  ),
  defaultMeta: { service: "symbiotic-api" },
  transports: [
    new winston.transports.File({ filename: "error.log", level: "error" }),
    new winston.transports.File({ filename: "combined.log" }),
    new winston.transports.Console({
      format: winston.format.combine(
        winston.format.colorize(),
        winston.format.simple()
      ),
    }),
  ],
});

/**
 * Create HMAC-SHA256 signature
 * @param {string} apiKey - API key ID
 * @param {string} secretKey - Secret key
 * @param {number} timestamp - Timestamp
 * @param {string} body - Request body content
 * @returns {string} Signature
 */
function createSignature(apiKey, secretKey, timestamp, body = "") {
  const message = `${apiKey}${timestamp}${body}`;
  const signature = crypto
    .createHmac("sha256", secretKey)
    .update(message)
    .digest("hex");
  return signature;
}

/**
 * Get authentication headers
 * @param {string} apiKey - API key ID
 * @param {string} secretKey - Secret key
 * @param {string} body - Request body content
 * @returns {Object} Dictionary containing authentication headers
 */
function getAuthHeaders(apiKey, secretKey, body = "") {
  const timestamp = Math.floor(Date.now() / 1000);
  const signature = createSignature(apiKey, secretKey, timestamp, body);

  return {
    "X-API-Key": apiKey,
    "X-Timestamp": timestamp.toString(),
    "X-Signature": signature,
  };
}

/**
 * Make authenticated request to API
 * @param {string} method - HTTP method
 * @param {string} endpoint - API endpoint
 * @param {Object} data - Request data for POST
 * @param {Object} params - Query parameters for GET
 * @returns {Promise<Object>} Response object
 */
async function makeAuthenticatedRequest(
  method,
  endpoint,
  data = null,
  params = null
) {
  const url = `${config.API_HOST}${endpoint}`;

  try {
    if (method.toUpperCase() === "GET") {
      // GET requests use query string for signature
      let queryString = "";
      if (params) {
        queryString = Object.entries(params)
          .map(([k, v]) => `${k}=${v}`)
          .join("&");
      }

      const headers = getAuthHeaders(
        config.SECRET_ID,
        config.SECRET_KEY,
        queryString
      );
      headers["Content-Type"] = "application/x-www-form-urlencoded";

      const response = await axios.get(url, { headers, params });
      return response;
    } else {
      // POST requests use request body for signature
      const body = data ? JSON.stringify(data) : "";
      const headers = getAuthHeaders(config.SECRET_ID, config.SECRET_KEY, body);
      headers["Content-Type"] = "application/json";

      const response = await axios.post(url, body, { headers });
      return response;
    }
  } catch (error) {
    logger.error("API request failed:", error.message);
    throw error;
  }
}

/**
 * Read local file
 * @param {string} filePath - Local file path
 * @returns {Promise<Object>} Object containing file data and extension
 */
async function readLocalFile(filePath) {
  return new Promise((resolve, reject) => {
    if (!fs.existsSync(filePath)) {
      reject(new Error(`File does not exist: ${filePath}`));
      return;
    }

    fs.readFile(filePath, (err, fileData) => {
      if (err) {
        reject(err);
        return;
      }

      const extension = path.extname(filePath).toLowerCase().substring(1);
      resolve({ fileData, extension });
    });
  });
}

/**
 * Get content type based on file extension
 * @param {string} extension - File extension
 * @returns {string} MIME type
 */
function getContentType(extension) {
  const mimeType = mime.lookup(extension);
  return mimeType || "application/octet-stream";
}

/**
 * Upload file to S3 using pre-signed URL
 * @param {string} signatureUrl - S3 pre-signed upload URL
 * @param {Buffer} fileData - File binary data
 * @param {string} contentType - File content type
 * @returns {Promise<boolean>} Whether upload was successful
 */
async function uploadFileToS3(
  signatureUrl,
  fileData,
  contentType = "image/jpeg"
) {
  try {
    const headers = {
      "Content-Type": contentType,
    };

    const response = await axios.put(signatureUrl, fileData, { headers });

    if (response.status === 200) {
      logger.info("File uploaded successfully");
      return true;
    } else {
      logger.error(`File upload failed, status code: ${response.status}`);
      logger.error(`Response content: ${response.data}`);
      return false;
    }
  } catch (error) {
    logger.error(`Error occurred during upload: ${error.message}`);
    return false;
  }
}

/**
 * Get filename from URL
 * @param {string} url - URL to extract filename from
 * @returns {string} Filename
 */
function getFilenameFromUrl(url) {
  const urlObj = new URL(url);
  const pathname = urlObj.pathname;
  const filename = path.basename(pathname);

  // Handle potential query parameters
  return filename.split("?")[0].split("#")[0];
}

/**
 * Simple upload file to S3
 * @param {string} filePath - Local file path to upload
 * @returns {Promise<Object>} Upload result with filename and download URL
 */
async function simpleUploadFileToS3(filePath) {
  try {
    const { fileData, extension } = await readLocalFile(filePath);

    // 1. Get S3 signature URL
    logger.info(`Getting S3 signature URL, file type: ${extension}`);
    const response = await makeAuthenticatedRequest(
      "GET",
      "/api/v1/image-signature",
      null,
      { extension }
    );

    if (response.status !== 200) {
      throw new Error(
        `Failed to get signature URL, status code: ${response.status}`
      );
    }

    const responseData = response.data;
    if (responseData.code !== 0) {
      throw new Error(`API returned error: ${responseData.msg}`);
    }

    // 2. Get signature URL and download URL
    const signature = responseData.data.signature;
    const downloadUrl = responseData.data.downloadUrl;

    logger.info(`Obtained signature URL: ${signature}`);
    logger.info(`File extension: ${extension}`);
    logger.info(`Download URL: ${downloadUrl}`);

    // 3. Get content type
    const contentType = getContentType(extension);
    logger.info(`File content type: ${contentType}`);

    // 4. Upload file to S3
    logger.info("Starting file upload to S3...");
    const success = await uploadFileToS3(signature, fileData, contentType);

    if (success) {
      logger.info("File upload completed!");
      logger.info(`File download link: ${downloadUrl}`);

      // Extract filename from download URL
      const filename = getFilenameFromUrl(downloadUrl);

      return {
        success: true,
        filename,
        downloadUrl,
      };
    } else {
      throw new Error("File upload failed!");
    }
  } catch (error) {
    logger.error(`Upload failed: ${error.message}`);
    return {
      success: false,
      error: error.message,
    };
  }
}

/**
 * Wait for task completion
 * @param {string} promptId - Task ID
 * @param {number} maxRetries - Maximum number of retries
 * @param {number} retryDelay - Delay between retries in milliseconds
 * @returns {Promise<Object>} Task status
 */
async function waitForTaskCompletion(
  promptId,
  maxRetries = 30,
  retryDelay = 2000
) {
  for (let i = 0; i < maxRetries; i++) {
    try {
      logger.info(`Checking task status (attempt ${i + 1}/${maxRetries})...`);

      const response = await makeAuthenticatedRequest(
        "GET",
        "/api/v1/get-task-status",
        null,
        { promptId }
      );

      if (response.status !== 200) {
        throw new Error(
          `Failed to get task status, status code: ${response.status}`
        );
      }

      const responseData = response.data;
      if (responseData.code !== 0) {
        throw new Error(`API returned error: ${responseData.msg}`);
      }

      const tasks = responseData.data.tasks;
      if (tasks && tasks.length > 0) {
        const task = tasks[0];
        logger.info(`Task status: ${task.complete}`);

        if (task.complete === 1) {
          logger.info("Task completed successfully!");
          return { success: true, task };
        } else if (task.complete === 3) {
          throw new Error("Task failed with error");
        }
      }

      // Wait before next retry
      if (i < maxRetries - 1) {
        logger.info(`Waiting ${retryDelay}ms before next check...`);
        await new Promise((resolve) => setTimeout(resolve, retryDelay));
      }
    } catch (error) {
      logger.error(`Error checking task status: ${error.message}`);
      if (i === maxRetries - 1) {
        throw error;
      }
    }
  }

  throw new Error("Task timeout - maximum retries exceeded");
}

/**
 * Get task history
 * @param {string} toolId - Tool ID
 * @returns {Promise<Object>} Task history
 */
async function getTaskHistory(toolId) {
  try {
    const response = await makeAuthenticatedRequest(
      "GET",
      "/api/v1/get-task-history",
      null,
      { tool_id: toolId }
    );

    if (response.status !== 200) {
      throw new Error(
        `Failed to get task history, status code: ${response.status}`
      );
    }

    const responseData = response.data;
    if (responseData.code !== 0) {
      throw new Error(`API returned error: ${responseData.msg}`);
    }

    return responseData.data;
  } catch (error) {
    logger.error(`Failed to get task history: ${error.message}`);
    throw error;
  }
}

module.exports = {
  logger,
  createSignature,
  getAuthHeaders,
  makeAuthenticatedRequest,
  readLocalFile,
  getContentType,
  uploadFileToS3,
  getFilenameFromUrl,
  simpleUploadFileToS3,
  waitForTaskCompletion,
  getTaskHistory,
};
