package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exits {}", email);
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
}
