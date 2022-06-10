package com.example.demo.service;

import com.example.demo.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile multipartFile, String fileName, int type);

    ImageModel getImage(int id);
}
