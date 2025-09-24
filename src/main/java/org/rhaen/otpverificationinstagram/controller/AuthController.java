package org.rhaen.otpverificationinstagram.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.dto.AuthResponse;
import org.rhaen.otpverificationinstagram.dto.LoginRequest;
import org.rhaen.otpverificationinstagram.dto.RefreshTokenRequest;
import org.rhaen.otpverificationinstagram.dto.RegisterRequest;
import org.rhaen.otpverificationinstagram.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Ro'yxatdan o'tish va tizimga kirish API'lari")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Ro'yxatdan o'tish jarayonini boshlash",
            description = "Foydalanuvchi email, instagram username va parolni yuboradi. Tizim emailga tasdiqlash kodini jo'natadi."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Tasdiqlash kodi muvaffaqiyatli yuborildi",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Verification code sent to your email...\"}"))),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri formatdagi ma'lumotlar (validatsiya xatosi)"),
            @ApiResponse(responseCode = "409", description = "Bu email yoki instagram username allaqachon mavjud")
    })
    @PostMapping("/register")
    public ResponseEntity<?> initiateRegistration(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = authService.initiateRegistration(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("message", message));
        } catch (IllegalStateException e) {
            log.error("Ro'yxatdan o'tishda xatolik: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Tizimga kirish (Login)",
            description = "Mavjud foydalanuvchi email va parol orqali tizimga kiradi va Access/Refresh tokenlarini oladi."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli tizimga kirildi",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Email yoki parol noto'g'ri")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) { // AuthenticationException'ni ham ushlaydi
            log.error("Login jarayonida xatolik: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Email yoki parol noto'g'ri."));
        }
    }

    @Operation(
            summary = "Access Token'ni yangilash",
            description = "Muddati o'tgan `accessToken` o'rniga yangi `accessToken` olish uchun yaroqli `refreshToken` yuboriladi."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access token muvaffaqiyatli yangilandi",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token yaroqsiz yoki muddati o'tgan",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\": \"Refresh token muddati o'tgan. Iltimos, qayta login qiling.\"}")))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Token yangilashda xatolik: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}
