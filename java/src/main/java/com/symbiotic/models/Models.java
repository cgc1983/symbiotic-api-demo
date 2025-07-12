package com.symbiotic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request Models
 */
public class Models {
    
    // Request Models
    public static class GetTaskStatus {
        @JsonProperty("workflow_id")
        public String workflowId;
        
        public GetTaskStatus(String workflowId) {
            this.workflowId = workflowId;
        }
    }
    
    public static class GetHistory {
        @JsonProperty("tool_id")
        public String toolId;

        @JsonProperty("page")
        public String page;

        @JsonProperty("page_size")
        public String pageSize;
        
        public GetHistory(String toolId) {
            this.toolId = toolId;
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

    
    // Response Data Models
    public static class PromptResponseData {
        @JsonProperty("workflow_id")
        public int workflowId;
    }
    
    public static class StatusTaskResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("complete")
        public int complete;
        
        @JsonProperty("execution_start")
        public long executionStart;
        
        @JsonProperty("create_at")
        public String createAt;
        
        @JsonProperty("update_at")
        public String updateAt;
    }
    
    public static class StatusResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("create_at")
        public String createAt;
        
        @JsonProperty("tasks")
        public StatusTaskResponseData[] tasks;

        @JsonProperty("percentage")
        public int percentage;
    }
    
    public static class HistoryTaskResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("complete")
        public int complete;
        
        @JsonProperty("execution_start")
        public long executionStart;
        
        @JsonProperty("out_puts")
        public String[] outPuts;
        
        @JsonProperty("create_at")
        public String createAt;
        
        @JsonProperty("update_at")
        public String updateAt;
    }
    
    public static class HistoryItemResponseData {
        @JsonProperty("id")
        public int id;
        
        @JsonProperty("create_at")
        public String createAt;
        
        @JsonProperty("tasks")
        public HistoryTaskResponseData[] tasks;

        @JsonProperty("percentage")
        public int percentage;
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
        
        @JsonProperty("download_url")
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