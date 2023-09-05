package com.nakta.springlv1.domain.comment.entity;

import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.entity.Timestamped;
import com.nakta.springlv1.domain.comment.dto.CommentRequestDto;
import com.nakta.springlv1.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Comment")
@Getter
@NoArgsConstructor
public class Comment extends Timestamped { // Timestamped가 board 패키지에 있어서 매우 찝찝함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Comment(CommentRequestDto requestDto, User user, Board board) {
        this.message = requestDto.getMessage();
        this.user = user;
        this.username = user.getUsername();
        this.board = board;
    }

    public void update(CommentRequestDto requestDto) {
        this.message = requestDto.getMessage();
    }
}
