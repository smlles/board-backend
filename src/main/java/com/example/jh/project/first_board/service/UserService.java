package com.example.jh.project.first_board.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jh.project.first_board.DTO.UserDTO;
import com.example.jh.project.first_board.entity.UserEntity;
import com.example.jh.project.first_board.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserDTO userDTO) {
        UserEntity user = UserDTO.toEntity(userDTO);
        userRepository.save(user);
    }
// 가입이 된건지 확인 하기 위한 유저 찾기
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    // 로그인 - 성공 시 UserDTO 반환, 실패 시 null 반환
    public UserDTO loginUser(UserDTO loginDTO) {
        Optional<UserEntity> userOpt = userRepository.findById(loginDTO.getId());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (user.getPassword().equals(loginDTO.getPassword())) {
                return new UserDTO(user);
            }
        }
        return null;
    }

    // 회원 정보 수정 - 성공 시 수정된 UserDTO 반환, 실패 시 null 반환
    public UserDTO updateUser(String id, UserDTO updatedUser) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return null;
        }
        UserEntity user = userOpt.get();

        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());

        userRepository.save(user);
        return new UserDTO(user);
    }

    // 회원 탈퇴 - 성공 시 true 반환, 실패 시 false 반환
    public boolean deleteUser(String id) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return false;
        }
        userRepository.delete(userOpt.get());
        return true;
    }
}
