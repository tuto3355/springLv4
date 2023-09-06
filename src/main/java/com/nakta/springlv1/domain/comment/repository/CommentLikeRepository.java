package com.nakta.springlv1.domain.comment.repository;

import com.nakta.springlv1.domain.comment.entity.Comment;
import com.nakta.springlv1.domain.comment.entity.CommentLike;
import com.nakta.springlv1.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    public Optional<CommentLike> findByUserAndComment(User user, Comment comment);
    public void deleteByUserAndComment(User user, Comment comment);

}

