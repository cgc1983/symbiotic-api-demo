from typing import List, Optional
from pydantic import BaseModel, Field


class GetTaskStatus(BaseModel):
    workflow_id: int = Field(..., description="WorkFlow ID")


class GetHistory(BaseModel):
    tool_id: str = Field(...,
                         description="Tool ID, for details, see: [Tool Description](http://49.233.159.47:4999/web/#/632681136/291199706)")
    page: int = Field(..., description="Page index (starting from 1)")
    page_size: int = Field(..., description="Number of history entries per page")


class TPose(BaseModel):
    character_name: str = Field(..., description="Character Name")
    character_detail: str = Field(..., description="Character Detail")
    lora_type: str = Field(..., description="Lora Type: 0:Realistic; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")


class CharacterSheetStep1(BaseModel):
    prompt_text: str = Field(..., description="Prompt Text")
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 0:Realistic; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")


class CharacterSheetStep2(BaseModel):
    prompt_text: str = Field(..., description="Prompt Text")
    character_image_name: str = Field(..., description="Character Image Name")
    video_name: str = Field(..., description="Video Name")
    lora_type: str = Field(..., description="Lora Type: 0:Realistic; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")


class Inpainting(BaseModel):
    description: str = Field(..., description="Description")
    mask_name: str = Field(..., description="Mask Image Name")


class Outpainting(BaseModel):
    description: str = Field(..., description="Description")
    image_name: str = Field(..., description="Image Name")
    left: int = Field(..., ge=0, description="Left Extend Size")
    top: int = Field(..., ge=0, description="Top Extend Size")
    right: int = Field(..., ge=0, description="Right Extend Size")
    bottom: int = Field(..., ge=0, description="Bottom Extend Size")


class MakeActionPose(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    prompt_text: str = Field(..., description="Prompt Text")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;")
    mask_image_name: str = Field(..., description="Mask Image Name")


class FixHand(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    prompt_text: str = Field(..., description="Prompt Text")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;")


class GeneratePoseVideo(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    prompt_text: str = Field(..., description="Prompt Text")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Idle left-facing; 1:Idle right-facing; 2:Walk to the left; 3:Walk to the right; 4:Run to the left; 5:Run to the right; 6:Punch to the left; 7:Punch to the right;")


class MakeActionPoseNightly(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    prompt_text: str = Field(..., description="Prompt Text")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right; ")
    mask_image_name: str = Field(..., description="Mask Image Name")


class FixHandNightly(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right; ")


class GeneratePoseVideoNightly(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    pose_type: str = Field(...,
                           description="Pose Type: 0:Walk to the left; 1:Walk to the right; 2:Run to the left; 3:Run to the right; ")


class MakePoseVideo(BaseModel):
    character_image_name: str = Field(..., description="Character Image Name")
    lora_type: str = Field(..., description="Lora Type: 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")
    pose_type: str = Field(..., description="Pose Type: refrence to file: Pose Description")


class GeneratePoseSpriteSheet(BaseModel):
    video_name: str = Field(..., description="Video Name")


class ImageToPrompt(BaseModel):
    image_name: str = Field(..., description="Image Name")


class Generate3DReqModel(BaseModel):
    image_name: str = Field(..., description="Image Name")
    type: str = Field(..., description="0 White Model, 1 White Model + Texture")


class RmbgReqModel(BaseModel):
    image_name: str = Field(..., description="Image Name")
    type: str = Field(..., description="Mode Type: 0: BiRefNet; 1: RMBG2.0;")


class UpscaleReqModel(BaseModel):
    image_name: str = Field(..., description="Image Name")
    factor: str = Field(..., description="Scale Factor: 0: 2X; 1: 4X;")


class SkyboxReqModel(BaseModel):
    prompt: str = Field(..., description="Prompt Text")


class ImageEditingReqModel(BaseModel):
    description: str = Field(..., description="Description about how to edit image")
    image_name: str = Field(..., description="Image Name")


class BrainStormReqModel(BaseModel):
    description: str = Field(..., description="Description for the theme")
    image_count: str = Field(..., description="Number of images generated this round: 2, 4, 8, 16")
    lora_type: str = Field(..., description="Lora Type: 0:Realistic; 1:Disney; 2:Pixel; 3:Anime; 4:Ghibli;")


class PromptResponseData(BaseModel):
    workflow_id: int = Field(..., description="WorkFlow ID")
    # promptId: str = Field(..., description="Task ID")
    # number: int = Field(..., description="No.")


class StatusTaskResponseData(BaseModel):
    id: int = Field(..., description="ID")
    # promptId: str = Field(..., description="Task ID")
    # userId: int = Field(..., description="User ID")
    complete: int = Field(...,
                          description="Completed status: 0: In Queue; 1: Completed; 2: Processing; 3: Error Occurred;")
    execution_start: int = Field(..., description="Execution start time")
    # outPuts: str = Field(..., description="Outputs")
    create_at: str = Field(..., description="Create Time")
    update_at: str = Field(..., description="Update Time")


class StatusResponseData(BaseModel):
    id: int = Field(..., description="ID")
    # user_id: int = Field(..., description="User ID")
    create_at: str = Field(..., description="Create Time")
    tasks: List[StatusTaskResponseData] = Field(..., description="Task List")
    # tplId: int = Field(..., description="Template ID")
    percentage: float = Field(..., description="percentage")


class HistoryTaskResponseData(BaseModel):
    id: int = Field(..., description="ID")
    # promptId: str = Field(..., description="Task ID")
    # userId: int = Field(..., description="User ID")
    complete: int = Field(...,
                          description="Completed status: 0: In Queue; 1: Completed; 2: Processing; 3: Error Occurred;")
    execution_start: int = Field(..., description="Execution start time")
    out_puts: List[str] = Field(..., description="Outputs; include Image, Video, Model etc.")
    create_at: str = Field(..., description="Create Time")
    update_at: str = Field(..., description="Update Time")


class HistoryItemResponseData(BaseModel):
    id: int = Field(..., description="ID")
    # user_id: int = Field(..., description="User ID")
    create_at: str = Field(..., description="Create Time")
    tasks: List[HistoryTaskResponseData] = Field(..., description="Task List")
    # tplId: int = Field(..., description="Template ID")
    percentage: float = Field(..., description="percentage")


class HistoryResponseData(BaseModel):
    items: List[HistoryItemResponseData] = Field(..., description="History List")


class S3SignResponseData(BaseModel):
    extension: str = Field(..., description="File extension")
    signature: str = Field(..., description="S3 signature URL")
    download_url: str = Field(..., description="Download URL")


class PromptResponse(BaseModel):
    code: int = Field(..., description="Error Code")
    msg: str = Field(..., description="Error Message")
    data: PromptResponseData = Field(None, description="Response Data")


class StatusResponse(BaseModel):
    code: int = Field(..., description="Error Code")
    msg: str = Field(..., description="Error Message")
    data: StatusResponseData = Field(None, description="Response Data")


class HistoryResponse(BaseModel):
    code: int = Field(..., description="Error Code")
    msg: str = Field(..., description="Error Message")
    data: HistoryResponseData = Field(None, description="Response Data")


class S3SignResponse(BaseModel):
    code: int = Field(..., description="Error Code")
    msg: str = Field(..., description="Error Message")
    data: S3SignResponseData = Field(None, description="Response Data")