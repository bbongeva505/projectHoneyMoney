package com.secondproject.coupleaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MyAccountInfoVO {
    @Schema(description = "총 입금액", example = "1000000")
    private Integer totalImport;
    @Schema(description = "총 지출액", example = "500000")
    private Integer totalExpense;
    @Schema(description = "총 잔액", example = "500000")
    private Integer balance;
}
