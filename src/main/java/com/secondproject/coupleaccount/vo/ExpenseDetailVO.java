package com.secondproject.coupleaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseDetailVO {
    @Schema(description = "카테고리 이름 " ,example = "카페")
    private String category;
    @Schema(description = "지출액 " ,example = "20000")
    private Integer price;
    @Schema(description = "지출 메모 " ,example = "메모1")
    private String memo;
    @Schema(description = "지출이미지 이름 " ,example = "TimotheeChalametCMBYN")
    private String ImageUri;
    @Schema(description = "상태" ,example = "0. 본인 1. 공동")
    private Integer expenseStatus;
}
