package com.example.jh.project.first_board.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.jh.project.first_board.DTO.BoardDTO;
import com.example.jh.project.first_board.DTO.BoardResponseDTO;
import com.example.jh.project.first_board.DTO.ResponseDTO;
import com.example.jh.project.first_board.entity.BoardEntity;
import com.example.jh.project.first_board.repository.BoardRepository;
import com.example.jh.project.first_board.security.CustomUserDetails;
import com.example.jh.project.first_board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

//	LocalDateTime now = LocalDateTime.now();
//	String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final BoardService service;
    private final BoardRepository boardRepository;

//    작성 
    @PostMapping
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody BoardDTO dto) {
    	System.out.println("userdetail : "+ userDetails);
        String userId = "테스트 계정";
        if (userDetails != null) {
            userId = userDetails.getUser().getUsername();  // 왜 못얻지 
        }
        System.out.println("작성자 userId: " + userId);
        BoardEntity entity = BoardDTO.toEntity(dto);
        entity.setId(null);
        entity.setAuthor(userId);
       

        List<BoardDTO> result = service.createPost(entity)
                                      .stream()
                                      .map(BoardDTO::new)
                                      .collect(Collectors.toList());

        ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder()
                                                    .data(result)
                                                    .build();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/boards")
    public ResponseDTO<BoardResponseDTO> getBoards() {
        List<BoardEntity> boards = boardRepository.findAll();
        List<BoardResponseDTO> boardDtos = boards.stream()
                .map(BoardResponseDTO::new)
                .toList();
        
        return ResponseDTO.<BoardResponseDTO>builder()
                .error(null)
                .data(boardDtos)
                .build();
    }

    /* --------------------- 글 목록 조회 --------------------- */
    @GetMapping
    public ResponseEntity<?> getBoardList() {
        List<BoardDTO> dtos = service.retrieveAllPosts()
                                     .stream().map(BoardDTO::new)
                                     .collect(Collectors.toList());
        ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder()
                                                    .data(dtos).build();
        return ResponseEntity.ok(response);
    }

    /* --------------------- 단일 글 조회 --------------------- */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable("id") Long id) {
        System.out.println("요청 들어옴: /board/" + id);
        try {
            BoardEntity entity = service.retrieveOnePost(id);
            System.out.println("조회된 게시글: " + entity);
            BoardDTO dto = new BoardDTO(entity);
            return ResponseEntity.ok(ResponseDTO.<BoardDTO>builder()
                    .data(List.of(dto)).build());
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body("에러: " + e.getMessage());
        }
    }
    /* --------------------- 글 수정 --------------------- */
    @PutMapping
    public ResponseEntity<?> updateBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody BoardDTO dto) {
        try {
            String currentUserEmail = userDetails.getUser().getEmail();

            // 기존 게시글의 작성자(email) 조회
            BoardEntity originalPost = service.findPostById(dto.getId());

            if (!originalPost.getAuthor().equals(currentUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
            }

            BoardEntity entity = BoardDTO.toEntity(dto);
            entity.setAuthor(currentUserEmail); // 보안 차원에서 이메일 덮어쓰기

            List<BoardDTO> result = service.updatePost(entity)
                                           .stream()
                                           .map(BoardDTO::new)
                                           .collect(Collectors.toList());

            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder()
                                                        .data(result).build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return badRequest(e);
        }
    }

    /* --------------------- 글 삭제 --------------------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("id") Long id,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
        	String currentUsername = userDetails.getUser().getUsername();
        	
            // 게시글 조회
            BoardEntity post = service.findPostById(id);

            if (!post.getAuthor().equals(currentUsername)) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN)
	           .body("삭제 권한이 없습니다.");
	}

            List<BoardDTO> result = service.deletePost(id)
                                           .stream()
                                           .map(BoardDTO::new)
                                           .collect(Collectors.toList());
            ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder()
                                                        .data(result).build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return badRequest(e);
        }
    }

    /* --------------------- 공통 에러 핸들러 --------------------- */
    private ResponseEntity<?> badRequest(Exception e) {
        ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder()
                                                    .error(e.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }
//    조회수 증가 로직 
    @PutMapping("/{id}/view")
    public ResponseEntity<?> increaseViewCount(@PathVariable Long id) {
        try {
            service.increaseView(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회수 증가 실패");
        }
    }
    
}
