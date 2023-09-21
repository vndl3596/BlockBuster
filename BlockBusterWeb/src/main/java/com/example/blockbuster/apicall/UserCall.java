package com.example.blockbuster.apicall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserCall {
    private static RestTemplate restTemplate = new RestTemplate();

    public static String forgetPassword(String mail) {
        String urlForget = "http://localhost:8080/api/acc/forgotPassword/" + mail;
        ResponseEntity<String> responseForget = restTemplate.postForEntity(urlForget, null, String.class);
        return responseForget.getBody();
    }

}
