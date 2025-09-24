package org.rhaen.otpverificationinstagram.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.repository.PendingVerificationRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class PendingVerificationCleanupScheduler {

    private final PendingVerificationRepository pendingRepo;

    // Har 30 daqiqada ishga tushadi (cron ifodasi yoki fixedRate)
    @Scheduled(fixedRate = 1800000) // 30 daqiqa = 1,800,000 millisekund
    @Transactional
    public void cleanupExpiredPendingVerifications() {
        log.info("Muddati o'tgan tasdiqlash so'rovlarini tozalash boshlandi...");
        try {
            LocalDateTime now = LocalDateTime.now();
            pendingRepo.deleteByExpiresAtBefore(now);
            log.info("Tozalash muvaffaqiyatli yakunlandi.");
        } catch (Exception e) {
            log.error("Muddati o'tgan so'rovlarni tozalashda xatolik yuz berdi:", e);
        }
    }
}
