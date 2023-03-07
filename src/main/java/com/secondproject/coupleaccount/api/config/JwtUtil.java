package com.secondproject.coupleaccount.api.config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.key}")
    private String key;

    public String create(Long userSeq) {
        return JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
            .withClaim("mbiSeq", userSeq)
            .sign(Algorithm.HMAC256(key));
    }

    public Long verifyAndExtractClaim(String token) {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token).getClaim("mbiSeq").asLong();
    }

    public String resolve(String token) {
        return token.replace(JwtProperties.TOKEN_PREFIX, "");
    }

    public long getExpireTime(String token) {
        return JWT.require(Algorithm.HMAC256(key)).build().verify(token).getExpiresAt().getTime();
    }

    public String getToken(Cookie[] cookies) {
        String token = Arrays.stream(cookies).filter(c -> c.isHttpOnly()&&c.getName().equals("token")).findFirst().orElseThrow().getValue();
        log.info("[JwtUtil] token = {}", token);  
        return token;
    }

    public Authentication getAuthentication(Long userSeq) {
        return new UsernamePasswordAuthenticationToken(userSeq, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
