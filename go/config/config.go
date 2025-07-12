package config

import (
	"os"
	"path/filepath"
)

const (
	APIHost = "http://127.0.0.1:8000"
	
	// Secret credentials
	SecretID  = "t1"
	SecretKey = "test_secret_key_001"
	
	// S3 bucket base URL
	S3BucketBaseURL = "https://comfyprod.s3.ap-northeast-1.amazonaws.com/temp/"
)

var (
	RootPath   string
	ParentPath string
	ImagePath  string
	VideoPath  string
)

func init() {
	// Get current working directory
	wd, err := os.Getwd()
	if err != nil {
		panic(err)
	}
	
	// Set paths
	RootPath = wd
	ParentPath = filepath.Dir(RootPath)
	ImagePath = filepath.Join(ParentPath, "images")
	VideoPath = filepath.Join(ParentPath, "videos")
} 