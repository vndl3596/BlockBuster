package com.example.demo.service;

import com.example.demo.model.ImageModel;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    JSONObject uploadImage(MultipartFile multipartFile, String fileName, int type);

    ImageModel getImage(String id);
}
