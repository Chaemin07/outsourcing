package com.example.outsourcing.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // Base Exception 처리
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Map<String, Object>> handleBaseException(BaseException e,
      HttpServletRequest request) {
    return buildErrorResponse(e.getErrorCode(), request.getRequestURI(), null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", Optional.of(error.getDefaultMessage()).orElse("Validation failed")
        ))
        .collect(Collectors.toList());

    return buildErrorResponse(ErrorCode.VALID_ERROR, request.getRequestURI(), fieldErrors);
  }


  private ResponseEntity<Map<String, Object>> buildErrorResponse(
      ErrorCode errorCode, String path, List<Map<String, String>> fieldErrors) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", errorCode.getHttpStatus().value());
    body.put("error", errorCode.getHttpStatus().getReasonPhrase());
    body.put("code", errorCode.getErrorCode());
    body.put("message", errorCode.getMessage());
    body.put("path", path);
    body.put("timestamp", LocalDateTime.now());
    if (fieldErrors != null && !fieldErrors.isEmpty()) {
      body.put("fieldErrors", fieldErrors);
    }
    return new ResponseEntity<>(body, errorCode.getHttpStatus());
  }
}
