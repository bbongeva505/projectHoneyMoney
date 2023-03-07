package com.secondproject.coupleaccount.vo.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalExpenseVO {
    @Schema(description = "총 지출액", example = "500000")
    private Integer totalExpense;
}

