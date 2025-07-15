package com.example.jh.project.first_board.DTO;

import com.example.jh.project.first_board.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	
	private Long uuid;
	private String email;
	private String password;
	private String username;
	
	
	
	
	public UserDTO(UserEntity entity) {
		
		this.uuid=entity.getUuid();
		this.email = entity.getEmail();
		this.password = entity.getPassword();
		this.username = entity.getUsername();
	}

	
	
	 public static UserEntity toEntity(UserDTO dto) {
	       return  UserEntity.builder()
	    		    .uuid(dto.getUuid())
	                .email(dto.getEmail())
	                .password(dto.getPassword())
	                .username(dto.getUsername())
	                .build();
	    }
	
	
}
