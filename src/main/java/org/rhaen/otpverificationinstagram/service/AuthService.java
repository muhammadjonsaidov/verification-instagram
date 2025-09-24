package org.rhaen.otpverificationinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.dto.AuthResponse;
import org.rhaen.otpverificationinstagram.dto.LoginRequest;
import org.rhaen.otpverificationinstagram.dto.RegisterRequest;
import org.rhaen.otpverificationinstagram.dto.UserResponse;
import org.rhaen.otpverificationinstagram.entity.PendingVerification;
import org.rhaen.otpverificationinstagram.entity.User;
import org.rhaen.otpverificationinstagram.repository.PendingVerificationRepository;
import org.rhaen.otpverificationinstagram.repository.UserRepository;
import org.rhaen.otpverificationinstagram.security.CustomUserDetails;
import org.rhaen.otpverificationinstagram.security.CustomUserDetailsService;
import org.rhaen.otpverificationinstagram.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PendingVerificationRepository pendingRepo;
    private final PasswordService passwordService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService; // Yangi EmailService
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public String initiateRegistration(RegisterRequest request) {
        // 1. Tizimda (User jadvalida) bu email yoki username mavjud emasligini tekshirish
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered.");
        }
        if (userRepository.findByInstagramUsernameIgnoreCase(request.getInstagramUsername()).isPresent()) {
            throw new IllegalStateException("Instagram username already registered.");
        }

        // 2. Pending holatida ham bu email yoki username bo'lsa, eskisini o'chirish
        pendingRepo.findByEmailOrInstagramUsernameIgnoreCase(request.getEmail(), request.getInstagramUsername())
                .ifPresent(pendingRepo::delete);

        // 3. Yangi pending yozuvini yaratish
        PendingVerification pending = new PendingVerification();
        pending.setEmail(request.getEmail());
        pending.setHashedPassword(passwordService.encode(request.getPassword()));
        pending.setInstagramUsername(request.getInstagramUsername().toLowerCase());
        pending.setFullName(request.getFullName());

        String verificationCode = generateVerificationCode(); // Masalan, 6 xonali raqam
        pending.setCode(verificationCode);
        pending.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Kod 5 daqiqa amal qiladi

        pendingRepo.save(pending);

        // 4. Email orqali kodni yuborish
        emailService.sendVerificationEmail(pending.getEmail(), verificationCode);

        // 5. Foydalanuvchiga javob qaytarish
        return "Verification code sent to your email. Please send this code from your Instagram account to @muhammadjonsaidov76.";
    }

    public AuthResponse login(LoginRequest request) {
        // Foydalanuvchi nomini va parolini tasdiqlash
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Authentication failed after passing manager."));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.from(user))
                .build();
    }
    public AuthResponse refreshToken(String refreshToken) {
        // 0. Validate refresh token
        validateRefreshToken(refreshToken);

        // 4. Tokendan foydalanuvchi emailini olish
        String email = jwtUtils.extractUsername(refreshToken);

        // 5. Shunday foydalanuvchi bazada mavjudligini tekshirish
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Refresh token'ga mos foydalanuvchi topilmadi."));

        return generateAuthResponse(user, refreshToken);
    }

    private void validateRefreshToken(String refreshToken) {
        // 1. Token mavjudligi va formati to'g'riligini tekshirish
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token yuborilmagan.");
        }

        // 2. Tokenning muddati o'tmaganligini tekshirish
        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token muddati o'tgan. Iltimos, qayta login qiling.");
        }

        // 3. Bu haqiqatan ham "refresh" token ekanligini tekshirish
        if (!jwtUtils.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Yuborilgan token 'refresh token' emas.");
        }
    }

    private AuthResponse generateAuthResponse(User user, String refreshToken) {
        // 6. Yangi accessToken generatsiya qilish
        UserDetails userDetails = new CustomUserDetails(user);
        String newAccessToken = jwtUtils.generateAccessToken(userDetails);

        // 7. Yangi AuthResponse'ni qaytarish (eski refreshToken o'zgarishsiz qoladi)
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Eski refresh token'ni qaytaramiz
                .user(UserResponse.from(user))
                .build();
    }

    private String generateVerificationCode() {
        // Masalan, 6 xonali tasodifiy raqam generatsiya qilish
        return String.format("%06d", new java.util.Random().nextInt(999999));
    }
}
