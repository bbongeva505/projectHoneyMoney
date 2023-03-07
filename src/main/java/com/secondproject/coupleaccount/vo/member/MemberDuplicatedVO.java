package com.secondproject.coupleaccount.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberDuplicatedVO {
    @Schema(description = "회원 이메일", example = "1234")
    private String userEmail;
}
