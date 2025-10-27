package com.example.jh.project.first_board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.jh.project.first_board.DTO.CommentDTO;
import com.example.jh.project.first_board.DTO.ResponseDTO;
import com.example.jh.project.first_board.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseDTO<CommentDTO> createComment(@RequestBody CommentDTO dto) {
        CommentDTO comment = commentService.createComment(dto);
        return ResponseDTO.<CommentDTO>builder().data(List.of(comment)).build();
    }

    // 특정 게시글 댓글 조회
    @GetMapping("/board/{boardId}")
    public ResponseDTO<CommentDTO> getComments(@PathVariable("boardId") Long boardId) {
        List<CommentDTO> comments = commentService.getCommentsByBoard(boardId);
        return ResponseDTO.<CommentDTO>builder().data(comments).build();
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseDTO<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseDTO.builder().data(null).build();
    }
}