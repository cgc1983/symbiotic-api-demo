import config
import requests
import json
import requests
import json
import time
import hashlib
import hmac
from config import SECRET_ID, SECRET_KEY, API_HOST
import time
import os
from loguru import logger
from urllib.parse import urlparse
from models import S3SignResponse


def get_api_host() -> str:
    """Get API HOST from config"""
    return config.API_HOST


def create_signature(api_key: str, secret_key: str, timestamp: int, body: str = "") -> str:
    """
    Create signature
    
    Args:
        api_key: API key ID
        secret_key: Secret key
        timestamp: Timestamp
        body: Request body content
        
    Returns:
        str: Signature
    """
    message = f"{api_key}{timestamp}{body}"
    signature = hmac.new(
        secret_key.encode('utf-8'),
        message.encode('utf-8'),
        hashlib.sha256
    ).hexdigest()
    return signature


def get_auth_headers(api_key: str, secret_key: str, body: str = "") -> dict:
    """
    Get authentication headers
    
    Args:
        api_key: API key ID
        secret_key: Secret key
        body: Request body content
        
    Returns:
        dict: Dictionary containing authentication headers
    """
    timestamp = int(time.time())
    signature = create_signature(api_key, secret_key, timestamp, body)
    
    return {
        "X-API-Key": api_key,
        "X-Timestamp": str(timestamp),
        "X-Signature": signature
    } 

def make_authenticated_request(method, endpoint, data=None, params=None):
    """Universal authenticated request method"""
    url = f"{API_HOST}{endpoint}"
    
    if method.upper() == "GET":
        # GET requests use query string for signature
        query_string = ""
        if params:
            query_string = "&".join([f"{k}={v}" for k, v in params.items()])
        
        headers = get_auth_headers(SECRET_ID, SECRET_KEY, query_string)
        headers["Content-Type"] = "application/x-www-form-urlencoded"
        
        response = requests.get(url, headers=headers, params=params)
    else:
        # POST requests use request body for signature
        body = json.dumps(data) if data else ""
        headers = get_auth_headers(SECRET_ID, SECRET_KEY, body)
        headers["Content-Type"] = "application/json"
        
        response = requests.post(url, headers=headers, data=body)
    
    return response


def read_local_file(file_path: str) -> tuple[bytes, str]:
    """
    Read local file
    
    Args:
        file_path: Local file path
        
    Returns:
        tuple: (File binary data, file extension)
    """
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"File does not exist: {file_path}")
    
    with open(file_path, 'rb') as f:
        file_data = f.read()
    
    # Get file extension
    _, extension = os.path.splitext(file_path)
    extension = extension.lower().lstrip('.')
    
    return file_data, extension

def get_content_type(extension: str) -> str:
    """
    Get MIME type based on file extension
    
    Args:
        extension: File extension
        
    Returns:
        str: MIME type
    """
    mime_types = {
        'jpg': 'image/jpeg',
        'jpeg': 'image/jpeg',
        'png': 'image/png',
        'gif': 'image/gif',
        'bmp': 'image/bmp',
        'webp': 'image/webp',
        'mp4': 'video/mp4',
        'avi': 'video/x-msvideo',
        'mov': 'video/quicktime',
        'pdf': 'application/pdf',
        'txt': 'text/plain',
        'json': 'application/json',
    }
    
    return mime_types.get(extension.lower(), 'application/octet-stream')

def upload_file_to_s3(signature_url: str, file_data: bytes, content_type: str = "image/jpeg"):
    """
    Upload file to S3 using pre-signed URL
    
    Args:
        signature_url: S3 pre-signed upload URL
        file_data: File binary data
        content_type: File content type
        
    Returns:
        bool: Whether upload was successful
    """
    try:
        headers = {
            'Content-Type': content_type,
        }
        
        response = requests.put(signature_url, data=file_data, headers=headers)
        
        if response.status_code == 200:
            logger.info("File uploaded successfully")
            return True
        else:
            logger.error(f"File upload failed, status code: {response.status_code}")
            logger.error(f"Response content: {response.text}")
            return False
            
    except Exception as e:
        logger.error(f"Error occurred during upload: {str(e)}")
        return False


def get_filename_from_url(url):
    # Parse URL to get path component
    parsed_url = urlparse(url)
    path = parsed_url.path

    # Extract filename from path
    filename = os.path.basename(path)

    # Handle potential query parameters (if filename contains ? or #)
    if '?' in filename:
        filename = filename.split('?')[0]
    if '#' in filename:
        filename = filename.split('#')[0]

    return filename

def simple_upload_file_to_s3(file_path: str):
    """
    Upload file to S3 using pre-signed URL
    
    Args:
        file_path: Local file path to upload
    """
    try:
        file_data, extension = read_local_file(file_path)
        
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
        
        # 3. Get content type
        content_type = get_content_type(extension)
        logger.info(f"File content type: {content_type}")
        
        # 4. Upload file to S3
        success = upload_file_to_s3(signature, file_data, content_type)
        if success:
            return get_filename_from_url(response_model.data.downloadUrl)
        else:
            return None

    except Exception as e:
        logger.error(f"Error occurred during upload: {str(e)}")
        return None

