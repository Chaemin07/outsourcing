package com.example.outsourcing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 요청
  MISSING_PARAMETER("필수 요청 파라미터가 없습니다.", HttpStatus.BAD_REQUEST, "400-007"),
  MISSING_HEADER("필수 요청 헤더가 없습니다.", HttpStatus.BAD_REQUEST, "400-008"),
  TYPE_MISMATCH("요청 파라미터 타입이 잘못되었습니다.", HttpStatus.BAD_REQUEST, "400-009"),
  NOT_READABLE_MESSAGE("요청 본문이 올바르지 않습니다.", HttpStatus.BAD_REQUEST, "400-010"),
  METHOD_NOT_SUPPORTED("지원하지 않는 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED, "405-001"),
  NO_HANDLER_FOUND("존재하지 않는 URI 입니다.", HttpStatus.NOT_FOUND, "404-007"),
  VALIDATION_FAILED("유효성 검사 실패", HttpStatus.BAD_REQUEST, "400-011"),
  CONSTRAINT_VIOLATION("요청이 제약 조건을 위반했습니다.", HttpStatus.BAD_REQUEST, "400-012"),
  INTERNAL_ERROR("서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR, "500-001"),
  VALID_ERROR("VAILD가 유효하지 않습니디", HttpStatus.BAD_REQUEST, "400-013"),

  // 유효하지 않은 요청
  INVALID_EMAIL("유효하지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, "400-001"),
  INVALID_PASSWORD("유효하지 않은 비밀번호입니다.", HttpStatus.BAD_REQUEST, "400-002"),
  INVALID_USER_ID("유효하지 않은 사용자입니다.", HttpStatus.BAD_REQUEST, "400-003"),
  INVALID_MENU_ID("유효하지 않은 메뉴입니다.", HttpStatus.BAD_REQUEST, "400-004"),
  INVALID_FRIEND_REQUEST("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST, "400-005"),
  INVALID_CODE("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST, "400-006"),
  NOT_DELIVERED("배송이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST, "400-007"),
  NOT_CUSTOMER("본인의 주문이 아닙니다.", HttpStatus.BAD_REQUEST, "400-008"),
  EMPTY_CATEGORY("카테고리가 비어있습니다.", HttpStatus.BAD_REQUEST, "400-009"),
  NOT_FAVORITE_STORE("즐겨찾기가 되어있지 않은 가게입니다.", HttpStatus.BAD_REQUEST, "400-010"),
  EXCEED_STORE_LIMIT("사장님은 최대 3개의 점포만 운영할 수 있습니다.", HttpStatus.BAD_REQUEST, "400-011"),
  IMAGE_UPLOAD_FAILED( "이미지 업로드에 실패했습니다.", HttpStatus.BAD_REQUEST, "400-012"),
  MENU_ALREADY_DELETED("삭제된 메뉴는 수정할 수 없습니다.", HttpStatus.BAD_REQUEST, "400-013"),

  // 인증
  UNAUTHORIZED_USER_ID("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED, "401-001"),
  UNAUTHORIZED_CODE("인증되지 않은 코드 입니다.", HttpStatus.UNAUTHORIZED, "401-002"),

  // 권한
  FORBIDDEN_STORE("가게에 접근할 수 없습니다.", HttpStatus.FORBIDDEN, "403-001"),

  // 엔티티 조회
  NOT_FOUND_EMAIL("이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-001"),
  NOT_FOUND_PASSWORD("비밀번호를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-002"),
  NOT_FOUND_USER_ID("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-003"),
  NOT_FOUND_POST_ID("게시글를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-004"),
  NOT_FOUND_COMMENT_ID("댓글를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-005"),
  NOT_FOUND_LIKE_ID("좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-006"),
  NOT_FOUND_ORDER_ID("주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-007"),
  NOT_FOUND_REVIEW("해당 주문에 대한 리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-008"),
  NOT_FOUND_STORE_ID("가게를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-009"),
  NOT_FOUND_REVIEW_COMMENT("리뷰 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-010"),
  NOT_FOUND_CATEGORY("카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "404-011"),

  // 중복
  CONFLICT_EMAIL("중복된 이메일입니다.", HttpStatus.CONFLICT, "409-001"),
  CONFLICT_PASSWORD("중복된 비밀번호입니다.", HttpStatus.CONFLICT, "409-002"),
  CONFLICT_STATUS("중복된 요청입니다.", HttpStatus.CONFLICT, "409-003");


  private final String message;
  private final HttpStatus httpStatus;
  private final String errorCode;
}


