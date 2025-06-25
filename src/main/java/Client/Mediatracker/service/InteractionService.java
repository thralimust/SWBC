package SWBS.mediatracker.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InteractionService {

    private static final int MAX_RETRIES = 6;
    private static final int TOTAL_TIME_MS = 30_000;
    private final Map<String, Integer> extensionAttempts = new ConcurrentHashMap<>();

    public ResponseEntity<?> processInteraction(String extension , boolean simulate) {
        if (extension == null || extension.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Missing GENESYS_EXTENSION parameter"
            ));
        }

        if (extension.length() < 5) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Invalid GENESYS_EXTENSION value"
            ));
        }

        int currentTry = extensionAttempts.getOrDefault(extension, 0) + 1;
        extensionAttempts.put(extension, currentTry);

        if (currentTry <= MAX_RETRIES) {
            return ResponseEntity.ok(Map.of(
                    "HTTP Status", "success",
                    "Request successful. The server has responded as required.", "200",
                    "InteractionId", UUID.randomUUID().toString(),
                    "extension", extension
            ));
        } else {
            if (simulate) {
                try {
                    Thread.sleep(TOTAL_TIME_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }


            String correlationId = UUID.randomUUID().toString();
            extensionAttempts.remove(extension); // reset for reuse

            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", String.format("Unable retrieve action ConversationId for userId: %s after 6 tries. CorrelationId: %s", extension, correlationId)
            ));
        }
    }
}
