package org.rhaen.otpverificationinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhaen.otpverificationinstagram.dto.UserResponse;
import org.rhaen.otpverificationinstagram.entity.User;
import org.rhaen.otpverificationinstagram.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserProfile(String email) {
        User user = findUserByEmail(email);
        return UserResponse.from(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + email));
    }
}
