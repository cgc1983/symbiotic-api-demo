# Symbiotic API Demo - Java Version

This is the Java equivalent of the Python and Go symbiotic API demo. It provides the same functionality for interacting with the Symbiotic API, including file uploads, inpainting, outpainting, pose generation, and more.

## Project Structure

```
java/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── symbiotic/
│       │           ├── config/
│       │           │   └── Config.java          # Configuration constants and paths
│       │           ├── models/
│       │           │   └── Models.java          # Request and response data structures
│       │           ├── utils/
│       │           │   └── Utils.java           # Utility functions for API calls and file handling
│       │           └── demo/
│       │               ├── DemoUploadFile.java        # File upload demo
│       │               ├── DemoInpainting.java        # Inpainting demo
│       │               ├── DemoOutpainting.java       # Outpainting demo
│       │               ├── DemoMakeActionPose.java    # Make action pose demo
│       │               ├── DemoFixHand.java           # Fix hand demo
│       │               └── DemoGeneratePoseVideo.java # Generate pose video demo
│       └── resources/
│           └── logback.xml                    # Logging configuration
├── pom.xml                                   # Maven project configuration
└── README.md                                 # This file
```

## Prerequisites

- Java 11 or later
- Maven 3.6 or later
- Access to the Symbiotic API

## Installation

1. Navigate to the `java` directory:

   ```bash
   cd java
   ```

2. Build the project:
   ```bash
   mvn clean compile
   ```

## Usage

Each demo is a separate executable. To run any demo:

```bash
# File upload demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoUploadFile"

# Inpainting demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoInpainting"

# Outpainting demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoOutpainting"

# Make action pose demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoMakeActionPose"

# Fix hand demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoFixHand"

# Generate pose video demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoGeneratePoseVideo"
```

Alternatively, you can build a fat JAR and run it directly:

```bash
# Build fat JAR
mvn clean package

# Run demos
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoUploadFile
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoInpainting
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoOutpainting
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoMakeActionPose
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoFixHand
java -cp target/symbiotic-api-demo-1.0.0.jar com.symbiotic.demo.DemoGeneratePoseVideo
```

## Configuration

The API configuration is in `src/main/java/com/symbiotic/config/Config.java`. You may need to update:

- `API_HOST`: The API server URL
- `SECRET_ID` and `SECRET_KEY`: Your API credentials
- `S3_BUCKET_BASE_URL`: S3 bucket base URL for downloads

## Features

### Core Utilities (`Utils.java`)

- **Authentication**: HMAC-SHA256 signature generation
- **File Handling**: Local file reading and S3 upload
- **API Requests**: Authenticated HTTP requests with proper headers
- **Content Type Detection**: MIME type mapping for various file formats
- **S3 Integration**: Pre-signed URL upload workflow
- **Task Management**: Status checking and completion waiting

### Models (`Models.java`)

- **Request Models**: All API request structures with Jackson annotations
- **Response Models**: All API response structures with validation
- **Data Models**: Internal data structures for API communication

### Demos

Each demo demonstrates a specific API functionality:

1. **File Upload** (`DemoUploadFile.java`): Upload files to S3 using pre-signed URLs
2. **Inpainting** (`DemoInpainting.java`): Fill masked areas in images with AI-generated content
3. **Outpainting** (`DemoOutpainting.java`): Extend images beyond their boundaries
4. **Make Action Pose** (`DemoMakeActionPose.java`): Generate character poses with masks
5. **Fix Hand** (`DemoFixHand.java`): Improve hand positioning in character images
6. **Generate Pose Video** (`DemoGeneratePoseVideo.java`): Create videos from character poses

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
- Proper exception propagation

## Dependencies

- `org.apache.httpcomponents:httpclient`: HTTP client for API calls
- `com.fasterxml.jackson.core:jackson-databind`: JSON processing
- `org.slf4j:slf4j-api`: Logging facade
- `ch.qos.logback:logback-classic`: Logging implementation

## File Requirements

The demos expect certain files to exist in the `../images/` directory:

- `pic_1.png`: Main character image
- `pose_mask_0.png`: Pose mask for action pose generation
- `mask.png`: Mask for inpainting (if using inpainting demo)

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
- `POST /api/v1/make-action-pose`: Generate action poses
- `POST /api/v1/fix-hand`: Fix hand positioning
- `POST /api/v1/generate-pose-video`: Generate pose videos
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

The project uses SLF4J with Logback for structured logging with:

- Timestamp formatting
- Error tracking
- Progress monitoring
- Debug information

## Development

To extend the project:

1. Add new request/response models to `Models.java`
2. Create utility functions in `Utils.java`
3. Implement new demo classes following the existing pattern
4. Update configuration as needed

## Comparison with Python and Go Versions

This Java version provides the same functionality as the Python and Go versions with:

- Strong typing through Java classes
- Better error handling with checked exceptions
- Thread-safe design
- Maven dependency management
- Comprehensive logging with SLF4J/Logback
- Jackson JSON processing for robust serialization

## Troubleshooting

Common issues:

- **File not found**: Ensure required images exist in `../images/`
- **Authentication errors**: Verify `SECRET_ID` and `SECRET_KEY` in `Config.java`
- **API errors**: Check API server status and endpoint URLs
- **S3 upload failures**: Verify network connectivity and S3 permissions
- **Build errors**: Ensure Java 11+ and Maven are properly installed

## Building and Running

```bash
# Clean and compile
mvn clean compile

# Run tests (if any)
mvn test

# Package into JAR
mvn package

# Run specific demo
mvn exec:java -Dexec.mainClass="com.symbiotic.demo.DemoUploadFile"
```
