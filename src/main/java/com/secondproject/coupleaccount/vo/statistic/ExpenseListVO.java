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
public class ExpenseListVO {
   @Schema(description="코드번호", example="200")
   private Integer code;
   @Schema(description="메시지", example="지출내역이 조회되었습니다.")
   private String msg;
   @Schema(description="카테고리별 지출내역", example="20000")
   private TotalExpenseVO totalExpenseVO; 
}   
