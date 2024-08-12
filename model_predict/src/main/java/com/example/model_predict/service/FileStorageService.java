package com.example.model_predict.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * @author Dr.Awe
 * @date 2024-07-26 18:11
 */

public interface FileStorageService {
    public String storeFile(MultipartFile file);
    public Path getFile(String fileName);
}
