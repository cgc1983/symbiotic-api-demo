package models

// Request Models
type GetTaskStatus struct {
	WorkflowID int `json:"workflow_id" description:"Task ID"`
}

type GetHistory struct {
	ToolID string `json:"tool_id" description:"Tool ID, reference to file: Tool Description"`
}

type CharacterDesignStep1 struct {
	CharacterName   string `json:"character_name" description:"Character Name"`
	CharacterDetail string `json:"character_detail" description:"Character Detail"`
	LoraType        string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
}

type CharacterDesignStep2 struct {
	PromptText        string `json:"prompt_text" description:"Prompt Text"`
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
}

type CharacterDesignStep3 struct {
	PromptText        string `json:"prompt_text" description:"Prompt Text"`
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	VideoName          string `json:"video_name" description:"Video Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
}

type Inpainting struct {
	Description string `json:"description" description:"Description"`
	MaskName    string `json:"mask_name" description:"Mask Image Name"`
}

type Outpainting struct {
	Description string `json:"description" description:"Description"`
	ImageName   string `json:"image_name" description:"Image Name"`
	Left        int    `json:"left" description:"Left Extend Size"`
	Top         int    `json:"top" description:"Top Extend Size"`
	Right       int    `json:"right" description:"Right Extend Size"`
	Bottom      int    `json:"bottom" description:"Bottom Extend Size"`
}

type MakeActionPose struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PromptText         string `json:"prompt_text" description:"Prompt Text"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;"`
	MaskImageName      string `json:"mask_image_name" description:"Mask Image Name"`
}

type FixHand struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PromptText         string `json:"prompt_text" description:"Prompt Text"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;"`
}

type GeneratePoseVideo struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PromptText         string `json:"prompt_text" description:"Prompt Text"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;"`
}

type MakeActionPoseNightly struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	PromptText         string `json:"prompt_text" description:"Prompt Text"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right;"`
	MaskImageName      string `json:"mask_image_name" description:"Mask Image Name"`
}

type FixHandNightly struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right;"`
}

type GeneratePoseVideoNightly struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	PoseType           string `json:"pose_type" description:"Pose Type; 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right;"`
}

type MakePoseVideo struct {
	CharacterImageName string `json:"character_image_name" description:"Character Image Name"`
	LoraType           string `json:"lora_type" description:"Lora Type; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;"`
	PoseType           string `json:"pose_type" description:"Pose Type; reference to file: Pose Description"`
}

type GeneratePoseSpriteSheet struct {
	VideoName string `json:"video_name" description:"Video Name"`
}

type ImageToPrompt struct {
	ImageName string `json:"image_name" description:"Image Name"`
}

type Generate3DReqModel struct {
	ImageName string `json:"image_name" description:"Image Name"`
	Type      string `json:"type" description:"0 White Model, 1 White Model + Texture"`
}

type RmbgReqModel struct {
	ImageName string `json:"image_name" description:"Image Name"`
	Type      string `json:"type" description:"Mode Type; 0: BiRefNet; 1: RMBG2.0;"`
}

type UpscaleReqModel struct {
	ImageName string `json:"image_name" description:"Image Name"`
	Factor    string `json:"factor" description:"Scale Factor; 0: 2X; 1: 4X;"`
}

type SkyboxReqModel struct {
	Prompt string `json:"prompt" description:"Prompt Text"`
}

// Response Data Models
type PromptResponseData struct {
	WorkflowID int    `json:"workflow_id" description:"WorkFlow ID"`
	Number     int    `json:"number" description:"No."`
}

type StatusTaskResponseData struct {
	ID             int    `json:"id" description:"ID"`
	PromptID       string `json:"prompt_id" description:"Task ID"`
	Complete       int    `json:"complete" description:"Completed status; 0: In Queue; 1: Completed; 2: Processing; 3: Error Occurred;"`
	ExecutionStart int    `json:"execution_start" description:"Execution start time"`
	CreateAt       string `json:"create_at" description:"Create Time"`
	UpdateAt       string `json:"update_at" description:"Update Time"`
}

type StatusResponseData struct {
	ID       int                      `json:"id" description:"ID"`
	CreateAt string                   `json:"create_at" description:"Create Time"`
	Tasks    []StatusTaskResponseData `json:"tasks" description:"Task List"`
}

type HistoryTaskResponseData struct {
	ID             int      `json:"id" description:"ID"`
	PromptID       string   `json:"prompt_id" description:"Task ID"`
	Complete       int      `json:"complete" description:"Completed status; 0: In Queue; 1: Completed; 2: Processing; 3: Error Occurred;"`
	ExecutionStart int      `json:"execution_start" description:"Execution start time"`
	OutPuts        []string `json:"out_puts" description:"Outputs; include Image, Video, Model etc."`
	CreateAt       string   `json:"create_at" description:"Create Time"`
	UpdateAt       string   `json:"update_at" description:"Update Time"`
}

type HistoryItemResponseData struct {
	ID       int                        `json:"id" description:"ID"`
	CreateAt string                     `json:"create_at" description:"Create Time"`
	Tasks    []HistoryTaskResponseData  `json:"tasks" description:"Task List"`
}

type HistoryResponseData struct {
	Items []HistoryItemResponseData `json:"items" description:"History List"`
}

type S3SignResponseData struct {
	Extension   string `json:"extension" description:"File extension"`
	Signature   string `json:"signature" description:"S3 signature URL"`
	DownloadURL string `json:"download_url" description:"Download URL"`
}

// Response Models
type PromptResponse struct {
	Code int                  `json:"code" description:"Error Code"`
	Msg  string               `json:"msg" description:"Error Message"`
	Data *PromptResponseData  `json:"data,omitempty" description:"Response Data"`
}

type StatusResponse struct {
	Code int                `json:"code" description:"Error Code"`
	Msg  string             `json:"msg" description:"Error Message"`
	Data *StatusResponseData `json:"data,omitempty" description:"Response Data"`
}

type HistoryResponse struct {
	Code int                 `json:"code" description:"Error Code"`
	Msg  string              `json:"msg" description:"Error Message"`
	Data *HistoryResponseData `json:"data,omitempty" description:"Response Data"`
}

type S3SignResponse struct {
	Code int                `json:"code" description:"Error Code"`
	Msg  string             `json:"msg" description:"Error Message"`
	Data *S3SignResponseData `json:"data,omitempty" description:"Response Data"`
} 