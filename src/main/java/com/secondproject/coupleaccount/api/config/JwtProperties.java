package com.secondproject.coupleaccount.api.config;

public interface JwtProperties {
    long EXPIRATION_TIME = 1000 * 60 * 60 * 12;
    String TOKEN_PREFIX = "Bearer ";
}
