package com.example.outsourcing.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthUser {

  @AliasFor("name")
  String value() default "";

  @AliasFor("value")
  String name() default "";

  boolean required() default true;      // TODO: SessonAttribute 동작 뜯어와서 비슷하게 동작하도록
}