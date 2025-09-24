package org.rhaen.otpverificationinstagram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.dto.UserResponse;
import org.rhaen.otpverificationinstagram.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "Foydalanuvchi ma'lumotlari bilan ishlash")
public class UserController {

    private final UserService userService;

    // Bu endpoint foydalanuvchi o'z ma'lumotlarini olishi uchun kerak
    @Operation(
            summary = "Foydalanuvchi profilini olish",
            description = "Tizimga kirgan foydalanuvchi o'zining profil ma'lumotlarini oladi. Bu endpoint uchun 'Bearer' token kerak.",
            security = @SecurityRequirement(name = "bearerAuth") // Bu endpoint himoyalanganligini bildiradi
    )
    @ApiResponse(responseCode = "200", description = "Foydalanuvchi ma'lumotlari muvaffaqiyatli olindi")
    @ApiResponse(responseCode = "404", description = "Foydalanuvchi topilmadi (token xato bo'lsa)")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            // Authentication.getName() bizga UserDetails.getUsername() ni qaytaradi (bizning holatda email)
            String email = authentication.getName();
            UserResponse userProfile = userService.getUserProfile(email);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            log.error("Foydalanuvchi profilini olishda xatolik: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
