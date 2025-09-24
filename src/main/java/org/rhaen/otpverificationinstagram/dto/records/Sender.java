package org.rhaen.otpverificationinstagram.dto.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sender(String id) {} // Bu foydalanuvchining PSID si
