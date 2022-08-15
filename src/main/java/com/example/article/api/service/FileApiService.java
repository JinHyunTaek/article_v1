package com.example.article.api.service;

import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.File;
import com.example.article.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.article.api.error.BasicErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FileApiService {

    @Value("${file.dir}")
    public String fileDir;

    public String getFullPath(String fileName){
        return fileDir+fileName;
    }

    private final FileRepository fileRepository;

    @Transactional
    public void saveFiles(List<MultipartFile> multipartFiles, Article article) {
        if(multipartFiles == null){
            return;
        }
        List<File> storeFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
//            if(multipartFile.isEmpty()){
//                System.out.println("multipart file is empty");
//                return;
//            }
            File file = saveFile(multipartFile, article);
            fileRepository.save(file);
            storeFiles.add(file);
        }
    }

    @Transactional
    public File saveFile(MultipartFile multipartFile,Article article)  {
        if(multipartFile.isEmpty()){
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);

        try{
            multipartFile.transferTo(new java.io.File(fullPath));
        }catch (IOException e){
            throw new BasicException(INTERNAL_SERVER_ERROR,e);
        }

        return File.builder()
                .article(article)
                .originalFilename(originalFilename)
                .storedFilename(storeFileName)
                .fileDirectory(fullPath)
                .build();
    }

    public void deleteFilesInDirectory(List<File> files) {
        for (File file : files) {
            String storedFilename = file.getStoredFilename();
            java.io.File storedFile = new java.io.File(getFullPath(storedFilename));
            storedFile.delete();
        }
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }

}
