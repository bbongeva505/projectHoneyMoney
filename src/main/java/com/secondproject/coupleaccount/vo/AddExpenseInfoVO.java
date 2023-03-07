package com.secondproject.coupleaccount.vo;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddExpenseInfoVO {
    @Schema(description = "지출 가격" ,example = "10000")
    private Integer price;
    @Schema(description = "지출 정보 메모" , example = "편의점 간식")
    private String memo;
    @Schema(description = "지출 날짜" , example = "2022-02-12")
    private LocalDate date;
    @Pattern(regexp = "[0-1]")
	@Schema(description = "지출대상(본인:0, 우리<공동>:1)", defaultValue = "1", allowableValues = {"0", "1"}, example = "1")
    private Integer status;
    // @Schema(description = "지출 정보 관련 이미지 번호" , example = "1")
    // private Long aiSeq;
    @Schema(description = "지출 카테고리 번호" , example = "1")
    private Long cateSeq;
}
