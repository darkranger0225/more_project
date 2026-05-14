package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload-path:#{null}}")
    private String externalUploadPath;

    @PostMapping("/image")
    public ResultVO<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultVO.error("请选择要上传的文件");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return ResultVO.error("文件名无效");
        }

        String fileExtension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            fileExtension = originalFilename.substring(lastDotIndex).toLowerCase();
        }

        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
        boolean isAllowed = false;
        for (String ext : allowedExtensions) {
            if (ext.equals(fileExtension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            return ResultVO.error("只支持 jpg, jpeg, png, gif, webp 格式的图片");
        }

        String newFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            String uploadDir;
            String imageUrl;

            if (externalUploadPath != null && !externalUploadPath.isEmpty()) {
                uploadDir = externalUploadPath;
                imageUrl = "/upload/" + newFilename;
            } else {
                String projectPath = System.getProperty("user.dir");
                uploadDir = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "dishes";
                imageUrl = "/images/dishes/" + newFilename;
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFilename);
            file.transferTo(filePath.toFile());

            return ResultVO.success(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.error("文件上传失败: " + e.getMessage());
        }
    }
}
