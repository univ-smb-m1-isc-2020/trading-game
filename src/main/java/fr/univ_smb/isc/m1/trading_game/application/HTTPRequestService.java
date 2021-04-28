package fr.univ_smb.isc.m1.trading_game.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class HTTPRequestService {
    public String getRequestResponse(URI uri){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.getForEntity(uri, String.class).getBody());
        return restTemplate.getForEntity(uri, String.class).getBody();
    }
}
