package com.nakta.springlv1.domain.board.repository;

import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.entity.BoardLike;
import com.nakta.springlv1.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    public Optional<BoardLike> findByUserAndBoard(User user, Board board);
    public void deleteByUserAndBoard(User user, Board board);
}
