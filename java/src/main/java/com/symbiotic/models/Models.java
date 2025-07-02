package com.symbiotic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request Models
 */
public class Models {
    
    // Request Models
    public static class GetTaskStatus {
        @JsonProperty("promptId")
        public String promptId;
        
        public GetTaskStatus(String promptId) {
            this.promptId = promptId;
        }
    }
    
    public static class GetHistory {
        @JsonProperty("tool_id")
        public String toolId;
        
        public GetHistory(String toolId) {
            this.toolId = toolId;
        }
    }
    
    public static class CharacterDesignStep1 {
        @JsonProperty("character_name")
        public String characterName;
        
        @JsonProperty("character_detail")
        public String characterDetail;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        public CharacterDesignStep1(String characterName, String characterDetail, String loraType) {
            this.characterName = characterName;
            this.characterDetail = characterDetail;
            this.loraType = loraType;
        }
    }
    
    public static class CharacterDesignStep2 {
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        public CharacterDesignStep2(String promptText, String characterImageName, String loraType) {
            this.promptText = promptText;
            this.characterImageName = characterImageName;
            this.loraType = loraType;
        }
    }
    
    public static class CharacterDesignStep3 {
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("video_name")
        public String videoName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        public CharacterDesignStep3(String promptText, String characterImageName, String videoName, String loraType) {
            this.promptText = promptText;
            this.characterImageName = characterImageName;
            this.videoName = videoName;
            this.loraType = loraType;
        }
    }
    
    public static class Inpainting {
        @JsonProperty("description")
        public String description;
        
        @JsonProperty("mask_name")
        public String maskName;
        
        public Inpainting(String description, String maskName) {
            this.description = description;
            this.maskName = maskName;
        }
    }
    
    public static class Outpainting {
        @JsonProperty("description")
        public String description;
        
        @JsonProperty("image_name")
        public String imageName;
        
        @JsonProperty("left")
        public int left;
        
        @JsonProperty("top")
        public int top;
        
        @JsonProperty("right")
        public int right;
        
        @JsonProperty("bottom")
        public int bottom;
        
