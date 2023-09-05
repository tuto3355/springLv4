package com.nakta.springlv1.domain.comment.repository;

import com.nakta.springlv1.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoard_IdOrderByModifiedAtDesc(Long id);
}