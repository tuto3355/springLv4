package com.nakta.springlv1.domain.comment.controller;

import com.nakta.springlv1.domain.comment.dto.CommentRequestDto;
import com.nakta.springlv1.domain.comment.dto.CommentResponseDto;
import com.nakta.springlv1.domain.comment.service.CommentService;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.global.Security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{commentId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.createComment(commentId,requestDto,userDetails.getUser()));
    }

    @PutMapping("/board/{boardId}/{commentId}")
    public ResponseEntity<CommentResponseDto> modifyComment(
            @PathVariable Long boardId, @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.modifyComment(boardId,commentId,requestDto,userDetails.getUser()));
    }

    @DeleteMapping("/board/{boardId}/{commentId}")
    public ResponseEntity<StringResponseDto> deleteComment(
            @PathVariable Long boardId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.deleteComment(boardId, commentId, userDetails.getUser()));
    }
}