        public Outpainting(String description, String imageName, int left, int top, int right, int bottom) {
            this.description = description;
            this.imageName = imageName;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
    
    public static class MakeActionPose {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        @JsonProperty("mask_image_name")
        public String maskImageName;
        
        public MakeActionPose(String characterImageName, String loraType, String promptText, String poseType, String maskImageName) {
            this.characterImageName = characterImageName;
            this.loraType = loraType;
            this.promptText = promptText;
            this.poseType = poseType;
            this.maskImageName = maskImageName;
        }
    }
    
    public static class FixHand {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        public FixHand(String characterImageName, String loraType, String promptText, String poseType) {
            this.characterImageName = characterImageName;
            this.loraType = loraType;
            this.promptText = promptText;
            this.poseType = poseType;
        }
    }
    
    public static class GeneratePoseVideo {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        public GeneratePoseVideo(String characterImageName, String loraType, String promptText, String poseType) {
            this.characterImageName = characterImageName;
            this.loraType = loraType;
            this.promptText = promptText;
            this.poseType = poseType;
        }
    }
    
    public static class MakeActionPoseNightly {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("prompt_text")
        public String promptText;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        @JsonProperty("mask_image_name")
        public String maskImageName;
        
        public MakeActionPoseNightly(String characterImageName, String promptText, String loraType, String poseType, String maskImageName) {
            this.characterImageName = characterImageName;
            this.promptText = promptText;
            this.loraType = loraType;
            this.poseType = poseType;
            this.maskImageName = maskImageName;
        }
    }
    
    public static class FixHandNightly {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        public FixHandNightly(String characterImageName, String loraType, String poseType) {
            this.characterImageName = characterImageName;
            this.loraType = loraType;
            this.poseType = poseType;
        }
    }
    
    public static class GeneratePoseVideoNightly {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        public GeneratePoseVideoNightly(String characterImageName, String poseType) {
            this.characterImageName = characterImageName;
            this.poseType = poseType;
        }
    }
    
    public static class MakePoseVideo {
        @JsonProperty("character_image_name")
        public String characterImageName;
        
        @JsonProperty("lora_type")
        public String loraType;
        
        @JsonProperty("pose_type")
        public String poseType;
        
        public MakePoseVideo(String characterImageName, String loraType, String poseType) {
            this.characterImageName = characterImageName;
            this.loraType = loraType;
            this.poseType = poseType;
        }
    }
    
    public static class GeneratePoseSpriteSheet {
        @JsonProperty("video_name")
        public String videoName;
        
        public GeneratePoseSpriteSheet(String videoName) {
            this.videoName = videoName;
        }
    }
    
    public static class ImageToPrompt {
        @JsonProperty("image_name")
        public String imageName;
        
        public ImageToPrompt(String imageName) {
            this.imageName = imageName;
        }
    }
    
    public static class Generate3DReqModel {
        @JsonProperty("image_name")
        public String imageName;
        
        @JsonProperty("type")
        public String type;
        
        public Generate3DReqModel(String imageName, String type) {
            this.imageName = imageName;
            this.type = type;
        }
    }
    
    public static class RmbgReqModel {
        @JsonProperty("image_name")
        public String imageName;
        
        @JsonProperty("type")
        public String type;
        
        public RmbgReqModel(String imageName, String type) {
            this.imageName = imageName;
            this.type = type;
        }
    }
    
    public static class UpscaleReqModel {
        @JsonProperty("image_name")
        public String imageName;
        
        @JsonProperty("factor")
        public String factor;
        
        public UpscaleReqModel(String imageName, String factor) {
            this.imageName = imageName;
            this.factor = factor;
        }
    }
    
    public static class SkyboxReqModel {
        @JsonProperty("prompt")
        public String prompt;
        
        public SkyboxReqModel(String prompt) {
            this.prompt = prompt;
        }
    }
    
    // Response Data Models
    public static class PromptResponseData {
        @JsonProperty("workflowId")
        public int workflowId;
        
        @JsonProperty("promptId")
        public String promptId;
        
        @JsonProperty("number")
        public int number;
    }
    
    public static class StatusTaskResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("promptId")
        public String promptId;
        
        @JsonProperty("complete")
        public int complete;
        
        @JsonProperty("executionStart")
        public long executionStart;
        
        @JsonProperty("createAt")
        public String createAt;
        
        @JsonProperty("updateAt")
        public String updateAt;
    }
    
    public static class StatusResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("createAt")
        public String createAt;
        
        @JsonProperty("tasks")
        public StatusTaskResponseData[] tasks;
    }
    
    public static class HistoryTaskResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("promptId")
        public String promptId;
        
        @JsonProperty("complete")
        public int complete;
        
        @JsonProperty("executionStart")
        public long executionStart;
        
        @JsonProperty("outPuts")
        public String[] outPuts;
        
        @JsonProperty("createAt")
        public String createAt;
        
        @JsonProperty("updateAt")
        public String updateAt;
    }
    
    public static class HistoryItemResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("createAt")
        public String createAt;
        
        @JsonProperty("tasks")
        public HistoryTaskResponseData[] tasks;
    }
    
    public static class HistoryResponseData {
        @JsonProperty("items")
        public HistoryItemResponseData[] items;
    }
    
    public static class S3SignResponseData {
        @JsonProperty("extension")
        public String extension;
        
        @JsonProperty("signature")
        public String signature;
        
        @JsonProperty("downloadUrl")
        public String downloadUrl;
    }
    
    // Response Models
    public static class PromptResponse {
        @JsonProperty("code")
        public int code;
        
        @JsonProperty("msg")
        public String msg;
        
        @JsonProperty("data")
        public PromptResponseData data;
    }
    
    public static class StatusResponse {
        @JsonProperty("code")
        public int code;
        
        @JsonProperty("msg")
        public String msg;
        
        @JsonProperty("data")
        public StatusResponseData data;
    }
    
    public static class HistoryResponse {
        @JsonProperty("code")
        public int code;
        
        @JsonProperty("msg")
        public String msg;
        
        @JsonProperty("data")
        public HistoryResponseData data;
    }
    
    public static class S3SignResponse {
        @JsonProperty("code")
        public int code;
        
        @JsonProperty("msg")
        public String msg;
        
        @JsonProperty("data")
        public S3SignResponseData data;
    }
} 