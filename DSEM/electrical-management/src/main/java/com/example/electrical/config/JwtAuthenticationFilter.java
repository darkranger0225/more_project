package com.example.electrical.config;

import com.example.electrical.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.electrical.common.Constants.HEADER_AUTHORIZATION;
import static com.example.electrical.common.Constants.TOKEN_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 如果没有token，直接放行（简化权限验证）
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        try {
            if (jwtUtil.validateToken(token)) {
                // 解析token获取用户信息
                Claims claims = jwtUtil.parseToken(token);
                String userId = claims.getSubject();
                String account = claims.get("account", String.class);
                Integer role = claims.get("role", Integer.class);

                // 根据角色设置权限
                List<SimpleGrantedAuthority> authorities;
                if (role != null) {
                    switch (role) {
                        case 2: // 系统管理员
                            authorities = Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_ADMIN"),
                                new SimpleGrantedAuthority("ROLE_SYS_ADMIN")
                            );
                            break;
                        case 1: // 公寓管理员
                            authorities = Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                            );
                            break;
                        default: // 学生
                            authorities = Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_USER")
                            );
                            break;
                    }
                } else {
                    authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                }

                // 设置Spring Security上下文
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 将用户信息存入request属性，供后续使用
                request.setAttribute("userId", Long.parseLong(userId));
                request.setAttribute("account", account);
                request.setAttribute("role", role);
            }
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            // token过期也放行，让业务层决定如何处理
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            // token验证失败也放行
        }

        // 无论token是否有效，都放行请求
        filterChain.doFilter(request, response);
    }
}
