package cultureinfo.culture_app.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 인증/인가 관련
    LOGIN_REQUIRED("AUTH_001", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ACCESS_DENIED("AUTH_002", HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    UNAUTHORIZED_MODIFICATION("AUTH_003", HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    UNAUTHORIZED_DELETION("AUTH_004", HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),


    // 사용자 인증/회원 관련
    EMAIL_ALREADY_EXISTS("USER_001", HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EMAIL_NOT_FOUND("USER_002", HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    ID_EMAIL_NOT_MATCH("USER_003", HttpStatus.NOT_FOUND, "아이디와 이메일이 일치하지 않습니다."),
    MEMBER_NOT_FOUND("USER_004", HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    INVALID_PASSWORD("USER_005", HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_ID("USER_006", HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
    USERNAME_ALREADY_EXISTS("USER_007", HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    NICKNAME_ALREADY_EXISTS("USER_008", HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

    // 이메일/인증 관련
    EMAIL_SEND_FAILED("MAIL_001", HttpStatus.BAD_GATEWAY, "이메일 전송에 실패했습니다."),
    AUTH_CODE_NOT_FOUND("AUTH_010", HttpStatus.BAD_REQUEST, "인증 코드가 존재하지 않거나 만료되었습니다."),
    AUTH_CODE_MISMATCH("AUTH_011", HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    TEMP_PASSWORD_SAVE_FAILED("USER_010", HttpStatus.INTERNAL_SERVER_ERROR, "임시 비밀번호 저장에 실패했습니다."),
    INQUIRY_SEND_FAILED("MAIL_002", HttpStatus.BAD_GATEWAY, "문의 이메일 전송에 실패했습니다."),

    // 파일
    FILE_UPLOAD_FAILED("FILE_001", HttpStatus.BAD_GATEWAY, "파일 업로드 중 오류가 발생했습니다."),
    FILE_DELETE_FAILED("FILE_002", HttpStatus.BAD_GATEWAY, "파일 삭제 중 오류가 발생했습니다."),

    // 콘텐츠/게시글/댓글 관련
    ENTITY_NOT_FOUND("COMMON_001", HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
    CONTENT_NOT_FOUND("CONTENT_001", HttpStatus.NOT_FOUND, "존재하지 않는 콘텐츠입니다."),
    SESSION_NOT_FOUND("CONTENT_002", HttpStatus.NOT_FOUND, "존재하지 않는 세션입니다."),
    SMALL_CATEGORY_NOT_FOUND("CONTENT_003", HttpStatus.NOT_FOUND, "존재하지 않는 소분류입니다."),
    ARTICLE_NOT_FOUND("CONTENT_004", HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND("CONTENT_005", HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    ARTICLE_LIKE_NOT_FOUND("CONTENT_006", HttpStatus.NOT_FOUND, "게시글 좋아요가 존재하지 않습니다."),
    COMMENT_LIKE_NOT_FOUND("CONTENT_007", HttpStatus.NOT_FOUND, "게시글 좋아요가 존재하지 않습니다."),

    //토큰 관련
    EXPIRED_JWT_EXCEPTION("TOKEN_001", HttpStatus.FORBIDDEN, "토큰이 만료되었습니다."),
    JWT_EXCEPTION("TOKEN_002", HttpStatus.FORBIDDEN, "토큰에 문제가 있습니다."),
    RUNTIME_EXCEPTION("TOKEN_003", HttpStatus.UNAUTHORIZED, "REFRESH TOKEN이 유효하지 않습니다."),

    // 공통
    INTERNAL_SERVER_ERROR("COMMON_999", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final String code;      // 고유한 에러 코드
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getCode() { return code; }
    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
