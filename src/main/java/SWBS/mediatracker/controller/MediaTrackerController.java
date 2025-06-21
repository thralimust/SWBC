package SWBS.mediatracker.controller;

import SWBS.mediatracker.service.InteractionService;
import SWBS.mediatracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
public class MediaTrackerController {

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/identity/connect/token")
    public ResponseEntity<Map<String, String>> fetchToken() {

        try {
            String token = tokenService.getBearerToken();

            return ResponseEntity.ok(Map.of("access_token", token));
        } catch (Exception e) {

            return ResponseEntity.status(500).body(Map.of("Error", e.getMessage()));
        }

    }

    @PostMapping("/api/v1/calltag")
    public ResponseEntity<?> handleInteraction(@RequestParam(name = "GENESYS_EXTENSION") String extension) {
        return interactionService.processInteraction(extension);
    }

}
