# 文件上传功能使用说明

这个脚本实现了使用 AWS S3 预签名 URL 上传文件的功能。

## 功能特性

- 支持上传本地文件
- 支持创建测试图片
- 自动检测文件类型和 MIME 类型
- 完整的错误处理和日志记录

## 安装依赖

```bash
pip install -r requirements.txt
```

## 使用方法

### 1. 上传本地文件

```bash
python demo_upload_file.py --file /path/to/your/image.jpg
```

### 2. 使用测试图片

```bash
python demo_upload_file.py --test
```

### 3. 指定文件扩展名（使用测试图片）

```bash
python demo_upload_file.py --extension png
```

### 4. 查看帮助

```bash
python demo_upload_file.py --help
```

## 参数说明

- `--file, -f`: 指定要上传的本地文件路径
- `--extension, -e`: 指定文件扩展名（默认: jpg）
- `--test, -t`: 使用测试图片而不是本地文件

## 支持的文件类型

- 图片: jpg, jpeg, png, gif, bmp, webp
- 视频: mp4, avi, mov
- 文档: pdf, txt, json
- 其他: 自动识别为 application/octet-stream

## 工作流程

1. 获取 S3 预签名 URL
2. 读取文件数据
3. 确定文件 MIME 类型
4. 使用 PUT 请求上传到 S3
5. 返回下载链接

## 示例输出

```
2024-01-01 12:00:00.000 | INFO     | __main__:main:123 | 开始文件上传流程
2024-01-01 12:00:00.001 | INFO     | __main__:main:145 | 准备上传本地文件: /path/to/image.jpg
2024-01-01 12:00:00.002 | INFO     | __main__:main:147 | 文件大小: 102400 字节
2024-01-01 12:00:00.003 | INFO     | __main__:main:155 | 获取S3签名URL，文件类型: jpg
2024-01-01 12:00:00.004 | INFO     | __main__:main:170 | 获取到签名URL: https://...
2024-01-01 12:00:00.005 | INFO     | __main__:main:172 | 文件扩展名: jpg
2024-01-01 12:00:00.006 | INFO     | __main__:main:173 | 下载URL: https://...
2024-01-01 12:00:00.007 | INFO     | __main__:main:177 | 文件内容类型: image/jpeg
2024-01-01 12:00:00.008 | INFO     | __main__:main:179 | 开始上传文件到S3...
2024-01-01 12:00:00.009 | INFO     | __main__:upload_file_to_s3:89 | 文件上传成功
2024-01-01 12:00:00.010 | INFO     | __main__:main:182 | 文件上传完成！
2024-01-01 12:00:00.011 | INFO     | __main__:main:183 | 文件下载链接: https://...
```
