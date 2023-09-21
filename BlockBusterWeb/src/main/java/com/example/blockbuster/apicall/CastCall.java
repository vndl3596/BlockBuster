package com.example.blockbuster.apicall;

import com.example.blockbuster.dto.CastDTO;
import com.example.blockbuster.dto.ImageDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class CastCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static CastDTO getCastById(int actor) {
        String uriCast = "http://localhost:8080/api/cast/" + actor;
        ResponseEntity<CastDTO> responseCast = restTemplate.getForEntity(uriCast, CastDTO.class);
        CastDTO cast = responseCast.getBody();
        if (cast != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("url", cast.getAvatar());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            String urlImage = "http://localhost:8080/getImage";
            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(urlImage, request, ImageDTO.class);
            cast.setAvatar(response.getBody().getUrl());
        }
        return cast;
    }
}
