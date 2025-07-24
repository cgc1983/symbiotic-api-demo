import os
import sys
import uuid
import platform


API_HOST = 'https://api.symbiotic-labs.xyz'
ROOT_PATH = os.path.dirname(os.path.abspath(__file__))
PARENT_PATH = os.path.dirname(ROOT_PATH)
IMAGE_PATH = os.path.join(PARENT_PATH, 'images')
VIDEO_PATH = os.path.join(PARENT_PATH, 'videos')

S3_BUCKET_BASE_URL = 'https://comfyprod-1365981877.cos.ap-singapore.myqcloud.com/temp'


# secret id
SECRET_ID = 't1'
# secret key
SECRET_KEY = 'test_secret_key_001'
