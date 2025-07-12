package com.symbiotic.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symbiotic.config.Config;
import com.symbiotic.models.Models;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility functions for API calls, authentication, file handling, and S3 uploads
 */
public class Utils {
    
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClients.createDefault();
    
    /**
     * Get API host from config
     */
    public static String getApiHost() {
        return Config.API_HOST;
    }
    
    /**
     * Create HMAC-SHA256 signature
     */
    public static String createSignature(String apiKey, String secretKey, long timestamp, String body) {
        try {
            String message = apiKey + timestamp + body;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(message.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error creating signature", e);
            throw new RuntimeException("Failed to create signature", e);
        }
    }
    
    /**
     * Convert byte array to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * Get authentication headers
     */
    public static Map<String, String> getAuthHeaders(String apiKey, String secretKey, String body) {
        long timestamp = System.currentTimeMillis() / 1000;
        String signature = createSignature(apiKey, secretKey, timestamp, body);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-API-Key", apiKey);
        headers.put("X-Timestamp", String.valueOf(timestamp));
        headers.put("X-Signature", signature);
        return headers;
    }
    
    /**
     * Make authenticated HTTP request
     */
    public static HttpResponse makeAuthenticatedRequest(String method, String endpoint, Object data, Map<String, String> params) throws Exception {
        String url = Config.API_HOST + endpoint;
        
        if ("GET".equalsIgnoreCase(method)) {
            // Build query string for GET requests
            String queryString = "";
            if (params != null && !params.isEmpty()) {
                List<String> queryParams = new ArrayList<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    queryParams.add(entry.getKey() + "=" + entry.getValue());
                }
                queryString = String.join("&", queryParams);
            }
            
            Map<String, String> headers = getAuthHeaders(Config.SECRET_ID, Config.SECRET_KEY, queryString);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            
            HttpGet request = new HttpGet(uriBuilder.build());
            for (Map.Entry<String, String> header : headers.entrySet()) {
                request.addHeader(header.getKey(), header.getValue());
            }
            
            return httpClient.execute(request);
            
        } else {
            // POST requests use request body for signature
            String body = "";
            if (data != null) {
                body = objectMapper.writeValueAsString(data);
            }
            
            Map<String, String> headers = getAuthHeaders(Config.SECRET_ID, Config.SECRET_KEY, body);
            headers.put("Content-Type", "application/json");
            
            HttpPost request = new HttpPost(url);
            if (data != null) {
                StringEntity entity = new StringEntity(body);
                request.setEntity(entity);
            }
            
            for (Map.Entry<String, String> header : headers.entrySet()) {
                request.addHeader(header.getKey(), header.getValue());
            }
            
            return httpClient.execute(request);
        }
    }
    
    /**
     * Read local file and return its content and extension
     */
    public static FileData readLocalFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        byte[] fileData = Files.readAllBytes(path);
        String extension = getFileExtension(filePath);
        
