package com.keduit.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFiles(String uploadPath, String originalFileName, byte[] fileData) throws IOException {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + saveFileName;
        FileOutputStream fileOutputStream = new FileOutputStream(fileUploadFullUrl);
        fileOutputStream.write(fileData);
        fileOutputStream.close();

        return saveFileName;
    }

    public void deleteFile(String filePath) throws IOException {

        File deleteFile = new File(filePath);
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
