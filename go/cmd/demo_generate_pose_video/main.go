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
	log.Println("Starting generate-pose-video process")
	
	// Upload character image
	characterImagePath := filepath.Join(config.ImagePath, "pic_1.png")
	characterImageName, err := utils.SimpleUploadFileToS3(characterImagePath)
	if err != nil {
		log.Fatalf("Failed to upload character image: %v", err)
	}
	log.Printf("Character image uploaded successfully, image name: %s", characterImageName)
	
	// Execute generate-pose-video
	requestModel := models.GeneratePoseVideo{
		CharacterImageName: characterImageName,
		LoraType:           "1", // 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;
		PromptText:         "A beautiful character in action pose",
		PoseType:           "0", // 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;
	}
	
	response, err := utils.MakeAuthenticatedRequest("POST", "/api/v1/generate-pose-video", requestModel, nil)
	if err != nil {
		log.Fatalf("Error making generate-pose-video request: %v", err)
	}
	defer response.Body.Close()
	
	var promptResponse models.PromptResponse
	if err := json.NewDecoder(response.Body).Decode(&promptResponse); err != nil {
		log.Fatalf("Error decoding response: %v", err)
	}
	
	if promptResponse.Code != 0 {
		log.Fatalf("Generate-pose-video execution failed, error message: %s", promptResponse.Msg)
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
	getHistoryReq := models.GetHistory{ToolID: "6"} // Assuming tool_id for generate-pose-video is 6
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