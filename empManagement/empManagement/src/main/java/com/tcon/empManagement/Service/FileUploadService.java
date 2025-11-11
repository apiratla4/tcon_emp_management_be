package com.tcon.empManagement.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    String uploadFile(MultipartFile file);

    List<String> uploadMultipleFiles(List<MultipartFile> files);

    byte[] downloadFile(String fileName);

    boolean deleteFile(String fileName);

    List<String> getAllFiles();
}
