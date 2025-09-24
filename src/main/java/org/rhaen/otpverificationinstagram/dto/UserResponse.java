package org.rhaen.otpverificationinstagram.dto;

import lombok.Builder;
import lombok.Data;
import org.rhaen.otpverificationinstagram.entity.User;

@Data
@Builder // Builder pattern orqali oson obyekt yaratish uchun
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String instagramUsername;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .instagramUsername(user.getInstagramUsername())
                .build();
    }
}