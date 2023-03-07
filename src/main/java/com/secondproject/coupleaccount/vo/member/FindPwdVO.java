package com.secondproject.coupleaccount.vo.member;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindPwdVO {
    @Schema(description = "이름", example = "김실명")
    private String name;
    @Schema(description = "이메일(아이디)", example = "example@google.co.kr")
    private String email;    
    @Schema(description = "생일", example = "2022-03-03")
    private LocalDate birth;
}
