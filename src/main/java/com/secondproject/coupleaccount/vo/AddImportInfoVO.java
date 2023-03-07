package com.secondproject.coupleaccount.vo;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class AddImportInfoVO {
    @Schema(description = "입금 가격", example = "500000")
    private Integer price;
    @Schema(description = "입금 정보 메모", example = "월급에서 50만원 넣음")
    private String memo;
    @Schema(description = "입금 상태", example = "1")
    private Integer importStatus;
    @Schema(description = "입금 날짜", example = "2023-02-13")
    private LocalDate importDate;
}