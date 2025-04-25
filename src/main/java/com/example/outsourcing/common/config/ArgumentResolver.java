package com.example.outsourcing.common.config;

import com.example.outsourcing.common.annotation.AuthUser;
import com.example.outsourcing.common.dto.AuthSession;
import com.example.outsourcing.user.entity.Role;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ArgumentResolver implements HandlerMethodArgumentResolver {

  // 처리 가능한 파라미터인지 확인
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(AuthUser.class);  // @AuthUser 이 붙은 파라미터만 처리
  }

  // 파라미터 값 바인딩
  @Override
  public Object resolveArgument(
      @Nullable MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    // JwtFilter 에서 세팅한 attribute 값 가져오기
    Long userId = (Long) request.getAttribute("userId");
    String email = (String) request.getAttribute("email");
    Role role = Role.valueOf((String) request.getAttribute("role"));

    return new AuthSession(userId, email, role);
  }
}
