package org.rhaen.otpverificationinstagram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class InstagramApiService {

    private static final String GRAPH_API_URL = "https://graph.facebook.com/v23.0/me/messages";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${instagram.api.access-token}")
    private String ACCESS_TOKEN;

    public void sendReplyMessage(String recipientPsid, String messageText) {
        String url = GRAPH_API_URL + "?access_token=" + ACCESS_TOKEN;

        String requestBody = String.format("""
                {
                  "recipient": {"id": "%s"},
                  "message": {"text": "%s"},
                  "messaging_type": "RESPONSE"
                }
                """, recipientPsid, messageText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("Successfully sent reply to PSID: {}", recipientPsid);
        } catch (Exception e) {
            log.error("Failed to send reply message to PSID: {}. Error: {}", recipientPsid, e.getMessage());
        }
    }
}