package com.nakta.springlv1.domain.board.service;

import com.nakta.springlv1.domain.board.dto.BoardRequestDto;
import com.nakta.springlv1.domain.board.dto.BoardResponseDto;
import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.entity.BoardLike;
import com.nakta.springlv1.domain.board.repository.BoardLikeRepository;
import com.nakta.springlv1.domain.board.repository.BoardRepository;
import com.nakta.springlv1.domain.comment.dto.CommentResponseDto;
import com.nakta.springlv1.domain.comment.entity.Comment;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        Board newboard = boardRepository.save(board);
        return new BoardResponseDto(newboard);
    }

    public List<BoardResponseDto> getAllBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(BoardResponseDto::new)
                .map(this::addCommentListByBoard_id)
                .toList();
    }

    public BoardResponseDto getOneBoard(Long id) {
        Board board = findBoardById(id);
        BoardResponseDto responseDto = new BoardResponseDto(board);
        return addCommentListByBoard_id(responseDto);
    }

    @Transactional
    public BoardResponseDto modifyBoard(Long id, BoardRequestDto requestDto, User user) {

        //작성자 일치 확인
        Board board = findBoardById(id);

        if(!(user.getRole()== UserRoleEnum.ADMIN)) {
            if (!(user.getUsername().equals(board.getUsername()))) {
                throw new CustomException(ErrorCode.ID_NOT_MATCH);
            }
        }
        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    public StringResponseDto deleteBoard(Long id, User user) {

        //작성자 일치 확인
        Board board = findBoardById(id);

        if(!(user.getRole()== UserRoleEnum.ADMIN)) {
            if (!(user.getUsername().equals(board.getUsername()))) {
                throw new CustomException(ErrorCode.ID_NOT_MATCH);
            }
        }
        boardRepository.deleteById(id);
        return new StringResponseDto("삭제를 성공하였음");
    }

    @Transactional
    public StringResponseDto likeBoard(Long id, User user) {
        Board board = findBoardById(id);
        Optional<BoardLike> boardLike = boardLikeRepository.findByUserAndBoard(user, board);
        if (boardLike.isPresent()) {
            board.updateLikes(-1);
            boardLikeRepository.deleteByUserAndBoard(user, board);
            return new StringResponseDto("좋아요 취소 성공!!");
        } else {
            board.updateLikes(1);
            boardLikeRepository.save(new BoardLike(user, board));
            return new StringResponseDto("좋아요 성공!!");
        }
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CANNOT_FIND_BOARD));
    }

    private BoardResponseDto addCommentListByBoard_id(BoardResponseDto responseDto) {
        List<Comment> list = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(responseDto.getId());
        responseDto.addCommentList(list.stream().map(CommentResponseDto::new).toList());
        return responseDto;
    }


}