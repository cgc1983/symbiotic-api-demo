# Symbiotic API Demo - Go Version

This is the Go equivalent of the Python symbiotic API demo. It provides the same functionality for interacting with the Symbiotic API, including file uploads, inpainting, outpainting, pose generation, and more.

## Project Structure

```
go/
├── config/
│   └── config.go          # Configuration constants and paths
├── models/
│   └── models.go          # Request and response data structures
├── utils/
│   └── utils.go           # Utility functions for API calls and file handling
├── cmd/
│   ├── demo_upload_file/
│   │   └── main.go        # File upload demo
│   ├── demo_inpainting/
│   │   └── main.go        # Inpainting demo
│   ├── demo_outpainting/
│   │   └── main.go        # Outpainting demo
│   ├── demo_make_action_pose/
│   │   └── main.go        # Make action pose demo
│   ├── demo_fix_hand/
│   │   └── main.go        # Fix hand demo
│   └── demo_generate_pose_video/
│       └── main.go        # Generate pose video demo
├── go.mod                 # Go module file
└── README.md              # This file
```

## Prerequisites

- Go 1.21 or later
- Access to the Symbiotic API

## Installation

1. Navigate to the `go` directory:

   ```bash
   cd go
   ```

2. Install dependencies:
   ```bash
   go mod tidy
   ```

## Usage

Each demo is a separate executable. To run any demo:

```bash
# File upload demo
go run cmd/demo_upload_file/main.go

# Inpainting demo
go run cmd/demo_inpainting/main.go

# Outpainting demo
go run cmd/demo_outpainting/main.go

# Make action pose demo
go run cmd/demo_make_action_pose/main.go

# Fix hand demo
go run cmd/demo_fix_hand/main.go

# Generate pose video demo
go run cmd/demo_generate_pose_video/main.go
```

## Configuration

The API configuration is in `config/config.go`. You may need to update:

- `APIHost`: The API server URL
- `SecretID` and `SecretKey`: Your API credentials
- `S3BucketBaseURL`: S3 bucket base URL for downloads

## Features

### Core Utilities (`utils/utils.go`)

- **Authentication**: HMAC-SHA256 signature generation
- **File Handling**: Local file reading and S3 upload
- **API Requests**: Authenticated HTTP requests
- **Content Type Detection**: MIME type mapping

### Models (`models/models.go`)

- **Request Models**: All API request structures
- **Response Models**: All API response structures
- **Data Models**: Internal data structures

### Demos

Each demo demonstrates a specific API functionality:

1. **File Upload**: Upload files to S3 using pre-signed URLs
2. **Inpainting**: Fill masked areas in images
3. **Outpainting**: Extend images beyond their boundaries
4. **Make Action Pose**: Generate character poses with masks
5. **Fix Hand**: Improve hand positioning in character images
6. **Generate Pose Video**: Create videos from character poses

## API Workflow

Most demos follow this pattern:

1. Upload required images to S3
2. Submit API request with image names
3. Poll for task completion
4. Retrieve results from history

## Error Handling

The code includes comprehensive error handling:

- File not found errors
- API request failures
- Response parsing errors
- Task status checking

## Dependencies

- `github.com/sirupsen/logrus`: Structured logging
- `github.com/google/uuid`: UUID generation (if needed)

## Comparison with Python Version

This Go version provides the same functionality as the Python version with:

- Strong typing through Go structs
- Better error handling
- Concurrent-friendly design
- Smaller executable size
- Better performance for high-throughput scenarios
