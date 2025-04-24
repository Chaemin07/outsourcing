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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  // 로그인 필터 제외 대상 URL
  private static final List<AntPathRequestMatcher> WHITE_LIST = List.of(
      new AntPathRequestMatcher("/", "GET"),
      new AntPathRequestMatcher("/signup", "POST"),  // 회원가입
      new AntPathRequestMatcher("/login", "POST")   // 로그아웃
  );

  // 사장 전용 URL
  private static final List<AntPathRequestMatcher> OWNER_LIST = List.of(
      new AntPathRequestMatcher("/owners", "GET")
  );

  // 토큰이 없다면 발급해주고 만료되었으면 어케하지?
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String url = request.getRequestURI();

    // WHITE_LIST 는 필터 적용 제외
    for (AntPathRequestMatcher matcher : WHITE_LIST) {
      if (matcher.matches(request)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    String token = request.getHeader("Authorization");

    // 토큰이 비어있다면
    if (token == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "로그인 해주세요.");
      return;
    }

    // 유효한 토큰인지 검사
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
    }

    // request attributes 세팅
    request.setAttribute("userId", Long.valueOf(claims.getSubject()));
    request.setAttribute("role", claims.get("role"));
    request.setAttribute("email", claims.get("email"));

    // 유저 역할 검증 // TODO: 추후 early return 하도록 변경
    for (AntPathRequestMatcher matcher : OWNER_LIST) {
      // 사장만 접근 가능 URL 에
      if (matcher.matches(request)) {
        Role userRole = Role.valueOf(claims.get("role", String.class));

        // 만약 유저 role 이 일반 사용자(USER)라면
        if (!Role.OWNER.equals(userRole)) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 접근입니다.");
          return;
        }

        filterChain.doFilter(request, response);
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
