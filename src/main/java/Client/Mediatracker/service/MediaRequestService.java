package Client.Mediatracker.service;

import Client.Mediatracker.common.ConfigProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class MediaRequestService {

    private final ConfigProperties configProperties;

    public MediaRequestService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public void callMediaApi() throws IOException {
        String requestUrl = configProperties.getMediaApiBaseUrl() + configProperties.getMediaApiPath();
        HttpURLConnection conn = (HttpURLConnection) new URL(requestUrl).openConnection();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (Scanner reader = new Scanner(conn.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (reader.hasNextLine()) {
                response.append(reader.nextLine());
            }
            System.out.println("Response Body: " + response);
        } catch (Exception e) {
            try (Scanner errorReader = new Scanner(conn.getErrorStream())) {
                StringBuilder errorResponse = new StringBuilder();
                while (errorReader.hasNextLine()) {
                    errorResponse.append(errorReader.nextLine());
                }
                System.out.println("Error Response: " + errorResponse);
            }
        }
        conn.disconnect();
    }
}
