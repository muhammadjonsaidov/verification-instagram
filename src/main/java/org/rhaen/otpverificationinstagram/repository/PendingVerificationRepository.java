package org.rhaen.otpverificationinstagram.repository;

import org.rhaen.otpverificationinstagram.entity.PendingVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PendingVerificationRepository extends JpaRepository<PendingVerification, Long> {

    // Kod orqali tasdiqlash so'rovini topish uchun
    Optional<PendingVerification> findByCode(String code);

    // Ro'yxatdan o'tishda bu email yoki username bilan boshqa pending so'rov bor-yo'qligini tekshirish
    Optional<PendingVerification> findByEmailOrInstagramUsernameIgnoreCase(String email, String instagramUsername);

    // Muddati o'tgan yozuvlarni o'chirish uchun (Scheduler uchun kerak bo'ladi)
    void deleteByExpiresAtBefore(LocalDateTime now);
}