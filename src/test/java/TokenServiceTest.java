import Client.Mediatracker.service.TokenService;
import Client.Mediatracker.common.ConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TokenServiceTest {

    @Mock
    private ConfigProperties configProperties;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock property values
        when(configProperties.getBaseUrl()).thenReturn("https://d-portal.swbc.local");
        when(configProperties.getClientId()).thenReturn("client");
        when(configProperties.getClientSecret()).thenReturn("secret");
        when(configProperties.getScope()).thenReturn("read");
        when(configProperties.getGrantType()).thenReturn("client_credentials");
    }

    @Test
    void testGetBearerToken_success() {
        Map<String, String> fakeResponse = Map.of("access_token", "access_token");
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(fakeResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(responseEntity);

        String result = tokenService.getBearerToken();
        assertEquals("access_token", result);
    }
    @Test
    void testGetBearerToken_failure() {
        // Simulate a failure from RestTemplate
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenThrow(new RestClientException("Connection refused"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tokenService.getBearerToken();
        });

        assertTrue(exception.getMessage().contains("I/O error on POST request"));
        assertTrue(exception.getMessage().contains("Connection refused"));
    }
}
