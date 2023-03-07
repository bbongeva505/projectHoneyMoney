package com.secondproject.coupleaccount.vo;
import io.swagger.v3.oas.annotations.media.Schema;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// @Builder
public interface ExpenseVO {
    @Schema(description = "지출 고유번호 " ,example = "1")
    Long getExpenseSeq();
    // @Schema(description = "지출 카테고리명 " ,example = "카페")
    // String getCategory();
    @Schema(description = "지출 액" ,example = "20000")
    Integer getExpense();
}


