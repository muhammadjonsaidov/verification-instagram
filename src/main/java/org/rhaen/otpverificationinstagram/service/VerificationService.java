package org.rhaen.otpverificationinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.entity.PendingVerification;
import org.rhaen.otpverificationinstagram.entity.User;
import org.rhaen.otpverificationinstagram.repository.PendingVerificationRepository;
import org.rhaen.otpverificationinstagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final PendingVerificationRepository pendingRepo;
    private final UserRepository userRepo;
    private final InstagramApiService instagramApiService;


    @Transactional
    public void finalizeRegistration(String receivedCode, String senderPsid) {
        // 1. Kod bo'yicha pending yozuvini topamiz
        Optional<PendingVerification> pendingOpt = pendingRepo.findByCode(receivedCode.trim());

        if (pendingOpt.isEmpty()) {
            log.warn("PSID {} dan noto'g'ri tasdiqlash kodi ('{}') keldi.", senderPsid, receivedCode);
            instagramApiService.sendReplyMessage(senderPsid, "Siz yuborgan kod xato yoki eskirgan. Iltimos, ro'yxatdan qayta o'ting.");
            return;
        }

        PendingVerification pending = pendingOpt.get();

        // 2. Kodning muddati o'tmaganligini tekshiramiz
        if (pending.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("PSID {} tomonidan muddati o'tgan kod ('{}') ishlatildi.", senderPsid, receivedCode);
            instagramApiService.sendReplyMessage(senderPsid, "Bu kodning amal qilish muddati tugagan. Iltimos, ro'yxatdan qayta o'ting.");
            pendingRepo.delete(pending); // Eskirgan yozuvni o'chiramiz
            return;
        }

        // 3. Barcha tekshiruvlar muvaffaqiyatli. Yangi User yaratamiz.
        User user = new User();
        user.setEmail(pending.getEmail());
        user.setPassword(pending.getHashedPassword()); // Parol allaqachon heshlangan
        user.setInstagramUsername(pending.getInstagramUsername());
        user.setFullName(pending.getFullName());
        user.setInstagramPsid(senderPsid);

        userRepo.save(user);

        // 4. Vaqtinchalik yozuvni o'chirib tashlaymiz
        pendingRepo.delete(pending);

        // 5. Foydalanuvchiga Instagram orqali muvaffaqiyatli javob yuboramiz
        String successMessage = "Tabriklaymiz, " + user.getInstagramUsername() + "! Sizning hisobingiz muvaffaqiyatli tasdiqlandi. Endi tizimga kirishingiz mumkin.";
        instagramApiService.sendReplyMessage(senderPsid, successMessage);
        log.info("MUVAFFAQIYATLI: Foydalanuvchi {} ro'yxatdan o'tdi va tasdiqlandi!", user.getEmail());
    }
}