package com.example.jh.project.first_board.DTO;

import com.example.jh.project.first_board.entity.BoardEntity;
import com.example.jh.project.first_board.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	private Long id;
	private String title;
	 
	private String description;
	private String author;
	private String createDate;
	private Long view;
	private String comment;
	
	
	
	public BoardDTO(BoardEntity boardEntity) {
		super();
		this.id = boardEntity.getId();
		this.title = boardEntity.getTitle();
		this.description = boardEntity.getDescription();
		this.author = boardEntity.getAuthor();
		this.createDate = boardEntity.getCreateDate();
		this.view = boardEntity.getView();
		this.comment = boardEntity.getComment();
	}
	
	
	public static BoardEntity toEntity(BoardDTO dto) {
	       return  BoardEntity.builder()
	    		    .id(dto.getId())
	    		    .title(dto.getTitle())
	                .description(dto.getDescription())
	                .author(dto.getAuthor())
	                .createDate(dto.getCreateDate())
	                .view(dto.getView())
	                .comment(dto.getComment())
	                .build();
	    }
	
}



