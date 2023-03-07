package com.secondproject.coupleaccount.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoupleMonthExpenseResponseVO {
    private Boolean status;
    private String message;
    @Schema(description = "월 별 총 지출액" ,example = "10000")
    private Integer ExpenseTotal;
}