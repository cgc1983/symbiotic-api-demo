// Request Models
class GetTaskStatus {
  constructor(promptId) {
    this.promptId = promptId;
  }
}

class GetHistory {
  constructor(tool_id) {
    this.tool_id = tool_id;
  }
}

class CharacterDesignStep1 {
  constructor(character_name, character_detail, lora_type) {
    this.character_name = character_name;
    this.character_detail = character_detail;
    this.lora_type = lora_type;
  }
}

class CharacterDesignStep2 {
  constructor(prompt_text, character_image_name, lora_type) {
    this.prompt_text = prompt_text;
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
  }
}

class CharacterDesignStep3 {
  constructor(prompt_text, character_image_name, video_name, lora_type) {
    this.prompt_text = prompt_text;
    this.character_image_name = character_image_name;
    this.video_name = video_name;
    this.lora_type = lora_type;
  }
}

class Inpainting {
  constructor(description, mask_name) {
    this.description = description;
    this.mask_name = mask_name;
  }
}

class Outpainting {
  constructor(description, image_name, left, top, right, bottom) {
    this.description = description;
    this.image_name = image_name;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }
}

class MakeActionPose {
  constructor(
    character_image_name,
    lora_type,
    prompt_text,
    pose_type,
    mask_image_name
  ) {
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
    this.prompt_text = prompt_text;
    this.pose_type = pose_type;
    this.mask_image_name = mask_image_name;
  }
}

class FixHand {
  constructor(character_image_name, lora_type, prompt_text, pose_type) {
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
    this.prompt_text = prompt_text;
    this.pose_type = pose_type;
  }
}

class GeneratePoseVideo {
  constructor(character_image_name, lora_type, prompt_text, pose_type) {
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
    this.prompt_text = prompt_text;
    this.pose_type = pose_type;
  }
}

class MakeActionPoseNightly {
  constructor(
    character_image_name,
    prompt_text,
    lora_type,
    pose_type,
    mask_image_name
  ) {
    this.character_image_name = character_image_name;
    this.prompt_text = prompt_text;
    this.lora_type = lora_type;
    this.pose_type = pose_type;
    this.mask_image_name = mask_image_name;
  }
}

class FixHandNightly {
  constructor(character_image_name, lora_type, pose_type) {
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
    this.pose_type = pose_type;
  }
}

class GeneratePoseVideoNightly {
  constructor(character_image_name, pose_type) {
    this.character_image_name = character_image_name;
    this.pose_type = pose_type;
  }
}

class MakePoseVideo {
  constructor(character_image_name, lora_type, pose_type) {
    this.character_image_name = character_image_name;
    this.lora_type = lora_type;
    this.pose_type = pose_type;
  }
}

class GeneratePoseSpriteSheet {
  constructor(video_name) {
    this.video_name = video_name;
  }
}

class ImageToPrompt {
  constructor(image_name) {
    this.image_name = image_name;
  }
}

class Generate3DReqModel {
  constructor(image_name, type) {
    this.image_name = image_name;
    this.type = type;
  }
}

class RmbgReqModel {
  constructor(image_name, type) {
    this.image_name = image_name;
    this.type = type;
  }
}

class UpscaleReqModel {
  constructor(image_name, factor) {
    this.image_name = image_name;
    this.factor = factor;
  }
}

class SkyboxReqModel {
  constructor(prompt) {
    this.prompt = prompt;
  }
}

// Response Models
class PromptResponseData {
  constructor(workflowId, promptId, number) {
    this.workflowId = workflowId;
    this.promptId = promptId;
    this.number = number;
  }
}

class StatusTaskResponseData {
  constructor(id, promptId, complete, executionStart, createAt, updateAt) {
    this.id = id;
    this.promptId = promptId;
    this.complete = complete;
    this.executionStart = executionStart;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }
}

class StatusResponseData {
  constructor(id, createAt, tasks) {
    this.id = id;
    this.createAt = createAt;
    this.tasks = tasks;
  }
}

class HistoryTaskResponseData {
  constructor(
    id,
    promptId,
    complete,
    executionStart,
    outPuts,
    createAt,
    updateAt
  ) {
    this.id = id;
    this.promptId = promptId;
    this.complete = complete;
    this.executionStart = executionStart;
    this.outPuts = outPuts;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }
}

class HistoryItemResponseData {
  constructor(id, createAt, tasks) {
    this.id = id;
    this.createAt = createAt;
    this.tasks = tasks;
  }
}

class HistoryResponseData {
  constructor(items) {
    this.items = items;
  }
}

class PromptResponse {
  constructor(code, msg, data = null) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}

class StatusResponse {
  constructor(code, msg, data = null) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}

class HistoryResponse {
  constructor(code, msg, data = null) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}

class S3SignResponseData {
  constructor(extension, signature, downloadUrl) {
    this.extension = extension;
    this.signature = signature;
    this.downloadUrl = downloadUrl;
  }
}

class S3SignResponse {
  constructor(code, msg, data = null) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}

module.exports = {
  // Request Models
  GetTaskStatus,
  GetHistory,
  CharacterDesignStep1,
  CharacterDesignStep2,
  CharacterDesignStep3,
  Inpainting,
  Outpainting,
  MakeActionPose,
  FixHand,
  GeneratePoseVideo,
  MakeActionPoseNightly,
  FixHandNightly,
  GeneratePoseVideoNightly,
  MakePoseVideo,
  GeneratePoseSpriteSheet,
  ImageToPrompt,
  Generate3DReqModel,
  RmbgReqModel,
  UpscaleReqModel,
  SkyboxReqModel,

  // Response Models
  PromptResponseData,
  StatusTaskResponseData,
  StatusResponseData,
  HistoryTaskResponseData,
  HistoryItemResponseData,
  HistoryResponseData,
  PromptResponse,
  StatusResponse,
  HistoryResponse,
  S3SignResponseData,
  S3SignResponse,
};
