package com.clientApiVoiture.ClientApiVoiture.client;

import org.springframework.web.client.RestTemplate;

public class VoitureClient {

    public static void getVoitures(){
        String url = "http://localhost:8080/Voitures";
        RestTemplate restTemplate = new RestTemplate();
        String test = restTemplate.getForObject(url, String.class);
        System.out.println(test);
    }
}
