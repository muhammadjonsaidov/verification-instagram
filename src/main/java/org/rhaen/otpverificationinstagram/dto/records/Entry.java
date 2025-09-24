package org.rhaen.otpverificationinstagram.dto.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Entry(String id, long time, List<Messaging> messaging) {}

