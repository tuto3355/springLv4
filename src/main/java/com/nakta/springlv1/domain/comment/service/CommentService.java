package com.nakta.springlv1.domain.comment.service;

import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.repository.BoardRepository;
import com.nakta.springlv1.domain.comment.dto.CommentRequestDto;
import com.nakta.springlv1.domain.comment.dto.CommentResponseDto;
import com.nakta.springlv1.domain.comment.entity.Comment;
import com.nakta.springlv1.domain.comment.exception.CommentErrorCode;
import com.nakta.springlv1.domain.comment.repository.CommentRepository;
import com.nakta.springlv1.domain.user.entity.UserRoleEnum;
import com.nakta.springlv1.domain.user.repository.UserRepository;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.domain.user.entity.User;
import com.nakta.springlv1.domain.user.jwt.JwtUtil;
import com.nakta.springlv1.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest req) {
        //토큰 검증
        String tokenValue = validateToken(req);

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
        User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() -> {
            throw new CustomException(CommentErrorCode.CANNOT_FIND_USER); //무조건 USER가 존재할수밖에 없지않나?
        });
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            throw new CustomException(CommentErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = new Comment(requestDto, user, board);
        Comment newComment = commentRepository.save(comment);

        return new CommentResponseDto(newComment);
    }

    @Transactional
    public CommentResponseDto modifyComment(Long boardId, Long commentId, CommentRequestDto requestDto, HttpServletRequest req) {
        //토큰 검증
        String tokenValue = validateToken(req);
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new CustomException(CommentErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(CommentErrorCode.CANNOT_FIND_COMMENT)
        );
        if (!(info.get("auth").equals("ADMIN"))) {
            if (!(info.getSubject().equals(comment.getUser().getUsername()))) {
                throw new CustomException(CommentErrorCode.ID_NOT_MATCH);
            }
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public StringResponseDto deleteComment(Long boardId, Long commentId, HttpServletRequest req) {
        //토큰 검증
        String tokenValue = validateToken(req);
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new CustomException(CommentErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(CommentErrorCode.CANNOT_FIND_COMMENT)
        );
        if (!(info.get("auth").equals("ADMIN"))) {
            if (!(info.getSubject().equals(comment.getUser().getUsername()))) {
                throw new CustomException(CommentErrorCode.ID_NOT_MATCH);
            }
        }
        commentRepository.deleteById(commentId);
        return new StringResponseDto("삭제가 잘 되었따");
    }

    private String validateToken(HttpServletRequest req) {
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        tokenValue = jwtUtil.substringToken(tokenValue);
        if (!jwtUtil.validateToken(tokenValue)) {
            throw new CustomException(CommentErrorCode.BAD_TOKEN);
        }
        return tokenValue;
    }
}
