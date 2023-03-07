package com.secondproject.coupleaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginMemberVO {
    @Schema(description = "회원 이메일", example = "1234")
    private String email;
    @Schema(description = "회원 패스워드", example = "1234")
    private String password;
}
