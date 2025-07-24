package config

import (
	"os"
	"path/filepath"
)

const (
	APIHost = "https://api.symbiotic-labs.xyz/"

	// Secret credentials
	SecretID  = "t1"
	SecretKey = "test_secret_key_001"

	// S3 bucket base URL
	S3BucketBaseURL = "https://comfyprod-1365981877.cos.ap-singapore.myqcloud.com/"
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
