package com.example.jh.project.first_board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.jh.project.first_board.DTO.UserDTO;
import com.example.jh.project.first_board.service.UserService;
import com.example.jh.project.first_board.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    
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

//    로그인 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginDTO) {
        UserDTO user = userService.loginUser(loginDTO);
        if (user != null) {
            String token = JwtUtil.generateToken(user.getId());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

//    회원 정보 수정
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO updatedUser) {

        UserDTO updated = userService.updateUser(userDetails.getUsername(), updatedUser);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
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
