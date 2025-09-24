package org.rhaen.otpverificationinstagram.dto.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Messaging(Sender sender, Recipient recipient, Message message) {
}
