package com.example.jh.project.first_board.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.jh.project.first_board.entity.UserEntity;

@Service
public class JwtUtil {

    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1일

    // JWT 생성
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("uid", user.getUuid()) // 사용자 UID
                .claim("name", user.getUsername()) // 사용자 이름
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    // JWT 검증
    public boolean validateToken(String token) {
    	System.out.println("✅ 토큰 유효성 검사 시작");
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            System.out.println("🟢 토큰 유효성 검사 통과");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 id 추출
    public String validateTokenAndGetEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(KEY)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   ;
    }
//    key return
    public Key getKey() {
        return KEY;
    }

    
    
}
