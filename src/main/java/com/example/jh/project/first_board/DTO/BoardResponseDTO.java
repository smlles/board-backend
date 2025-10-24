package com.example.jh.project.first_board.DTO;

import com.example.jh.project.first_board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BoardResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String author;
    private String createDate;
    private Long view;

    // Entity → DTO 변환 생성자
    public BoardResponseDTO(BoardEntity board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.author = board.getAuthor(); // UserEntity의 username
        this.createDate = board.getCreateDate();
        this.view = board.getView();
    }
}