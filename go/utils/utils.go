package utils

import (
	"bytes"
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"time"

	"symbiotic-api-demo/config"
	"symbiotic-api-demo/models"

	"github.com/sirupsen/logrus"
)

var log = logrus.New()

func init() {
	log.SetFormatter(&logrus.TextFormatter{
		FullTimestamp: true,
	})
}

// GetAPIHost returns the API host from config
func GetAPIHost() string {
	return config.APIHost
}

// CreateSignature creates HMAC-SHA256 signature
func CreateSignature(apiKey, secretKey string, timestamp int64, body string) string {
	message := fmt.Sprintf("%s%d%s", apiKey, timestamp, body)
	
	h := hmac.New(sha256.New, []byte(secretKey))
	h.Write([]byte(message))
	
	return hex.EncodeToString(h.Sum(nil))
}

// GetAuthHeaders returns authentication headers
func GetAuthHeaders(apiKey, secretKey, body string) map[string]string {
	timestamp := time.Now().Unix()
	signature := CreateSignature(apiKey, secretKey, timestamp, body)
	
	return map[string]string{
		"X-API-Key":    apiKey,
		"X-Timestamp":  strconv.FormatInt(timestamp, 10),
		"X-Signature":  signature,
	}
}

// MakeAuthenticatedRequest makes an authenticated HTTP request
func MakeAuthenticatedRequest(method, endpoint string, data interface{}, params map[string]string) (*http.Response, error) {
	requestURL := fmt.Sprintf("%s%s", config.APIHost, endpoint)
	
	var req *http.Request
	var err error
	
	if strings.ToUpper(method) == "GET" {
		// Build query string for GET requests
		queryString := ""
		if params != nil {
			// Create URL with query parameters to get the properly encoded query string
			baseURL, _ := url.Parse(requestURL)
			q := baseURL.Query()
			for k, v := range params {
				q.Add(k, v)
			}
			queryString = q.Encode()
		}
		
		headers := GetAuthHeaders(config.SecretID, config.SecretKey, queryString)
		headers["Content-Type"] = "application/x-www-form-urlencoded"
		
		req, err = http.NewRequest("GET", requestURL, nil)
		if err != nil {
			return nil, err
		}
		
		// Add query parameters using the same encoding method
		if params != nil {
			q := req.URL.Query()
			for k, v := range params {
				q.Add(k, v)
			}
			req.URL.RawQuery = q.Encode()
		}
		
		// Add headers
		for k, v := range headers {
			req.Header.Set(k, v)
		}
		
	} else {
		// POST requests use request body for signature
		var body []byte
		if data != nil {
			body, err = json.Marshal(data)
			if err != nil {
				return nil, err
			}
		}
		
		bodyStr := string(body)
		headers := GetAuthHeaders(config.SecretID, config.SecretKey, bodyStr)
		headers["Content-Type"] = "application/json"
		
		req, err = http.NewRequest("POST", requestURL, bytes.NewBuffer(body))
		if err != nil {
			return nil, err
		}
		
		// Add headers
		for k, v := range headers {
			req.Header.Set(k, v)
		}
	}
	
	client := &http.Client{}
	return client.Do(req)
}

// ReadLocalFile reads a local file and returns its content and extension
func ReadLocalFile(filePath string) ([]byte, string, error) {
	if _, err := os.Stat(filePath); os.IsNotExist(err) {
		return nil, "", fmt.Errorf("file does not exist: %s", filePath)
	}
	
	fileData, err := os.ReadFile(filePath)
	if err != nil {
		return nil, "", err
	}
	
	// Get file extension
	ext := strings.ToLower(filepath.Ext(filePath))
	ext = strings.TrimPrefix(ext, ".")
	
	return fileData, ext, nil
}

// GetContentType returns MIME type based on file extension
func GetContentType(extension string) string {
	mimeTypes := map[string]string{
		"jpg":  "image/jpeg",
		"jpeg": "image/jpeg",
		"png":  "image/png",
		"gif":  "image/gif",
		"bmp":  "image/bmp",
		"webp": "image/webp",
		"mp4":  "video/mp4",
		"avi":  "video/x-msvideo",
		"mov":  "video/quicktime",
		"pdf":  "application/pdf",
		"txt":  "text/plain",
		"json": "application/json",
	}
	
	if mimeType, exists := mimeTypes[strings.ToLower(extension)]; exists {
		return mimeType
	}
	return "application/octet-stream"
}

// UploadFileToS3 uploads file to S3 using pre-signed URL
func UploadFileToS3(signatureURL string, fileData []byte, contentType string) bool {
	req, err := http.NewRequest("PUT", signatureURL, bytes.NewBuffer(fileData))
	if err != nil {
		log.Errorf("Error creating upload request: %v", err)
		return false
	}
	
	req.Header.Set("Content-Type", contentType)
	
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		log.Errorf("Error uploading file: %v", err)
		return false
	}
	defer resp.Body.Close()
	
	if resp.StatusCode == 200 {
		log.Info("File uploaded successfully")
		return true
	} else {
		body, _ := io.ReadAll(resp.Body)
		log.Errorf("File upload failed, status code: %d", resp.StatusCode)
		log.Errorf("Response content: %s", string(body))
		return false
	}
}

// GetFilenameFromURL extracts filename from URL
func GetFilenameFromURL(urlStr string) string {
	parsedURL, err := url.Parse(urlStr)
	if err != nil {
		return ""
	}
	
	path := parsedURL.Path
	filename := filepath.Base(path)
	
	// Handle potential query parameters
	if idx := strings.Index(filename, "?"); idx != -1 {
		filename = filename[:idx]
	}
	if idx := strings.Index(filename, "#"); idx != -1 {
		filename = filename[:idx]
	}
	
	return filename
}

// SimpleUploadFileToS3 uploads file to S3 using pre-signed URL (complete workflow)
func SimpleUploadFileToS3(filePath string) (string, error) {
	fileData, extension, err := ReadLocalFile(filePath)
	if err != nil {
		return "", err
	}
	
	// 1. Get S3 signature URL
	log.Infof("Getting S3 signature URL, file type: %s", extension)
	params := map[string]string{"extension": extension}
	response, err := MakeAuthenticatedRequest("GET", "/api/v1/image-signature", nil, params)
	if err != nil {
		return "", err
	}
	defer response.Body.Close()
	
	if response.StatusCode != 200 {
		body, _ := io.ReadAll(response.Body)
		log.Errorf("Failed to get signature URL, status code: %d", response.StatusCode)
		log.Errorf("Response content: %s", string(body))
		return "", fmt.Errorf("failed to get signature URL")
	}
	
	var signResponse models.S3SignResponse
	if err := json.NewDecoder(response.Body).Decode(&signResponse); err != nil {
		return "", err
	}
	
	if signResponse.Code != 0 {
		log.Errorf("API returned error: %s", signResponse.Msg)
		return "", fmt.Errorf("API error: %s", signResponse.Msg)
	}
	
	// 2. Get signature URL
	signature := signResponse.Data.Signature
	
	// 3. Get content type
	contentType := GetContentType(extension)
	log.Infof("File content type: %s", contentType)
	
	// 4. Upload file to S3
	success := UploadFileToS3(signature, fileData, contentType)
	if success {
		filename := GetFilenameFromURL(signResponse.Data.DownloadURL)
		return filename, nil
	} else {
		return "", fmt.Errorf("failed to upload file to S3")
	}
} 