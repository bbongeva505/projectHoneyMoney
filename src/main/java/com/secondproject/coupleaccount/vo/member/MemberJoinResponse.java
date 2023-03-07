package com.secondproject.coupleaccount.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberJoinResponse {
    @Schema(description = "상태", example = "true(회원가입 통과시)")
    private Boolean status;
    @Schema(description = "메세지", example = "회원가입 완료")
    private String message;
}
