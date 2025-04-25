package com.example.outsourcing.jwt;

import com.example.outsourcing.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  // 토큰 유효시간
  private static final Long TOKEN_TIME = 60 * 60 * 1000L;    // 60분
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";     // 관례

  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;    // 비밀키(secretKey)를 통해 키 생성

  // 비밀키 알고리즘
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  // 스프링 컨테이너 초기화 이후 1회 초기화 실행
  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // 토큰 발급
  public String createToken(Long userId, String email, Role role) {
    // 페이로드
    Date now = new Date();

    return Jwts.builder()
        .setSubject(String.valueOf(userId))                       // 토큰의 주체
        .setHeaderParam("typ", "JWT")                       // 헤더 설정
        .setIssuedAt(now)                                       // 발행 시간
        .setExpiration(new Date(now.getTime() + TOKEN_TIME))   // 토큰 만료기한 (발급 일시 +60분)
        .claim("userId", userId)                          // Private Claims (Key-Value)
        .claim("email", email)
        .claim("role", role)
        .signWith(key, signatureAlgorithm)   // 서명 (사용 알고리즘, 서명 생성-검증 용 비밀 키)
        .compact();
  }

  public String substringToken(String tokenValue) {
    if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
      return tokenValue.substring(7);
    }
    throw new RuntimeException("토큰을 찾을 수 없습니다.");
  }

  // 토큰 바디(Claims) 반환
  public Claims extractClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody();
  }

  // 토큰에서 userId 획득
  public Long getUserIdFromToken(String token) {
    return Long.valueOf(Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject());
  }
}
