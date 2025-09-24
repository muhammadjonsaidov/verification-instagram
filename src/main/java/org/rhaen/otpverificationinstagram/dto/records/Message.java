package org.rhaen.otpverificationinstagram.dto.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Message(String mid, String text) {}
