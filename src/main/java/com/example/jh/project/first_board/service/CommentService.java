package com.example.jh.project.first_board.service;

import com.example.jh.project.first_board.DTO.CommentDTO;
import com.example.jh.project.first_board.entity.BoardEntity;
import com.example.jh.project.first_board.entity.CommentEntity;
import com.example.jh.project.first_board.repository.BoardRepository;
import com.example.jh.project.first_board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    public CommentDTO createComment(CommentDTO dto) {
        CommentEntity entity = CommentDTO.toEntity(dto);
        entity.setCreateDate(LocalDateTime.now());

        // 게시글 연결
        BoardEntity board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
        entity.setBoard(board);

        commentRepository.save(entity);
        return new CommentDTO(entity);
    }

    // 특정 게시글 댓글 조회
    public List<CommentDTO> getCommentsByBoard(Long boardId) {
        List<CommentEntity> comments = commentRepository.findByBoardId(boardId);
        return comments.stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}