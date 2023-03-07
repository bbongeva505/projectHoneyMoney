package com.secondproject.coupleaccount.api.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondproject.coupleaccount.repository.TokenBlackListRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    private final TokenBlackListRepository tokenBlackListRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

         String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
         if(StringUtils.hasText(authorization) && authorization.startsWith(JwtProperties.TOKEN_PREFIX)) {
            try {
                String token = jwtUtil.resolve(authorization);
                Long userSeq = jwtUtil.verifyAndExtractClaim(token);

                // 로그인한 유저의 토큰값을 이용하는지 검증
                if(tokenBlackListRepo.existsById(token)) {
                    log.error("로그아웃한 유저의 토큰값으로 접근");  
                    response.sendError(403, "유효하지않은 토큰값입니다."); 
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userSeq, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("인증완료");
                doFilter(request, response, filterChain);
            } catch (JWTVerificationException e) {
                log.error(e.getMessage());
                response.setStatus(401);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(jsonResponseWrapper(e));
            }
            return;
        }
        doFilter(request, response, filterChain);
    }

    private static String jsonResponseWrapper(Exception e) throws JsonProcessingException {


        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "Invalid JWT");
        jsonMap.put("reason", e.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(jsonMap);
    }
    
}
