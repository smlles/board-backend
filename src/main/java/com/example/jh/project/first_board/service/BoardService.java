package com.example.jh.project.first_board.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jh.project.first_board.entity.BoardEntity;
import com.example.jh.project.first_board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository repository;

    /* CREATE ------------------------------------------------------------ */
    public List<BoardEntity> createPost(BoardEntity entity) {
        // 작성시간 ‧ 조회수 기본값 세팅
        entity.setCreateDate(LocalDateTime.now()
                             .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        entity.setView(0L);

        repository.save(entity);
        return repository.findAll();          // 글 목록 전체 반환
    }

    /* READ -------------------------------------------------------------- */
    public List<BoardEntity> retrieveAllPosts() {
        return repository.findAll();
    }

    @Transactional
    public BoardEntity retrieveOnePost(Long id) {
        BoardEntity target = repository.findById(id)
                              .orElseThrow(() -> new RuntimeException("글을 찾을 수 없습니다."));
        // 조회수 증가
        target.setView(target.getView() + 1);
        // dirty checking으로 자동 update
        return target;
    }

    /* UPDATE ------------------------------------------------------------ */
    public List<BoardEntity> updatePost(BoardEntity entity) {
        BoardEntity target = repository.findById(entity.getId())
                              .orElseThrow(() -> new RuntimeException("글을 찾을 수 없습니다."));

        // 필요한 필드만 수정
        target.setTitle(entity.getTitle());
        target.setDescription(entity.getDescription());
        target.setComment(entity.getComment());

        repository.save(target);
        return repository.findAll();
    }

    /* DELETE ------------------------------------------------------------ */
    public List<BoardEntity> deletePost(Long id) {
        repository.deleteById(id);
        return repository.findAll();
    }
//    조회수 증가 로직 
    @Transactional
    public void increaseView(Long id) {
        BoardEntity post = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글 없음"));
        post.setView(post.getView() + 1);
        repository.save(post); // JPA의 더티체킹으로 업데이트됨
    }
    
    public BoardEntity findPostById(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
    }
}
