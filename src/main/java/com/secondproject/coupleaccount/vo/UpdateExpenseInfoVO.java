package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class UpdateExpenseInfoVO {
    @Schema(description = "수정 지출 가격" ,example = "300000")
    private Integer updatePrice;
    @Schema(description = "수정 지출 날짜" ,example = "2022-02-13")
    private LocalDate updateDate;
    @Schema(description = "수정 지출 메모" ,example = "생각해보니 편의점에서 3만원 나 혼자 썼음.")
    private String updateMemo;
    @Schema(description = "지출대상(본인:0, 우리<공동>:1)", defaultValue = "1", allowableValues = {"0", "1"},example = "0")
    private Integer updateStatus;
    @Schema(description = "수정 지출 카테고리 번호 " ,example = "2")
    private Long updateCateSeq;
}
