package Client.Mediatracker;

import Client.Mediatracker.common.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
@EnableConfigurationProperties
public class ClientApplication implements CommandLineRunner {
    private final ConfigProperties configProperties;

    public ClientApplication(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        String requestUrl = configProperties.getMediaApiBaseUrl() + configProperties.getMediaApiPath();

        // Create connection
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Default is GET

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