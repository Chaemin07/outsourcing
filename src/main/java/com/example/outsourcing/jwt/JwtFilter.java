package com.example.outsourcing.jwt;

import com.example.outsourcing.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  // 로그인 필터 제외 대상 URL
  private static final List<String> WHITE_LIST = List.of(
      "/signup",  // 회원가입
      "/login"    // 로그아웃
  );

  // 사장 전용 URL    // TODO: URL 추가
  private static final List<String> OWNER_LIST = List.of(
      "/owners",
      "/stores/*/categories/**",
      "/reviews/*/comments/**"
  );

  // 토큰이 없다면 발급해주고 만료되었으면?
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String url = request.getRequestURI();
    log.info("Request url :" + url);

    // WHITE_LIST 는 필터 적용 제외
    for (String matcher : WHITE_LIST) {
      if (url.startsWith(matcher)) {
        log.info("WHITE LIST REQUEST");
        filterChain.doFilter(request, response);
        return;
      }
    }

    String bearerToken = request.getHeader("Authorization");
    log.info("Authorization Header: {}", bearerToken);

    // 토큰이 비어있다면
    if (bearerToken == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "로그인 해주세요.");
      return;
    }

    // 유효한 토큰인지 검사
    String token = jwtUtil.substringToken(bearerToken);
    Claims claims = null;

    try {
      claims = jwtUtil.extractClaims(token);
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰에 문제가 있습니다.");
    }
    // 유저 역할 검증 // TODO: 추후 early return 하도록 변경
    for (String matcher : OWNER_LIST) {
      // 사장만 접근 가능 URL 에
      // if (url.startsWith(matcher)) {
      if (pathMatcher.match(matcher, url)) {
        Role userRole = Role.valueOf(claims.get("role", String.class));

        // 만약 유저 role 이 일반 사용자(USER)라면
        if (!Role.OWNER.equals(userRole)) {
          log.info("JwtFilter Role : Invalid");
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 접근입니다.");
          return;
        }
      }
    }

    // request attributes 세팅
    request.setAttribute("userId", Long.valueOf(claims.getSubject()));
    request.setAttribute("role", claims.get("role"));
    request.setAttribute("email", claims.get("email"));

    filterChain.doFilter(request, response);
  }
}
