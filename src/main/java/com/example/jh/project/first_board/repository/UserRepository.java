package com.example.jh.project.first_board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jh.project.first_board.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(String id);        // 여기 id는 String 타입으로 변경됨
    Optional<UserEntity> findByEmail(String email);

    boolean existsById(String id);
    boolean existsByEmail(String email);
}