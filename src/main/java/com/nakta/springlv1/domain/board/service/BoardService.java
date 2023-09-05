package com.nakta.springlv1.domain.board.service;

import com.nakta.springlv1.domain.board.dto.BoardRequestDto;
import com.nakta.springlv1.domain.board.dto.BoardResponseDto;
import com.nakta.springlv1.domain.board.exception.BoardErrorCode;
import com.nakta.springlv1.domain.board.repository.BoardRepository;
import com.nakta.springlv1.domain.comment.dto.CommentResponseDto;
import com.nakta.springlv1.domain.comment.entity.Comment;
import com.nakta.springlv1.domain.comment.exception.CommentErrorCode;
import com.nakta.springlv1.domain.comment.repository.CommentRepository;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.user.entity.User;
import com.nakta.springlv1.domain.user.jwt.JwtUtil;
import com.nakta.springlv1.domain.user.repository.UserRepository;
import com.nakta.springlv1.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest req) {

        //토큰 검증
        String tokenValue = validateToken(req);

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
        User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() -> {
            throw new CustomException(BoardErrorCode.CANNOT_FIND_USER); //무조건 USER가 존재할수밖에 없지않나?
        });

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
        Board board = findById(id);
        BoardResponseDto responseDto = new BoardResponseDto(board);
        return addCommentListByBoard_id(responseDto);
//        return addCommentListByBoard_id(new BoardResponseDto(findById(id)));  //한줄로 써도되나? 가독성 괜찮나?
    }

    @Transactional
    public BoardResponseDto modifyBoard(Long id, BoardRequestDto requestDto, HttpServletRequest req) {

        //토큰 검증
        String tokenValue = validateToken(req);

        //작성자 일치 확인
        Board board = findById(id);
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        if(!(info.get("auth").equals("ADMIN"))) {
            if (!(info.getSubject().equals(board.getUsername()))) {
                throw new CustomException(BoardErrorCode.ID_NOT_MATCH);
            }
        }
        board.update(requestDto, info.getSubject());
        return new BoardResponseDto(board);
    }

    public StringResponseDto deleteBoard(Long id, HttpServletRequest req) {

        //토큰 검증
        String tokenValue = validateToken(req);

        //작성자 일치 확인
        Board board = findById(id);
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        if(!(info.get("auth").equals("ADMIN"))) {
            if (!(info.getSubject().equals(board.getUsername()))) {
                throw new CustomException(BoardErrorCode.ID_NOT_MATCH);
            }
        }
        boardRepository.deleteById(id);
        return new StringResponseDto("삭제를 성공하였음");
    }

    private Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(BoardErrorCode.CANNOT_FIND_BOARD));
    }

    private String validateToken(HttpServletRequest req) {
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        tokenValue = jwtUtil.substringToken(tokenValue);
        if (!jwtUtil.validateToken(tokenValue)) {
            throw new CustomException(BoardErrorCode.BAD_TOKEN);
        }
        return tokenValue;
    }

    private BoardResponseDto addCommentListByBoard_id(BoardResponseDto responseDto) {
        List<Comment> list = commentRepository.findAllByBoard_IdOrderByModifiedAtDesc(responseDto.getId());
        responseDto.addCommentList(list.stream().map(CommentResponseDto::new).toList());
        return responseDto;
    }
}