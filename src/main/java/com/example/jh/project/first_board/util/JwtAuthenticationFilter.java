package com.example.jh.project.first_board.util;

import com.example.jh.project.first_board.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        System.out.println("filter 확인 url은 ... 바로! "+path);
        return path.equals("/api/users/register")
                || path.equals("/api/users/login")
                || path.startsWith("/api/comment") // 댓글 조회는 익명 접근 가능
                || path.equals("/error")
                || request.getMethod().equals("OPTIONS");
       
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JwtAuthenticationFilter 진입 - 요청 URI: " + request.getRequestURI());
        String token = parseJwt(request);
        System.out.println("📦 파싱된 토큰: " + token);

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                // JWT에서 이메일 추출
                String userEmail = jwtUtil.validateTokenAndGetEmail(token);
                System.out.println("✅ JWT 인증 필터 - 사용자 확인됨: " + userEmail);

                // Claims 직접 파싱
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // DB에서 UserDetails 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 인증 객체 생성 및 SecurityContext 등록
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities().isEmpty()
                                        ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                                        : userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("✅ SecurityContext에 인증 객체 등록 완료");

            } catch (UsernameNotFoundException e) {
                System.out.println("❌ 사용자 없음: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ JWT 필터 예외: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ 유효하지 않거나 누락된 JWT");
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        // Authorization 헤더가 없으면 쿠키에서 검색
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
