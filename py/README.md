# Symbiotic API Demo - Python Version

This Python project demonstrates how to interact with the Symbiotic API, providing functionality for file uploads, image processing, character pose generation, and more.

## Project Structure

```
py/
├── config.py                    # Configuration constants and paths
├── models.py                    # Request and response data structures
├── utils.py                     # Utility functions for API calls and file handling
├── demo_upload_file.py          # File upload demo
├── demo_inpainting.py           # Inpainting demo
├── demo_outpainting.py          # Outpainting demo
├── demo_make_action_pose.py     # Make action pose demo
├── demo_fix_hand.py             # Fix hand demo
├── demo_generate_pose_video.py  # Generate pose video demo
├── requirements.txt             # Python dependencies
└── README.md                    # This file
```

## Prerequisites

- Python 3.8 or later
- Access to the Symbiotic API
- Required image files in the `../images/` directory

## Installation

1. Navigate to the `py` directory:

   ```bash
   cd py
   ```

2. Create a virtual environment (recommended):

   ```bash
   python -m venv .venv
   source .venv/bin/activate  # On Windows: .venv\Scripts\activate
   ```

3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Usage

Each demo can be run independently:

```bash
# File upload demo
python demo_upload_file.py

# Inpainting demo
python demo_inpainting.py

# Outpainting demo
python demo_outpainting.py

```

## Configuration

The API configuration is in `config.py`. You may need to update:

- `API_HOST`: The API server URL
- `SECRET_ID` and `SECRET_KEY`: Your API credentials
- `S3_BUCKET_BASE_URL`: S3 bucket base URL for downloads

## Features

### Core Utilities (`utils.py`)

- **Authentication**: HMAC-SHA256 signature generation
- **File Handling**: Local file reading and S3 upload
- **API Requests**: Authenticated HTTP requests with proper headers
- **Content Type Detection**: MIME type mapping for various file formats
- **S3 Integration**: Pre-signed URL upload workflow

### Models (`models.py`)

- **Request Models**: All API request structures using Pydantic
- **Response Models**: All API response structures with validation
- **Data Models**: Internal data structures for API communication

### Demos

Each demo demonstrates a specific API functionality:

1. **File Upload** (`demo_upload_file.py`): Upload files to S3 using pre-signed URLs
2. **Inpainting** (`demo_inpainting.py`): Fill masked areas in images with AI-generated content
3. **Outpainting** (`demo_outpainting.py`): Extend images beyond their boundaries

## API Workflow

Most demos follow this pattern:

1. Upload required images to S3 using pre-signed URLs
2. Submit API request with uploaded image names
3. Poll for task completion status
4. Retrieve results from task history

## Error Handling

The code includes comprehensive error handling:

- File not found errors
- API request failures
- Response parsing errors
- Task status checking with retry logic

## Dependencies

- `loguru>=0.7.0`: Advanced logging with structured output
- `pydantic>=2.5.0`: Data validation and settings management
- `requests>=2.31.0`: HTTP library for API calls

## File Requirements

The demos expect certain files to exist in the `../images/` directory:

- `pic_1.png`: Main character image
- `pose_mask_0.png`: Pose mask for action pose generation

## Authentication

The API uses HMAC-SHA256 authentication with:

- API Key ID (`X-API-Key` header)
- Timestamp (`X-Timestamp` header)
- Signature (`X-Signature` header)

The signature is generated from: `{api_key}{timestamp}{body}`

## API Endpoints

The demos interact with these API endpoints:

- `GET /api/v1/image-signature`: Get S3 pre-signed upload URL
- `POST /api/v1/inpainting`: Execute inpainting
- `POST /api/v1/outpainting`: Execute outpainting
- `GET /api/v1/get-task-status`: Check task completion status
- `GET /api/v1/get-task-history`: Retrieve task history

## Character Design Features

The API supports various character design options:

- **Lora Types**: 1:Disney, 2:Pixel, 3:Anime, 4:Ghibli
- **Pose Types**: Idle, Walk, Run, Punch (left/right facing)
- **Character Design Steps**: Multi-step character creation process

## S3 Integration

Files are uploaded to S3 using pre-signed URLs:

1. Request signature URL from API
2. Upload file directly to S3
3. Use returned filename in subsequent API calls

## Logging

The project uses Loguru for structured logging with:

- Timestamp formatting
- Error tracking
- Progress monitoring
- Debug information

## Development

To extend the project:

1. Add new request/response models to `models.py`
2. Create utility functions in `utils.py`
3. Implement new demo scripts following the existing pattern
4. Update configuration as needed

## Troubleshooting

Common issues:

- **File not found**: Ensure required images exist in `../images/`
- **Authentication errors**: Verify `SECRET_ID` and `SECRET_KEY` in `config.py`
- **API errors**: Check API server status and endpoint URLs
- **S3 upload failures**: Verify network connectivity and S3 permissions
