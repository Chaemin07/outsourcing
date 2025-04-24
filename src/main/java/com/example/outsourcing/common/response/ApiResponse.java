package com.example.outsourcing.common.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통 API 응답 포맷 클래스입니다.
 * 모든 API 응답은 이 클래스를 통해 일관된 구조로 반환됩니다.
 *
 * @param <T> 응답 데이터의 타입 (예: UserDto, List<PostDto> 등)
 */
@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    /**
     * HTTP 상태 코드 (예: 200, 201, 400, 404, 500 등)
     */
    private int statusCode;

    /**
     * 응답 메시지 (성공/에러 메시지)
     */
    private String message;

    /**
     * 실제 응답 데이터 (없으면 null)
     */
    private T data;

// ==================== 성공 응답 ====================

    /**
     * 요청이 성공했을 때 사용되는 응답 생성 메서드
     *
     * @param message 사용자에게 보여줄 메시지
     * @param data    실제 응답 데이터
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 요청이 성공했을 때 사용되는 응답 생성 메서드
     *
     * @param status  커스텀 HttpStatus  (ex: 201 Created)
     * @param message 사용자에게 보여줄 메시지
     * @param data    실제 응답 데이터
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(status.value())
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 요청이 성공했을 때 (기본 메시지 사용)
     */
    public static <T> ApiResponse<T> success(T data) {
        return success("요청이 성공적으로 처리되었습니다.", data);
    }

    /**
     * 요청이 성공했지만 반환할 데이터가 없을 때 사용
     * 메시지만 (data는 null)
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }

    // ==================== 에러 응답 ====================

    /**
     * 요청이 실패했을 때 사용되는 응답 생성 메서드
     *
     * @param statusCode 에러 코드
     * @param message    에러 메시지
     * @return ApiResponse 객체 (data는 null)
     */
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 요청이 실패했을 때 사용되는 응답 생성 메서드
     * HttpStatus로 가독성 좋게
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return error(status.value(), message);
    }


}
