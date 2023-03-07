package com.secondproject.coupleaccount.vo.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseVO {
    @Schema(description = "상태", example = "true")
    private Boolean status;
    @JsonProperty("Autorization")
    @Schema(description = "Autorization", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzY5MjEwNjksIm1iaVNlcSI6Mn0.79iGHLqGBej40PnzbM-wsA8WP6rGenEXXAQTZQ2lEzU")
    private String Authorization;
    @JsonProperty("message")
    @Schema(description = "message", example = "로그인 성공")
    private String message;

    
}
