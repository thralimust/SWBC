package Client.Mediatracker.controller;

import Client.Mediatracker.service.InteractionService;
import Client.Mediatracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

   /* @PostMapping("/api/v1/calltag")
    public ResponseEntity<?> handleInteraction(@RequestParam(name = "GENESYS_EXTENSION") String extension, @RequestParam(name = "simulate") boolean simulate) {
        return interactionService.processInteraction(extension, simulate);
    }*/

}
