// Request Models
class GetTaskStatus {
  constructor(workflow_id) {
    this.workflow_id = workflow_id;
  }
}

class GetHistory {
  constructor(tool_id) {
    this.tool_id = tool_id;
    this.page = this.page;
    this.page_size = this.page_size
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


// Response Models
class PromptResponseData {
  constructor(workflow_id) {
    this.workflow_id = workflow_id;
  }
}

class StatusTaskResponseData {
  constructor(id, complete, execution_start, create_at, update_at) {
    this.id = id;
    this.complete = complete;
    this.execution_start = execution_start;
    this.create_at = create_at;
    this.update_at = update_at;
  }
}

class StatusResponseData {
  constructor(id, create_at, tasks, percentage) {
    this.id = id;
    this.create_at = create_at;
    this.tasks = tasks;
    this.percentage = percentage;
  }
}

class HistoryTaskResponseData {
  constructor(
    id,
    complete,
    execution_start,
    out_puts,
    create_at,
    update_at
  ) {
    this.id = id;
    this.promptId = promptId;
    this.complete = complete;
    this.execution_start = execution_start;
    this.out_puts = out_puts;
    this.create_at = create_at;
    this.update_at = update_at;
  }
}

class HistoryItemResponseData {
  constructor(id, create_at, tasks, percentage) {
    this.id = id;
    this.create_at = create_at;
    this.tasks = tasks;
    this.percentage = percentage;
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
  constructor(extension, signature, download_url) {
    this.extension = extension;
    this.signature = signature;
    this.download_url = download_url;
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
  Inpainting,
  Outpainting,

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
