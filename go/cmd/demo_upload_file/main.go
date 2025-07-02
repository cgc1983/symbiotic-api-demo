package main

import (
	"encoding/json"
	"io"
	"log"
	"path/filepath"

	"symbiotic-api-demo/config"
	"symbiotic-api-demo/models"
	"symbiotic-api-demo/utils"
)

func main() {
	filePath := filepath.Join(config.ImagePath, "pic_1.png")
	
	fileData, extension, err := utils.ReadLocalFile(filePath)
	if err != nil {
		log.Fatalf("Error reading file: %v", err)
	}
	
	// 1. Get S3 signature URL
	log.Printf("Getting S3 signature URL, file type: %s", extension)
	params := map[string]string{"extension": extension}
	response, err := utils.MakeAuthenticatedRequest("GET", "/api/v1/image-signature", nil, params)
	if err != nil {
		log.Fatalf("Error making request: %v", err)
	}
	defer response.Body.Close()
	
	if response.StatusCode != 200 {
		body, _ := io.ReadAll(response.Body)
		log.Fatalf("Failed to get signature URL, status code: %d, response: %s", response.StatusCode, string(body))
	}
	
	var signResponse models.S3SignResponse
	if err := json.NewDecoder(response.Body).Decode(&signResponse); err != nil {
		log.Fatalf("Error decoding response: %v", err)
	}
	
	if signResponse.Code != 0 {
		log.Fatalf("API returned error: %s", signResponse.Msg)
	}
	
	// 2. Get signature URL
	signature := signResponse.Data.Signature
	downloadURL := signResponse.Data.DownloadURL
	
	log.Printf("Obtained signature URL: %s", signature)
	log.Printf("File extension: %s", extension)
	log.Printf("Download URL: %s", downloadURL)
	
	// 3. Get content type
	contentType := utils.GetContentType(extension)
	log.Printf("File content type: %s", contentType)
	
	// 4. Upload file to S3
	log.Println("Starting file upload to S3...")
	success := utils.UploadFileToS3(signature, fileData, contentType)
	
	if success {
		log.Println("File upload completed!")
		log.Printf("File download link: %s", downloadURL)
	} else {
		log.Fatal("File upload failed!")
	}
} 