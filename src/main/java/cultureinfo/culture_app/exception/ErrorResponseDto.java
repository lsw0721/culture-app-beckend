package cultureinfo.culture_app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponseDto {
    private final String code; //에러코드
    private final String message; //사용자에게 보여줄 메세지
    private final LocalDateTime timestamp; // 에러 발생 시각
    private final String path; // 요청 url

    public static ErrorResponseDto of(ErrorCode errorCode, String path) {
        return ErrorResponseDto.builder()
                .code(errorCode.getCode()) // 에러 코드
                .message(errorCode.getMessage()) // 에러 메세지
                .timestamp(LocalDateTime.now()) // 에러 발생 시간
                .path(path) // 에러 생긴 url
                .build();
    }

    //@Vaild, @Email, @NotNull 등을 위반시 예외처리용
    public static ErrorResponseDto validationError(String message, String path) {
        return ErrorResponseDto.builder()
                .code("VALIDATION_ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}
