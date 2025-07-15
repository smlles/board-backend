package com.example.jh.project.first_board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.jh.project.first_board.DTO.UserDTO;
import com.example.jh.project.first_board.entity.UserEntity;
import com.example.jh.project.first_board.security.CustomUserDetails;
import com.example.jh.project.first_board.service.UserService;
import com.example.jh.project.first_board.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;  // 주입

    
//  회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return ResponseEntity.ok().body("회원가입 성공");
    }
// 회원가입 확인용 GET
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginDTO) {
        UserEntity user = userService.loginUser(loginDTO);
        if (user != null) {
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
//    수정할 때 얻을 개인 데이터
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("▶ 사용자 인증 객체: " + userDetails);
        // 아래 코드 중 NPE나 Casting 에러 발생 가능성
        String email = userDetails.getUsername(); // 또는 getEmail()
        UserDTO userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }
//    회원 정보 수정
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody UserDTO dto) {
        try {
            Long userId = userDetails.getUser().getUuid(); // ✅ 이미 Long임
            UserDTO updated = userService.updateUser(userId, dto); // 메서드도 Long 타입
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
        	return null;
        }
    }
//    비밀번호 잊어버렸을 때, 강제 재설정 
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
        	  String tempPassword = userService.resetPassword(email);
              return ResponseEntity.ok(Map.of("tempPassword", tempPassword));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호 초기화 실패: " + e.getMessage());
        }
    }
//    회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        boolean deleted = userService.deleteUser(userDetails.getUsername());
        if (deleted) {
            return ResponseEntity.ok("회원 탈퇴 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        }
    }
}
