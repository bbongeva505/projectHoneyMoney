package com.secondproject.coupleaccount.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindPwdResponseVO {
    @Schema(description = "상태", example = "true")
    private Boolean status;
    @Schema(description = "메시지", example = "임시 비밀번호 발송완료")
    private String message;

    public FindPwdResponseVO(Boolean status, String message){
        this.status = status;
        this.message = message;
    }
}
