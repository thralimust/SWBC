package Client.Mediatracker;

import Client.Mediatracker.common.ConfigProperties;
import Client.Mediatracker.service.MediaRequestService;
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

    private final MediaRequestService mediaRequestService;

    public ClientApplication(MediaRequestService mediaRequestService) {
        this.mediaRequestService = mediaRequestService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        mediaRequestService.callMediaApi();
    }
}
