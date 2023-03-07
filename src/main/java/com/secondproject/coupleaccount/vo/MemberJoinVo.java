package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberJoinVo {
    @Schema(description = "회원 이메일", example = "1234")
    private String mbiBasicEmail;
    @Schema(description = "회원 비밀번호", example = "1234")
    private String password;
    @Schema(description = "회원 성별", example = "(1:남자, 2:여자)")
    private Integer gender;
    @Schema(description = "회원 이름(실명)", example = "김실명")
    private String name;
    @Schema(description = "회원 기념일(사귄날)", example = "2022-02-02")
    private LocalDate mbiStartDay;
    @Schema(description = "회원 생일", example = "2000-05-21")
    private LocalDate mbiBrith;
    @Schema(description = "회원 닉네임", example = "닉네임예시1")
    private String nickName;    
    @Schema(description = "데이트 통장 고유번호", example = "Z9dfOiTWrz")
    private String accountNumber;
}
