package com.example.f5.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUpload {
    private String fileDir;


    /*파일 업로드*/
    public void uploadFile(MultipartFile multipartFile, String fileChildDir) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = extractExt(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName, fileChildDir)));
    }

    private static String extractExt(String originalFilename) {
        /*확장자명 추출*/
        String ext = creatStoreFilename(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private static String creatStoreFilename(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String getFullPath(String fileName, String fileChildDir) {
        FileUrl fileUrl = new FileUrl();
        fileDir = fileUrl.selectUrl() + fileChildDir;

        char firstChar = fileDir.charAt(0);
        char lastChar = fileDir.charAt(fileDir.length() - 1);

        String result = "";
        // 유닉스 또는 리눅스 경로의 경우
        if (firstChar == '/' && lastChar != '/') {
            result = fileDir + "/" + fileName;
        }
        // 윈도우즈 경로의 경우
        else if (firstChar == 'C' && lastChar != '\\') {
            result = fileDir + "\\" + fileName;
        }

        return result;
    }
}
