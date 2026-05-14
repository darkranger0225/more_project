package com.school.communication.controller;

import com.school.communication.dto.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Value("${file.upload.access-url}")
    private String accessUrl;

    @Value("${file.upload.base-url:}")
    private String baseUrl;

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + extension;

            File destFile = new File(uploadDir, newFilename);
            file.transferTo(destFile);

            // 返回相对路径，前端根据当前域名自动拼接
            String fileUrl = accessUrl + newFilename;
            return Result.success(fileUrl);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}