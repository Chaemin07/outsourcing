package com.example.outsourcing.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  ErrorCode errorCode;

  public BaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
