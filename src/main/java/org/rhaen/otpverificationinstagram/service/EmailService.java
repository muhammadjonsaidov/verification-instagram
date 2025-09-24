package org.rhaen.otpverificationinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${instagram.business.username}")
    private String toInstagramDM;

    @Async // Bu metodni alohida oqimda (thread) bajaradi
    public void sendVerificationEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Hisobingizni Tasdiqlash Kodi");
            message.setText(
                    "Xush kelibsiz!\n\n" +
                            "Ro'yxatdan o'tishni yakunlash uchun, quyidagi tasdiqlash kodini o'z Instagram hisobingizdan bizning " + toInstagramDM + " sahifamizga yuboring.\n\n" +
                            "Sizning kodingiz: " + code + "\n\n" +
                            "Bu kod 5 daqiqa davomida amal qiladi."
            );
            mailSender.send(message);
            log.info("Tasdiqlash xabari {} manziliga muvaffaqiyatli yuborildi.", toEmail);
        } catch (MailException e) {
            log.error("{} manziliga xabar yuborishda xatolik yuz berdi: {}", toEmail, e.getMessage());
        }
    }
}