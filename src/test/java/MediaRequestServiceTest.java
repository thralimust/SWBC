

import Client.Mediatracker.common.ConfigProperties;
import Client.Mediatracker.service.impl.MediaRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import static org.mockito.Mockito.*;
import java.util.Scanner;

public class MediaRequestServiceTest {

    private ConfigProperties configProperties;

    @BeforeEach
    void setup() {
        configProperties = mock(ConfigProperties.class);
        when(configProperties.getMediaApiBaseUrl()).thenReturn("http://localhost:9999");
        when(configProperties.getMediaApiPath()).thenReturn("?GENESYS_EXTENSION=121210&simulate=false");
    }

    @Test
    void testCallMediaApi_success() throws Exception {
        HttpURLConnection mockConn = mock(HttpURLConnection.class);
        when(mockConn.getResponseCode()).thenReturn(200);
        when(mockConn.getInputStream()).thenReturn(new ByteArrayInputStream("{\"extension\":\"121210&simulate=false\",\"InteractionId\":\"0ee5a939-12f8-4f3e-b85d-a2100cd29138\",\"HTTP status Code\":\"success\"}".getBytes()));

        MediaRequestService service = new MediaRequestService(configProperties) {
            @Override
            public void callMediaApi() throws IOException {
                HttpURLConnection conn = mockConn;
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                try (Scanner reader = new Scanner(conn.getInputStream())) {
                    StringBuilder response = new StringBuilder();
                    while (reader.hasNextLine()) {
                        response.append(reader.nextLine());
                    }
                    System.out.println("Response Body: " + response );
                }
            }
        };

        service.callMediaApi();
        verify(mockConn).getInputStream();
    }

    @Test
    void testCallMediaApi_failure() throws Exception {
        HttpURLConnection mockConn = mock(HttpURLConnection.class);
        when(mockConn.getResponseCode()).thenReturn(500);
        when(mockConn.getInputStream()).thenThrow(new IOException("IO Error"));
        when(mockConn.getErrorStream()).thenReturn(new ByteArrayInputStream("Internal Server Error".getBytes()));

        MediaRequestService service = new MediaRequestService(configProperties) {
            @Override
            public void callMediaApi() throws IOException {
                HttpURLConnection conn = mockConn;
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                try (Scanner reader = new Scanner(conn.getInputStream())) {
                    // will throw IOException
                } catch (Exception e) {
                    try (Scanner errorReader = new Scanner(conn.getErrorStream())) {
                        StringBuilder errorResponse = new StringBuilder();
                        while (errorReader.hasNextLine()) {
                            errorResponse.append(errorReader.nextLine());
                        }
                        System.out.println("Error Response: " + errorResponse);
                    }
                }
            }
        };

        service.callMediaApi();
        verify(mockConn).getErrorStream();
    }
}
