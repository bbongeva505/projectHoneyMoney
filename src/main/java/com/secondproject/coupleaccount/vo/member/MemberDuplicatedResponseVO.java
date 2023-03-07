package com.secondproject.coupleaccount.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberDuplicatedResponseVO {
    @Schema(description = "상태", example = "true")
    private Boolean status;
    @Schema(description = "메시지", example = "회원가입 가능")
    private String message;
}
