package com.example.demo.controller;

import com.example.demo.model.ImageModel;
import com.example.demo.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.util.AppConstants.DEFAULT_IMAGE_MOVIE;

@RestController
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String fileUploadImage(@RequestParam("image") MultipartFile multipartFile, @RequestParam("name") String name) {
        return imageService.uploadImage(multipartFile, name, DEFAULT_IMAGE_MOVIE);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<ImageModel> getImage(@PathVariable("id") int id) {
        return new ResponseEntity<>(imageService.getImage(id), HttpStatus.OK);
    }
}
