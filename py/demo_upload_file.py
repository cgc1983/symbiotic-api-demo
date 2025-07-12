from config import SECRET_ID, SECRET_KEY,API_HOST,IMAGE_PATH
from utils import make_authenticated_request,read_local_file,get_content_type,upload_file_to_s3
import requests
from loguru import logger
from models import S3SignResponse
import os
import tempfile
import io
import mimetypes




def main():

    file_data, extension = read_local_file(os.path.join(IMAGE_PATH, 'pic_1.png'))
    
    # 1. Get S3 signature URL
    logger.info(f"Getting S3 signature URL, file type: {extension}")
    response = make_authenticated_request(
        "GET", "/api/v1/image-signature", 
        params={"extension": extension}
    )
    
    if response.status_code != 200:
        logger.error(f"Failed to get signature URL, status code: {response.status_code}")
        logger.error(f"Response content: {response.text}")
        return
    
    response_model: S3SignResponse = S3SignResponse(**response.json())
    
    if response_model.code != 0:
        logger.error(f"API returned error: {response_model.msg}")
        return
    
    # 2. Get signature URL
    signature = response_model.data.signature
    download_url = response_model.data.download_url
    
    logger.info(f"Obtained signature URL: {signature}")
    logger.info(f"File extension: {extension}")
    logger.info(f"Download URL: {download_url}")
    
    # 3. Get content type
    content_type = get_content_type(extension)
    logger.info(f"File content type: {content_type}")
    
    # 4. Upload file to S3
    logger.info("Starting file upload to S3...")
    success = upload_file_to_s3(signature, file_data, content_type)
    
    if success:
        logger.info("File upload completed!")
        logger.info(f"File download link: {download_url}")
    else:
        logger.error("File upload failed!")

if __name__ == "__main__":
    main()
