package org.rhaen.otpverificationinstagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.dto.records.WebhookPayload;
import org.rhaen.otpverificationinstagram.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final VerificationService verificationService;

    @Value("${instagram.webhook.verify-token}")
    private String VERIFY_TOKEN;

    @Value("${instagram.page-id}")
    private String PAGE_ID;


    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if (mode != null && token != null && mode.equals("subscribe") && token.equals(VERIFY_TOKEN)) {
            log.info("WEBHOOK_VERIFIED");
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookEvents(@RequestBody WebhookPayload payload) {
        log.info("Webhook qabul qilindi: {}", payload);

        payload.entry().forEach(entry -> {
            if (entry.messaging() != null) {
                entry.messaging().forEach(event -> {
                    String senderPsid = event.sender().id();

                    if (senderPsid.equals(PAGE_ID)) {
                        return;
                    }

                    if (event.message() != null && event.message().text() != null) {
                        String messageText = event.message().text().trim();
                        verificationService.finalizeRegistration(messageText, senderPsid);
                    }
                });
            }
        });
        return new ResponseEntity<>("EVENT_RECEIVED", HttpStatus.OK);
    }
}