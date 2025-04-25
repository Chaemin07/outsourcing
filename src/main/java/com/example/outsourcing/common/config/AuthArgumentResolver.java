package com.example.outsourcing.common.config;

import com.example.outsourcing.common.annotation.AuthUser;
import jakarta.servlet.ServletException;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

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
    return request.getAttribute(name, 1);
  }

  protected void handleMissingValue(String name, MethodParameter parameter)
      throws ServletException {
    throw new ServletRequestBindingException(
        "Missing authUser attribute '" + name + "' of type " + parameter.getNestedParameterType()
            .getSimpleName());
  }
}
