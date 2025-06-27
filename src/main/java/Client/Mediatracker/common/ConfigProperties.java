package Client.Mediatracker.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConfigProperties {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    // Auth properties
    @Value("${swbc.auth.base-url}")
    private String baseUrl;

    @Value("${swbc.auth.client-id}")
    private String clientId;

    @Value("${swbc.auth.client-secret}")
    private String clientSecret;

    @Value("${swbc.auth.scope}")
    private String scope;

    @Value("${swbc.auth.grant_type}")
    private String grantType;

    // Media API properties
    @Value("${media.api.base-url}")
    private String mediaApiBaseUrl;

    @Value("${media.api.path}")
    private String mediaApiPath;

    // Getters
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getMediaApiBaseUrl() {
        return mediaApiBaseUrl;
    }

    public String getMediaApiPath() {
        return mediaApiPath;
    }
}
