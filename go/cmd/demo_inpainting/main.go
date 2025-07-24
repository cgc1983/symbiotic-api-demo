package main

import (
	"encoding/json"
	"log"
	"path/filepath"
	"strconv"
	"time"

	"symbiotic-api-demo/config"
	"symbiotic-api-demo/models"
	"symbiotic-api-demo/utils"
)

func main() {
	log.Println("Starting inpainting process")
	
	// Upload mask image
	imagePath := filepath.Join(config.ImagePath, "pic_1.png")
	imageName, err := utils.SimpleUploadFileToS3(imagePath)
	if err != nil {
		log.Fatalf("Failed to upload image: %v", err)
	}
	log.Printf("Image uploaded successfully, image name: %s", imageName)
	
	// Execute inpainting
	requestModel := models.Inpainting{
		Description: "girl",
		MaskName:    imageName,
	}
	
	response, err := utils.MakeAuthenticatedRequest("POST", "/api/v1/inpainting", requestModel, nil)
	if err != nil {
		log.Fatalf("Error making inpainting request: %v", err)
	}
	defer response.Body.Close()
	
	var promptResponse models.PromptResponse
	if err := json.NewDecoder(response.Body).Decode(&promptResponse); err != nil {
		log.Fatalf("Error decoding response: %v", err)
	}
	
	if promptResponse.Code != 0 {
		log.Fatalf("Inpainting execution failed, error message: %s", promptResponse.Msg)
	}
	
	workflowId := promptResponse.Data.WorkflowID
	
	// Check status
	getStatusReq := models.GetTaskStatus{WorkflowID: workflowId}
	for {
		statusResponse, err := utils.MakeAuthenticatedRequest("GET", "/api/v1/get-task-status", nil, map[string]string{
			"workflow_id": strconv.Itoa(getStatusReq.WorkflowID),
		})
		if err != nil {
			log.Fatalf("Error checking status: %v", err)
		}
		
		var statusModel models.StatusResponse
		if err := json.NewDecoder(statusResponse.Body).Decode(&statusModel); err != nil {
			log.Fatalf("Error decoding status response: %v", err)
		}
		statusResponse.Body.Close()
		
		if statusModel.Data.Tasks[0].Complete == 1 {
			break
		} else {
			log.Println("Task not completed, please wait...")
			time.Sleep(5 * time.Second)
		}
	}
	
	// Get history
	getHistoryReq := models.GetHistory{ToolID: "4"}
	historyResponse, err := utils.MakeAuthenticatedRequest("GET", "/api/v1/get-task-history", nil, map[string]string{
		"tool_id": getHistoryReq.ToolID,
		"page": "1",
		"page_size": "10",
	})
	if err != nil {
		log.Fatalf("Error getting history: %v", err)
	}
	defer historyResponse.Body.Close()
	
	var historyModel models.HistoryResponse
	if err := json.NewDecoder(historyResponse.Body).Decode(&historyModel); err != nil {
		log.Fatalf("Error decoding history response: %v", err)
	}
	
	if historyModel.Code != 0 {
		log.Fatalf("Failed to get history, error message: %s", historyModel.Msg)
	}
	
	for _, item := range historyModel.Data.Items {
		log.Printf("History ID: %d", item.ID)
		tasks := item.Tasks
		for _, task := range tasks {
			log.Printf("Task ID: %d", task.ID)
			log.Printf("Task status: %d", task.Complete)
			log.Printf("Task start time: %d", task.ExecutionStart)
			log.Printf("Task update time: %s", task.UpdateAt)
			log.Printf("Task creation time: %s", task.CreateAt)
			for _, output := range task.OutPuts {
				downloadURL := config.S3BucketBaseURL + output
				log.Printf("Download link: %s", downloadURL)
			}
		}
	}
} 