const path = require("path");

// API Configuration
const API_HOST = "https://api.symbiotic-labs.xyz";

// Path Configuration
const ROOT_PATH = __dirname;
const PARENT_PATH = path.dirname(path.dirname(ROOT_PATH));
const IMAGE_PATH = path.join(PARENT_PATH, "images");
const VIDEO_PATH = path.join(PARENT_PATH, "videos");

// S3 Configuration
const S3_BUCKET_BASE_URL =
  "https://comfyprod-1365981877.cos.ap-singapore.myqcloud.com/temp/";

// Authentication Credentials
const SECRET_ID = "t1";
const SECRET_KEY = "test_secret_key_001";

module.exports = {
  // API Configuration
  API_HOST,

  // Path Configuration
  ROOT_PATH,
  PARENT_PATH,
  IMAGE_PATH,
  VIDEO_PATH,

  // S3 Configuration
  S3_BUCKET_BASE_URL,

  // Authentication
  SECRET_ID,
  SECRET_KEY,
};
