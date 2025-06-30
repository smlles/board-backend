package com.example.jh.project.first_board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class BoardEntity {

	@Id
	private Long id;
	private String title;
	private String description;
	private String author;
	private String createDate;
	private Long view;
	
	
	
}
