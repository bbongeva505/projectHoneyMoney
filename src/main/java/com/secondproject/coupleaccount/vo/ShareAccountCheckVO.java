package com.secondproject.coupleaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ShareAccountCheckVO {
    @Schema(description = "공유통장 코드", example = "Z9dfOiTWrz")
    private String accountCode;
}
