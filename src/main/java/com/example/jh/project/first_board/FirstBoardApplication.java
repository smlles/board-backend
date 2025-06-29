package com.example.jh.project.first_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.jh.project.first_board.repository")
@SpringBootApplication(scanBasePackages = "com.example.jh.project.first_board")
@EntityScan(basePackages = "com.example.jh.project.first_board.entity")
public class FirstBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstBoardApplication.class, args);
	}

}
