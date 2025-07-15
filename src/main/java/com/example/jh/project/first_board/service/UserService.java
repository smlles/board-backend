package com.example.jh.project.first_board.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    

    public void saveUser(UserDTO userDTO) {
        UserEntity user = UserDTO.toEntity(userDTO);
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        
        userRepository.save(user);
    }
// 가입이 된건지 확인 하기 위한 유저 찾기
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

 // 로그인 - 성공 시 UserEntity 반환, 실패 시 null 반환
    public UserEntity loginUser(UserDTO loginDTO) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return user;
            }
        }
        return null;
    }
    // 회원 정보 수정 - 성공 시 수정된 UserDTO 반환, 실패 시 null 반환
//     정보 수정하기 전에, 원래 데이터를 가져올 메서드 
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return new UserDTO(userEntity);
    }
    
//    여기가 수정 
    public UserDTO updateUser(Long userId, UserDTO dto) {
    	 Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            user.setUsername(dto.getUsername());  // 닉네임 변경 등

            userRepository.save(user);
            return new UserDTO(user);
        }
        return null;
    }
//    비밀번호 재설정 
    public String resetPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        // 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 10);
        //암호화
        String encryptedPassword = passwordEncoder.encode(tempPassword);
        //재설정
        user.setPassword(encryptedPassword);
        //저장
        userRepository.save(user);

        return tempPassword;
    }
    

    // 회원 탈퇴 - 성공 시 true 반환, 실패 시 false 반환
    public boolean deleteUser(String id) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(id);
        if (userOpt.isEmpty()) {
            return false;
        }
        userRepository.delete(userOpt.get());
        return true;
    }
}
