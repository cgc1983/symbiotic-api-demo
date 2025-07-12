# Symbiotic API Demo - Node.js Version

This Node.js project demonstrates how to interact with the Symbiotic API, providing functionality for file uploads, image processing, character pose generation, and more. It's the Node.js equivalent of the Python and Go symbiotic API demos.

## Project Structure

```
nodejs/
├── config/
│   └── config.js                    # Configuration constants and paths
├── models/
│   └── models.js                    # Request and response data structures
├── utils/
│   └── utils.js                     # Utility functions for API calls and file handling
├── demos/
│   ├── demo_upload_file.js          # File upload demo
│   ├── demo_inpainting.js           # Inpainting demo
│   ├── demo_outpainting.js          # Outpainting demo
├── package.json                     # Node.js dependencies and scripts
└── README.md                        # This file
```

## Prerequisites

- Node.js 16.0 or later
- Access to the Symbiotic API
- Required image files in the `../images/` directory

## Installation

1. Navigate to the `nodejs` directory:

   ```bash
   cd nodejs
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Usage

Each demo can be run independently using npm scripts:

```bash
# File upload demo
npm run demo:upload

# Inpainting demo
npm run demo:inpainting

# Outpainting demo
npm run demo:outpainting

```

Or run directly with Node.js:

```bash
# File upload demo
node demos/demo_upload_file.js

# Inpainting demo
node demos/demo_inpainting.js

# Outpainting demo
node demos/demo_outpainting.js

```

## Configuration

The API configuration is in `config/config.js`. You may need to update:

- `API_HOST`: The API server URL
- `SECRET_ID` and `SECRET_KEY`: Your API credentials
- `S3_BUCKET_BASE_URL`: S3 bucket base URL for downloads

## Features

### Core Utilities (`utils/utils.js`)

- **Authentication**: HMAC-SHA256 signature generation
- **File Handling**: Local file reading and S3 upload
- **API Requests**: Authenticated HTTP requests with proper headers
- **Content Type Detection**: MIME type mapping for various file formats
- **S3 Integration**: Pre-signed URL upload workflow
- **Logging**: Structured logging with Winston
- **Task Management**: Polling for task completion and history retrieval

### Models (`models/models.js`)

- **Request Models**: All API request structures using ES6 classes
- **Response Models**: All API response structures with validation
- **Data Models**: Internal data structures for API communication

### Demos

Each demo demonstrates a specific API functionality:

1. **File Upload** (`demo_upload_file.js`): Upload files to S3 using pre-signed URLs
2. **Inpainting** (`demo_inpainting.js`): Fill masked areas in images with AI-generated content
3. **Outpainting** (`demo_outpainting.js`): Extend images beyond their boundaries

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
- Structured logging with error tracking

## Dependencies

- `axios`: HTTP client for API requests
- `crypto`: Built-in Node.js crypto module for HMAC signatures
- `mime-types`: MIME type detection
- `winston`: Structured logging
- `form-data`: Form data handling (if needed)

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

The project uses Winston for structured logging with:

- Timestamp formatting
- Error tracking
- Progress monitoring
- Debug information
- File and console output

## Development

To extend the project:

1. Add new request/response models to `models/models.js`
2. Create utility functions in `utils/utils.js`
3. Implement new demo scripts following the existing pattern
4. Update configuration as needed
5. Add new npm scripts to `package.json`

## Comparison with Python and Go Versions

This Node.js version provides the same functionality as the Python and Go versions with:

- **Async/Await**: Modern JavaScript async programming
- **ES6 Classes**: Clean object-oriented design
- **Promise-based**: Non-blocking I/O operations
- **NPM Ecosystem**: Rich package management
- **Event-driven**: Node.js event loop architecture
- **Cross-platform**: Runs on Windows, macOS, and Linux

## Troubleshooting

Common issues:

- **File not found**: Ensure required images exist in `../images/`
- **Authentication errors**: Verify `SECRET_ID` and `SECRET_KEY` in `config/config.js`
- **API errors**: Check API server status and endpoint URLs
- **S3 upload failures**: Verify network connectivity and S3 permissions
- **Module not found**: Run `npm install` to install dependencies

## Performance Considerations

- **Concurrent Requests**: Node.js handles concurrent API requests efficiently
- **Memory Management**: Proper cleanup of file buffers and streams
- **Connection Pooling**: Axios handles connection reuse automatically
- **Error Recovery**: Retry logic with exponential backoff

## Security

- **HMAC Authentication**: Secure API authentication
- **Input Validation**: Model validation for request data
- **Error Handling**: Secure error messages without sensitive data exposure
- **File Validation**: MIME type checking for uploads
