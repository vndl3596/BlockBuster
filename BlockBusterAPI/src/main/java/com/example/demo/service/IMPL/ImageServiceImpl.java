package com.example.demo.service.IMPL;

import com.example.demo.model.ImageModel;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.ImageService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import static com.example.demo.util.AppConstants.*;
import static com.example.demo.util.DataUtils.decompressZLib;
import static org.apache.lucene.index.IndexFileNames.getExtension;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final AccountRepository accountRepository;
    @Override
    public JSONObject uploadImage(MultipartFile multipartFile, String fileName, int type) {
        JSONObject path = new JSONObject();
        String extension = getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        File fileRootDir = new File("./");
        if (type == DEFAULT_IMAGE_ACCOUNTS) {
            fileRootDir = new File(DEFAULT_URL_DATA_IMAGE_ACCOUNT);
        } else if (type == DEFAULT_IMAGE_MOVIE) {
            fileRootDir = new File(DEFAULT_URL_DATA_IMAGE_MOVIE);
        } else if (type == DEFAULT_IMAGE_CAST) {
            fileRootDir = new File(DEFAULT_URL_DATA_IMAGE_CAST);
        } else if (type == DEFAULT_IMAGE_DIRECTOR) {
            fileRootDir = new File(DEFAULT_URL_DATA_IMAGE_DIRECTOR);
        }
        if (!fileRootDir.exists()) {
            fileRootDir.mkdir();
        }
        try {
            // Tạo file tại Server.
            File serverFile = new File(fileRootDir.getAbsolutePath() + File.separator + fileName + "." + extension);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(multipartFile.getBytes());
            stream.close();
            path.put("path", "." + getExtension(serverFile.getPath()));
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ImageModel getImage(String url) {
        ImageModel imageModel = new ImageModel();
        File file = new File(url);
        if (file.exists()){
            imageModel.setImgName(file.getName());
            imageModel.setUrl(decompressZLib(file));
            return imageModel;
        }
        return null;
    }
}

