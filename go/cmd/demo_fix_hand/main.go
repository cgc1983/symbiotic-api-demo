package main

import (
	"encoding/json"
	"log"
	"path/filepath"
	"time"

	"symbiotic-api-demo/config"
	"symbiotic-api-demo/models"
	"symbiotic-api-demo/utils"
)

func main() {
	log.Println("Starting fix-hand process")
	
	// Upload character image
	imagePath := filepath.Join(config.ImagePath, "pic_1.png")
	imageName, err := utils.SimpleUploadFileToS3(imagePath)
	if err != nil {
		log.Fatalf("Failed to upload character image: %v", err)
	}
	log.Printf("Character image uploaded successfully, image name: %s", imageName)
	
	// Execute fix-hand
	requestModel := models.FixHand{
		CharacterImageName: imageName,
		LoraType:           "1", // Disney style
		PromptText:         "Fix the hand position and make it look natural",
		PoseType:           "0", // Idle left-facing
	}
	
	response, err := utils.MakeAuthenticatedRequest("POST", "/api/v1/fix-hand", requestModel, nil)
	if err != nil {
		log.Fatalf("Error making fix-hand request: %v", err)
	}
	defer response.Body.Close()
	
	var promptResponse models.PromptResponse
	if err := json.NewDecoder(response.Body).Decode(&promptResponse); err != nil {
		log.Fatalf("Error decoding response: %v", err)
	}
	
	if promptResponse.Code != 0 {
		log.Fatalf("Fix-hand execution failed, error message: %s", promptResponse.Msg)
	}
	
	promptID := promptResponse.Data.PromptID
	
	// Check status
	getStatusReq := models.GetTaskStatus{PromptID: promptID}
	for {
		statusResponse, err := utils.MakeAuthenticatedRequest("GET", "/api/v1/get-task-status", nil, map[string]string{
			"promptId": getStatusReq.PromptID,
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
	getHistoryReq := models.GetHistory{ToolID: "5"} // Assuming tool_id for fix-hand is 5
	historyResponse, err := utils.MakeAuthenticatedRequest("GET", "/api/v1/get-task-history", nil, map[string]string{
		"tool_id": getHistoryReq.ToolID,
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