package com.secondproject.coupleaccount.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token_black_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TokenBlackList {
    
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "expire_time")
    private Long expireTime;
}
