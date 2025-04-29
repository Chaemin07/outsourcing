package com.example.outsourcing.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

// 비밀번호 암호화 및 검증
@Component
public class PasswordEncoder {

    // 비밀번호를 암호화하여 반환
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    // 암호화된 비밀번호와 일치하는지 확인하여 boolean값 반환
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
