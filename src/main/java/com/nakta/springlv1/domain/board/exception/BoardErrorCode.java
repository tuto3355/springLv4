package com.nakta.springlv1.domain.board.exception;

import com.nakta.springlv1.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode { //enum 클래스 이해 필요

    ID_NOT_MATCH(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    BAD_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    CANNOT_FIND_USER(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
    CANNOT_FIND_BOARD(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다.");
//    ID_NOT_MATCH(HttpStatus.NOT_FOUND, "아이디를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
