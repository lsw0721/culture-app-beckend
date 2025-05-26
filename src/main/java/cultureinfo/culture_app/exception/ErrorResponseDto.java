package cultureinfo.culture_app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String code; //에러코드
    private final String message; //사용자에게 보여줄 메세지

    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.getCode(), errorCode.getMessage());
    }
}
