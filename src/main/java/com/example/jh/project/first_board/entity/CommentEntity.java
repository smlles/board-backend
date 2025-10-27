package com.example.jh.project.first_board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class CommentEntity {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, length = 500)
	    private String content;

	    @Column(nullable = false)
	    private String author; // 작성자 닉네임 or 이메일
	    private LocalDateTime createDate;

	    // 여러 댓글이 하나의 게시글(Board)에 속함 (N:1)
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "board_id")
	    private BoardEntity board;
}
