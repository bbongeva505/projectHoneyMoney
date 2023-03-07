package com.secondproject.coupleaccount.vo;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class UpdateImportInfoVO {
    @Schema(description = "수정 입금 가격", example = "550000")
    private Integer updatePrice;
     @Schema(description = "입금 정보 메모", example = "월급에서 50만원 +5만원넣음")
     private String updateMemo;
     @Schema(description = "수정 입금 날짜 ", example = "2023-02-23")
     private LocalDate updateDate;
      @Schema(description = "수정 입금 상태 ", example = "1")
     private Integer updateStatus;
}