import os.path

from config import SECRET_ID, SECRET_KEY,API_HOST,IMAGE_PATH,S3_BUCKET_BASE_URL
from utils import make_authenticated_request,simple_upload_file_to_s3
from models import MakeActionPose, PromptResponse, GetTaskStatus, StatusResponse, GetHistory, HistoryResponse
from loguru import logger
import time

def main():
    logger.info("Starting make-action-pose process")
    
    # Upload character image
    character_image_name = simple_upload_file_to_s3(os.path.join(IMAGE_PATH,"pic_1.png"))
    if character_image_name:
        logger.info(f"Character image uploaded successfully, image name: {character_image_name}")
    else:
        logger.error("Failed to upload character image")
        return

    # Upload mask image
    mask_image_name = simple_upload_file_to_s3(os.path.join(IMAGE_PATH,"pose_mask_0.png"))
    if mask_image_name:
        logger.info(f"Mask image uploaded successfully, image name: {mask_image_name}")
    else:
        logger.error("Failed to upload mask image")
        return

    # Execute make-action-pose
    request_model = MakeActionPose(
        character_image_name=character_image_name,
        lora_type="1",  # 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;
        prompt_text="A beautiful character in action pose",
        pose_type="0",  # 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;
        mask_image_name=mask_image_name
    )
    response = make_authenticated_request(
        "POST", "/api/v1/make-action-pose", 
        data=request_model.model_dump()
    )
    prompt_model = PromptResponse(**response.json())
    if prompt_model.code != 0:
        logger.error(f"Make-action-pose execution failed, error message: {prompt_model.msg}")
        return
    prompt_id = prompt_model.data.promptId

    # Check status
    get_status_req = GetTaskStatus(promptId=prompt_id)
    while True:
        status_model = make_authenticated_request(
            "GET", "/api/v1/get-task-status",
            params=get_status_req.model_dump()
        )
        status_model = StatusResponse(**status_model.json())
        if status_model.data.tasks[0].complete == 1:
            break
        else:
            logger.info("Task not completed, please wait...")
            time.sleep(5)

    # Get history
    get_history_req = GetHistory(tool_id="5")  # Assuming tool_id for make-action-pose is 5
    response = make_authenticated_request(
        "GET", "/api/v1/get-task-history",
        params=get_history_req.model_dump()
    )

    logger.info(f"Getting history: {response.json()}")
    history_model = HistoryResponse(**response.json())
    if history_model.code != 0:
        logger.error(f"Failed to get history, error message: {history_model.msg}")
        return
    for item in history_model.data.items:
        logger.info(f"History ID: {item.id}")
        tasks = item.tasks
        for task in tasks:
            logger.info(f"Task ID: {task.id}")
            logger.info(f"Task status: {task.complete}")
            logger.info(f"Task start time: {task.executionStart}")
            logger.info(f"Task update time: {task.updateAt}")
            logger.info(f"Task creation time: {task.createAt}")
            for output in task.outPuts:
                download_url = f"{S3_BUCKET_BASE_URL}/{output}"
                logger.info(f"Download link: {download_url}")


if __name__ == "__main__":
    main()
