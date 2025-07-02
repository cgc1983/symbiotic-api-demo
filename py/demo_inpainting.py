import os.path

from config import SECRET_ID, SECRET_KEY,API_HOST,IMAGE_PATH,S3_BUCKET_BASE_URL
from utils import make_authenticated_request,simple_upload_file_to_s3
from models import Inpainting, PromptResponse, GetTaskStatus, StatusResponse, GetHistory, HistoryResponse
from loguru import logger
from models import Inpainting
import time

def main():
    logger.info("Starting inpainting process")
    # Upload mask image
    image_name = simple_upload_file_to_s3(os.path.join(IMAGE_PATH,"pic_1.png"))
    if image_name:
        logger.info(f"Image uploaded successfully, image name: {image_name}")
    else:
        logger.error("Failed to upload image")
        return

    # Execute inpainting
    request_model = Inpainting(
        description="girl",
        mask_name=image_name
    )
    response = make_authenticated_request(
        "POST", "/api/v1/inpainting", 
        data=request_model.model_dump()
    )
    prompt_model = PromptResponse(**response.json())
    if prompt_model.code != 0:
        logger.error(f"Inpainting execution failed, error message: {prompt_model.msg}")
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
    get_history_req = GetHistory(tool_id="4")
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
