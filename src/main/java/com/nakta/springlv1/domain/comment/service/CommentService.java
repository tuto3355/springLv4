package com.nakta.springlv1.domain.comment.service;

import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.repository.BoardRepository;
import com.nakta.springlv1.domain.comment.dto.CommentRequestDto;
import com.nakta.springlv1.domain.comment.dto.CommentResponseDto;
import com.nakta.springlv1.domain.comment.entity.Comment;
import com.nakta.springlv1.domain.comment.entity.CommentLike;
import com.nakta.springlv1.domain.comment.repository.CommentLikeRepository;
import com.nakta.springlv1.domain.comment.repository.CommentRepository;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.domain.user.entity.User;
import com.nakta.springlv1.domain.user.entity.UserRoleEnum;
import com.nakta.springlv1.domain.user.jwt.JwtUtil;
import com.nakta.springlv1.domain.user.repository.UserRepository;
import com.nakta.springlv1.global.exception.CustomException;
import com.nakta.springlv1.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {

        Board board = boardRepository.findById(id).orElseThrow(() -> {
            throw new CustomException(ErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = new Comment(requestDto, user, board);
        Comment newComment = commentRepository.save(comment);

        return new CommentResponseDto(newComment);
    }

    @Transactional
    public CommentResponseDto modifyComment(Long boardId, Long commentId, CommentRequestDto requestDto, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.CANNOT_FIND_COMMENT)
        );
        if(!(user.getRole()== UserRoleEnum.ADMIN)) {
            if (!(user.getUsername().equals(board.getUsername()))) {
                throw new CustomException(ErrorCode.ID_NOT_MATCH);
            }
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public StringResponseDto deleteComment(Long boardId, Long commentId, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.CANNOT_FIND_BOARD);
        });
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.CANNOT_FIND_COMMENT)
        );
        if(!(user.getRole()== UserRoleEnum.ADMIN)) {
            if (!(user.getUsername().equals(board.getUsername()))) {
                throw new CustomException(ErrorCode.ID_NOT_MATCH);
            }
        }
        commentRepository.deleteById(commentId);
        return new StringResponseDto("삭제가 잘 되었따");
    }
    @Transactional
    public StringResponseDto likeBoard(Long id, User user) {
        Comment comment = findCommentById(id);
        Optional<CommentLike> commentLike = commentLikeRepository.findByUserAndComment(user, comment);
        if (commentLike.isPresent()) {
            comment.updateLikes(-1);
            commentLikeRepository.deleteByUserAndComment(user, comment);
            return new StringResponseDto("좋아요 취소 성공!!");
        } else {
            comment.updateLikes(1);
            commentLikeRepository.save(new CommentLike(user, comment));
            return new StringResponseDto("좋아요 성공!!");
        }
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CANNOT_FIND_COMMENT));
    }
}
