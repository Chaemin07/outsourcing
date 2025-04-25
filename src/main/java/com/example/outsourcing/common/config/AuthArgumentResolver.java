package com.example.outsourcing.common.config;

import com.example.outsourcing.common.annotation.AuthUser;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

@Slf4j
public class AuthArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

  public AuthArgumentResolver() {
  }

  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthUser.class);
  }

  protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(
      MethodParameter parameter) {
    AuthUser ann = (AuthUser) parameter.getParameterAnnotation(AuthUser.class);
    Assert.state(ann != null, "No AuthUser annotation");
    return new AbstractNamedValueMethodArgumentResolver.NamedValueInfo(ann.name(), ann.required(),
        "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n");
  }

  @org.springframework.lang.Nullable
  protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) {
    Object val = request.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    log.info("RESOLVING NAME: {}, value: {}, type: {}", name, val, val.getClass());
    return val;
  }

  protected void handleMissingValue(String name, MethodParameter parameter)
      throws ServletException {
    throw new ServletRequestBindingException(
        "Missing authUser attribute '" + name + "' of type " + parameter.getNestedParameterType()
            .getSimpleName());
  }
}
