package com.example.jh.project.first_board.DTO;

import com.example.jh.project.first_board.entity.CommentEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private Long boardId;

    public CommentDTO(CommentEntity entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.boardId = entity.getBoard().getId();
    }
    
    public static CommentEntity toEntity(CommentDTO dto) {
        return CommentEntity.builder()
                .content(dto.getContent())
                .author(dto.getAuthor())
                .build(); // board는 Service에서 set
    }
}