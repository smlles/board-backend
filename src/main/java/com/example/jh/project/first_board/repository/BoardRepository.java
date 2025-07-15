package com.example.jh.project.first_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jh.project.first_board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	
	
}
