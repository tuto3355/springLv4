package com.nakta.springlv1.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ID_NOT_MATCH(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    CANNOT_FIND_BOARD(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
    CANNOT_FIND_COMMENT(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "중복된 아이디입니다."),
    ADMINTOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "관리자 패스워드가 일치하지 않습니다.");


//    BAD_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
//    CANNOT_FIND_USER(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
//    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
//    ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