        return new FileData(fileData, extension);
    }
    
    /**
     * Get file extension from file path
     */
    private static String getFileExtension(String filePath) {
        String fileName = new File(filePath).getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * Get MIME type based on file extension
     */
    public static String getContentType(String extension) {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("bmp", "image/bmp");
        mimeTypes.put("webp", "image/webp");
        mimeTypes.put("mp4", "video/mp4");
        mimeTypes.put("avi", "video/x-msvideo");
        mimeTypes.put("mov", "video/quicktime");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("json", "application/json");
        
        return mimeTypes.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }
    
    /**
     * Upload file to S3 using pre-signed URL
     */
    public static boolean uploadFileToS3(String signatureUrl, byte[] fileData, String contentType) {
        try {
            HttpPut request = new HttpPut(signatureUrl);
            request.setHeader("Content-Type", contentType);
            request.setEntity(new ByteArrayEntity(fileData));
            
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200) {
                logger.info("File uploaded successfully");
                return true;
            } else {
                String responseBody = EntityUtils.toString(response.getEntity());
                logger.error("File upload failed, status code: {}", statusCode);
                logger.error("Response content: {}", responseBody);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error uploading file", e);
            return false;
        }
    }
    
    /**
     * Get filename from URL
     */
    public static String getFilenameFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            String path = url.getPath();
            String filename = new File(path).getName();
            
            // Handle potential query parameters
            if (filename.contains("?")) {
                filename = filename.split("\\?")[0];
            }
            if (filename.contains("#")) {
                filename = filename.split("#")[0];
            }
            
            return filename;
        } catch (Exception e) {
            logger.error("Error extracting filename from URL", e);
            return "";
        }
    }
    
    /**
     * Simple upload file to S3 using pre-signed URL
     */
    public static String simpleUploadFileToS3(String filePath) throws Exception {
        FileData fileData = readLocalFile(filePath);
        
        // 1. Get S3 signature URL
        logger.info("Getting S3 signature URL, file type: {}", fileData.extension);
        Map<String, String> params = new HashMap<>();
        params.put("extension", fileData.extension);
        
        HttpResponse response = makeAuthenticatedRequest("GET", "/api/v1/image-signature", null, params);
        
        if (response.getStatusLine().getStatusCode() != 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.error("Failed to get signature URL, status code: {}", response.getStatusLine().getStatusCode());
            logger.error("Response content: {}", responseBody);
            throw new RuntimeException("Failed to get signature URL");
        }
        
        String responseBody = EntityUtils.toString(response.getEntity());
        Models.S3SignResponse signResponse = objectMapper.readValue(responseBody, Models.S3SignResponse.class);
        
        if (signResponse.code != 0) {
            logger.error("API returned error: {}", signResponse.msg);
            throw new RuntimeException("API returned error: " + signResponse.msg);
        }
        
        // 2. Get signature URL
        String signature = signResponse.data.signature;
        String downloadUrl = signResponse.data.downloadUrl;
        
        logger.info("Obtained signature URL: {}", signature);
        logger.info("File extension: {}", fileData.extension);
        logger.info("Download URL: {}", downloadUrl);
        
        // 3. Get content type
        String contentType = getContentType(fileData.extension);
        logger.info("File content type: {}", contentType);
        
        // 4. Upload file to S3
        logger.info("Starting file upload to S3...");
        boolean success = uploadFileToS3(signature, fileData.data, contentType);
        
        if (success) {
            logger.info("File upload completed!");
            logger.info("File download link: {}", downloadUrl);
            return getFilenameFromUrl(downloadUrl);
        } else {
            throw new RuntimeException("File upload failed!");
        }
    }
    
    /**
     * Check task status
     */
    public static Models.StatusResponse checkTaskStatus(int workflowId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("workflow_id", String.valueOf(workflowId));
        
        HttpResponse response = makeAuthenticatedRequest("GET", "/api/v1/get-task-status", null, params);
        
        if (response.getStatusLine().getStatusCode() != 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.error("Failed to get task status, status code: {}", response.getStatusLine().getStatusCode());
            logger.error("Response content: {}", responseBody);
            throw new RuntimeException("Failed to get task status");
        }
        
        String responseBody = EntityUtils.toString(response.getEntity());
        return objectMapper.readValue(responseBody, Models.StatusResponse.class);
    }
    
    /**
     * Get task history
     */
    public static Models.HistoryResponse getTaskHistory(String toolId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("tool_id", toolId);
        params.put("page", "1");
        params.put("page_size", "64");
        
        HttpResponse response = makeAuthenticatedRequest("GET", "/api/v1/get-task-history", null, params);
        
        if (response.getStatusLine().getStatusCode() != 200) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.error("Failed to get task history, status code: {}", response.getStatusLine().getStatusCode());
            logger.error("Response content: {}", responseBody);
            throw new RuntimeException("Failed to get task history");
        }
        
        String responseBody = EntityUtils.toString(response.getEntity());
        return objectMapper.readValue(responseBody, Models.HistoryResponse.class);
    }
    
    /**
     * Wait for task completion
     */
    public static Models.StatusTaskResponseData waitForTaskCompletion(int workflowId, int maxRetries, long retryIntervalMs) throws Exception {
        for (int i = 0; i < maxRetries; i++) {
            Models.StatusResponse statusResponse = checkTaskStatus(workflowId);
            
            if (statusResponse.code != 0) {
                logger.error("API returned error: {}", statusResponse.msg);
                throw new RuntimeException("API returned error: " + statusResponse.msg);
            }
            
            if (statusResponse.data != null && statusResponse.data.tasks != null) {
                for (Models.StatusTaskResponseData task : statusResponse.data.tasks) {
                    if (workflowId == task.id) {
                        if (task.complete == 1) {
                            logger.info("Task completed successfully");
                            return task;
                        } else if (task.complete == 3) {
                            throw new RuntimeException("Task failed with error");
                        } else {
                            logger.info("Task still processing, retrying in {} ms...", retryIntervalMs);
                            Thread.sleep(retryIntervalMs);
                            break;
                        }
                    }
                }
            }
        }
        
        throw new RuntimeException("Task did not complete within the specified time");
    }
    
    /**
     * File data container
     */
    public static class FileData {
        public final byte[] data;
        public final String extension;
        
        public FileData(byte[] data, String extension) {
            this.data = data;
            this.extension = extension;
        }
    }
} 