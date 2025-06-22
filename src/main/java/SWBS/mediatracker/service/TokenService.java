package SWBS.mediatracker.service;

import SWBS.mediatracker.config.SwbcAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TokenService {

    @Autowired
    private SwbcAuthProperties authProperties;

    private final RestTemplate restTemplate = new RestTemplate();


    public String getBearerToken() {
        String url = authProperties.getBaseUrl() ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", authProperties.getClientId());
        body.add("client_secret", authProperties.getClientSecret());
        body.add("scope", authProperties.getScope());
        body.add("grant_type", authProperties.getGrant_type());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<?, ?> responseBody = response.getBody();
            return responseBody != null ? (String) responseBody.get("access_token") : null;
        } catch (Exception ex) {

            throw new RuntimeException("I/O error on POST request for \"" + url + "\": " + ex.getMessage());
        }
    }

}