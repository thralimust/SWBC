package Client.Mediatracker.service.impl;

import Client.Mediatracker.common.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TokenService {

    private final ConfigProperties authProperties;
    private final RestTemplate restTemplate;

    public TokenService(ConfigProperties authProperties, RestTemplate restTemplate) {
        this.authProperties = authProperties;
        this.restTemplate = restTemplate;
    }

    public String getBearerToken() {
        String url = authProperties.getBaseUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", authProperties.getClientId());
        body.add("client_secret", authProperties.getClientSecret());
        body.add("scope", authProperties.getScope());
        body.add("grant_type", authProperties.getGrantType());

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

